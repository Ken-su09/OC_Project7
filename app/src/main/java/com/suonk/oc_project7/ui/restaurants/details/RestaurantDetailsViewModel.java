package com.suonk.oc_project7.ui.restaurants.details;

import static com.suonk.oc_project7.ui.restaurants.details.RestaurantDetailsActivity.PLACE_ID;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.model.data.places.Place;
import com.suonk.oc_project7.model.data.restaurant.RestaurantDetails;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;
import com.suonk.oc_project7.ui.workmates.WorkmateItemViewState;
import com.suonk.oc_project7.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class RestaurantDetailsViewModel extends ViewModel {

    @NonNull
    private final WorkmatesRepository workmatesRepository;

    @NonNull
    private final MediatorLiveData<RestaurantDetailsViewState> restaurantDetailsViewStateLiveData = new MediatorLiveData<>();

    @NonNull
    private MutableLiveData<List<WorkmateItemViewState>> workmateViewStateLiveData = new MutableLiveData<>();

    @NonNull
    private final SingleLiveEvent<Integer> selectRestaurantButtonIcon = new SingleLiveEvent<>();

    @NonNull
    private final SingleLiveEvent<Integer> likeButtonText = new SingleLiveEvent<>();

    @NonNull
    private Workmate currentWorkmate;

    @NonNull
    private final FirebaseAuth firebaseAuth;

    private final Context context;

    private final String placeId;

    @Inject
    public RestaurantDetailsViewModel(@NonNull WorkmatesRepository workmatesRepository,
                                      @NonNull RestaurantsRepository restaurantsRepository,
                                      @NonNull FirebaseAuth firebaseAuth,
                                      @ApplicationContext Context context,
                                      SavedStateHandle savedStateHandle) {
        this.workmatesRepository = workmatesRepository;
        this.firebaseAuth = firebaseAuth;
        this.context = context;

        placeId = savedStateHandle.get(PLACE_ID);

        LiveData<RestaurantDetails> restaurantDetailsLiveData = restaurantsRepository.getRestaurantDetailsById(placeId);
        LiveData<List<Workmate>> workmatesLiveData = workmatesRepository.getWorkmatesHaveChosenTodayLiveData();
        LiveData<List<Workmate>> allWorkmatesLiveData = workmatesRepository.getAllWorkmatesFromFirestoreLiveData();

        restaurantDetailsViewStateLiveData.addSource(restaurantDetailsLiveData, restaurantDetails -> {
            combine(workmatesRepository.getWorkmatesHaveChosenTodayLiveData().getValue(), restaurantDetails,
                    allWorkmatesLiveData.getValue());
        });

        restaurantDetailsViewStateLiveData.addSource(workmatesLiveData, workmates -> {
            combine(workmates, restaurantDetailsLiveData.getValue(), allWorkmatesLiveData.getValue());
        });

        restaurantDetailsViewStateLiveData.addSource(allWorkmatesLiveData, allWorkmates -> {
            combine(workmatesLiveData.getValue(), restaurantDetailsLiveData.getValue(), allWorkmates);
        });
    }

    private void combine(@Nullable List<Workmate> workmates, @Nullable RestaurantDetails restaurantDetails,
                         @Nullable List<Workmate> allWorkmates) {
        List<WorkmateItemViewState> workmatesItemViews = new ArrayList<>();

        likeButtonText.setValue(R.string.like);

        if (workmates != null && placeId != null) {
            for (Workmate workmate : workmates) {
                if (!firebaseAuth.getCurrentUser().getDisplayName().equals(workmate.getName()) && placeId.equals(workmate.getRestaurantId())) {
                    workmatesItemViews.add(new WorkmateItemViewState(
                            workmate.getId(),
                            context.getString(R.string.workmates_has_joined, workmate.getName()),
                            workmate.getPictureUrl(),
                            Color.BLACK,
                            Typeface.NORMAL
                    ));
                } else if (firebaseAuth.getCurrentUser().getDisplayName().equals(workmate.getName()) && placeId.equals(workmate.getRestaurantId())) {
                    selectRestaurantButtonIcon.setValue(R.drawable.ic_accept);
                }
            }
            workmateViewStateLiveData.setValue(workmatesItemViews);
        }

        if (allWorkmates != null) {
            for (Workmate workmate : allWorkmates) {
                if (firebaseAuth.getCurrentUser().getDisplayName().equals(workmate.getName())) {
                    currentWorkmate = workmate;
                    break;
                }
            }
        }

        Log.i("currentWorkmate", "currentWorkmate : " + currentWorkmate);
        if (currentWorkmate != null) {
            for (String like : currentWorkmate.getListOfLikes()) {
                Log.i("currentWorkmate", "like : " + like);
                if (like.equals(placeId)) {
                    likeButtonText.setValue(R.string.dislike);
                    break;
                }
            }
        }

        if (restaurantDetails != null) {
            String picture = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";

            if (restaurantDetails.getImage().split("photo_reference").length == 2) {
                if (!restaurantDetails.getImage().split("photo_reference")[1].equals("=")) {
                    picture = restaurantDetails.getImage();
                }
            }

            restaurantDetailsViewStateLiveData.setValue(new RestaurantDetailsViewState(
                    restaurantDetails.getPlaceId(),
                    restaurantDetails.getRestaurantName(),
                    restaurantDetails.getAddress(),
                    restaurantDetails.getRating().intValue(),
                    picture,
                    restaurantDetails.getPhoneNumber(),
                    restaurantDetails.getWebsiteLink()));
        }
    }

    public LiveData<List<WorkmateItemViewState>> getWorkmatesLiveData() {
        return workmateViewStateLiveData;
    }

    public LiveData<RestaurantDetailsViewState> getRestaurantDetailsViewStateLiveData() {
        return restaurantDetailsViewStateLiveData;
    }

    public SingleLiveEvent<Integer> getSelectRestaurantButtonIcon() {
        Log.i("currentWorkmate", "selectRestaurantButtonIcon : " + selectRestaurantButtonIcon);
        return selectRestaurantButtonIcon;
    }

    public SingleLiveEvent<Integer> getLikeButtonText() {
        return likeButtonText;
    }

    public void addWorkmate() {
        workmatesRepository.addWorkmateToHaveChosenTodayList(firebaseAuth.getCurrentUser(), placeId);
    }

    public void likeRestaurant() {
        List<String> ids;

        if (currentWorkmate != null) {
            if (currentWorkmate.getListOfLikes() != null) {
                ids = currentWorkmate.getListOfLikes();
            } else {
                ids = new ArrayList<>();
            }

            if (ids.contains(placeId)) {
                ids.remove(placeId);
            } else {
                ids.add(placeId);
            }
            workmatesRepository.likeRestaurant(firebaseAuth.getCurrentUser(), ids);
        }
    }
}