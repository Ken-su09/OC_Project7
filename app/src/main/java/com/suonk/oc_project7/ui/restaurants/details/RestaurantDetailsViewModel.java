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
    private final MediatorLiveData<RestaurantDetailsViewState> restaurantDetailsViewStateLiveData = new MediatorLiveData<>();

    @NonNull
    private MutableLiveData<List<WorkmateItemViewState>> workmateViewStateLiveData = new MutableLiveData<>();

    @NonNull
    private final SingleLiveEvent<Integer> selectRestaurantButtonIcon = new SingleLiveEvent<>();

    @NonNull
    private final FirebaseAuth firebaseAuth;

    private final Context context;

    private final String placeId;

    @Inject
    public RestaurantDetailsViewModel(@NonNull CurrentLocationRepository locationRepository,
                                      @NonNull WorkmatesRepository workmatesRepository,
                                      @NonNull RestaurantsRepository restaurantsRepository,
                                      @NonNull FirebaseAuth firebaseAuth,
                                      @ApplicationContext Context context,
                                      SavedStateHandle savedStateHandle) {
        this.restaurantsRepository = restaurantsRepository;
        this.workmatesRepository = workmatesRepository;
        this.locationRepository = locationRepository;
        this.firebaseAuth = firebaseAuth;
        this.context = context;

        placeId = savedStateHandle.get(PLACE_ID);

        LiveData<RestaurantDetails> restaurantDetailsLiveData = restaurantsRepository.getRestaurantDetailsById(placeId);
        LiveData<List<Workmate>> workmatesLiveData = workmatesRepository.getWorkmatesHaveChosenTodayLiveData();

//        LiveData<List<Workmate>> restaurantLiveData = Transformations.switchMap(locationRepository.getLocationMutableLiveData(), currentLocation -> {
//            String location = currentLocation.getLat() + "," + currentLocation.getLng();
//            return restaurantsRepository.getNearRestaurants(location);
//        });

//        restaurantDetailsViewStateLiveData.addSource(locationRepository.getLocationMutableLiveData(), currentLocation -> {
//            String location = currentLocation.getLat() + "," + currentLocation.getLng();
//            restaurantsRepository.getNearRestaurants(location);
//
//        });

        restaurantDetailsViewStateLiveData.addSource(restaurantDetailsLiveData, restaurantDetails -> {
            combine(workmatesRepository.getWorkmatesHaveChosenTodayLiveData().getValue(), restaurantDetails);
        });

        restaurantDetailsViewStateLiveData.addSource(workmatesLiveData, workmates -> {
            combine(workmates, restaurantDetailsLiveData.getValue());
        });
    }

    private void combine(@Nullable List<Workmate> workmates, @Nullable RestaurantDetails restaurantDetails) {
        List<WorkmateItemViewState> workmatesItemViews = new ArrayList<>();

        selectRestaurantButtonIcon.setValue(R.drawable.ic_to_select);

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
                } else {
                    selectRestaurantButtonIcon.setValue(R.drawable.ic_accept);
                }
            }
            workmateViewStateLiveData.setValue(workmatesItemViews);
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

    public SingleLiveEvent<Integer> getSelectRestaurantButtonIcon() {
        return selectRestaurantButtonIcon;
    }

    public void addWorkmate() {
        workmatesRepository.addWorkmateToHaveChosenTodayList(firebaseAuth.getCurrentUser(), placeId);
    }
}