package com.suonk.oc_project7.ui.restaurants.details;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.model.data.places.CurrentLocation;
import com.suonk.oc_project7.model.data.restaurant.Restaurant;
import com.suonk.oc_project7.model.data.restaurant.RestaurantDetails;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;
import com.suonk.oc_project7.repositories.places.PlacesRepository;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;
import com.suonk.oc_project7.ui.restaurants.list.RestaurantItemViewState;
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
    private final RestaurantsRepository restaurantsRepository;
    @NonNull
    private final CurrentLocationRepository locationRepository;
    @NonNull
    private final WorkmatesRepository workmatesRepository;

    @NonNull
    private final SingleLiveEvent<Boolean> ratingSingleLiveEvent = new SingleLiveEvent<>();

    @NonNull
    private final SingleLiveEvent<Integer> haveChosenSingleLiveEvent = new SingleLiveEvent<>();

    @NonNull
    private final MediatorLiveData<RestaurantDetailsViewState> restaurantDetailsViewStateLiveData = new MediatorLiveData<>();

    @NonNull
    private MutableLiveData<List<WorkmateItemViewState>> workmateViewStateLiveData = new MutableLiveData<>();

    @NonNull
    private MutableLiveData<String> restaurantIdLiveData = new MutableLiveData<>();

    @NonNull
    private final FirebaseAuth firebaseAuth;

    final Context context;

    @Inject
    public RestaurantDetailsViewModel(@NonNull CurrentLocationRepository locationRepository,
                                      @NonNull WorkmatesRepository workmatesRepository,
                                      @NonNull RestaurantsRepository restaurantsRepository,
                                      @NonNull FirebaseAuth firebaseAuth,
                                      @ApplicationContext Context context) {
        this.restaurantsRepository = restaurantsRepository;
        this.workmatesRepository = workmatesRepository;
        this.locationRepository = locationRepository;
        this.firebaseAuth = firebaseAuth;
        this.context = context;
    }

    public void setRestaurantDetailsLiveData(String place_id) {
        LiveData<RestaurantDetails> restaurantDetailsLiveData = restaurantsRepository.getRestaurantDetailsById(place_id);
        LiveData<List<Workmate>> workmatesLiveData = workmatesRepository.getWorkmatesHaveChosenTodayLiveData();
        restaurantIdLiveData.setValue(place_id);

        restaurantDetailsViewStateLiveData.addSource(restaurantDetailsLiveData, restaurantDetails -> {
            combine(workmatesRepository.getWorkmatesHaveChosenTodayLiveData().getValue(), restaurantDetails, restaurantIdLiveData.getValue());
        });

        restaurantDetailsViewStateLiveData.addSource(workmatesLiveData, workmates -> {
            combine(workmates, restaurantDetailsLiveData.getValue(), restaurantIdLiveData.getValue());
        });

        restaurantDetailsViewStateLiveData.addSource(restaurantIdLiveData, restaurantId -> {
            combine(workmatesRepository.getWorkmatesHaveChosenTodayLiveData().getValue(), restaurantDetailsLiveData.getValue(), restaurantId);
        });
    }

    private void combine(@Nullable List<Workmate> workmates, @Nullable RestaurantDetails restaurantDetails, @Nullable String restaurantId) {
        List<WorkmateItemViewState> workmatesItemViews = new ArrayList<>();
        haveChosenSingleLiveEvent.setValue(R.drawable.ic_accept);

        if (workmates != null) {
            for (Workmate workmate : workmates) {
                if (!firebaseAuth.getCurrentUser().getDisplayName().equals(workmate.getName())) {
                    if (restaurantId != null) {
                        if (restaurantId.equals(workmate.getRestaurantId())) {
                            workmatesItemViews.add(new WorkmateItemViewState(
                                    workmate.getId(),
                                    context.getString(R.string.workmates_has_joined, workmate.getName()),
                                    workmate.getPictureUrl(),
                                    Color.BLACK,
                                    Typeface.NORMAL
                            ));
                        }
                    }
                }
            }
            workmateViewStateLiveData.setValue(workmatesItemViews);
        }

        if (restaurantDetails != null) {
            String picture = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";

//            if (restaurantDetails.getImage().split("photo_reference")[1].equals("=")) {
//                picture = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";
//            } else {
//                picture = restaurantDetails.getImage();
//            }

            if (restaurantDetails.getImage().equals("")) {
                picture = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";
            } else {
                picture = restaurantDetails.getImage();
            }

            restaurantDetailsViewStateLiveData.setValue(new RestaurantDetailsViewState(
                    restaurantDetails.getPlaceId(),
                    restaurantDetails.getRestaurantName(),
                    restaurantDetails.getAddress(),
                    restaurantDetails.getRating().toString(),
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

    public void addWorkmate(@NonNull FirebaseUser firebaseUser, @NonNull String restaurantId) {
        workmatesRepository.addWorkmateToHaveChosenTodayList(firebaseUser, restaurantId);
    }

    @NonNull
    public SingleLiveEvent<Boolean> getRatingSingleLiveEvent() {
        return ratingSingleLiveEvent;
    }

    @NonNull
    public SingleLiveEvent<Integer> getHaveChosenSingleLiveEvent() {
        return haveChosenSingleLiveEvent;
    }
}