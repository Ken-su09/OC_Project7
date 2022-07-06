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
    private MutableLiveData<String> currentRestaurantId = new MutableLiveData<>();

    @NonNull
    private final SingleLiveEvent<Boolean> ratingSingleLiveEvent = new SingleLiveEvent<>();

    @NonNull
    private final SingleLiveEvent<Integer> haveChosenSingleLiveEvent = new SingleLiveEvent<>();

    @NonNull
    private final MediatorLiveData<List<WorkmateItemViewState>> viewStatesLiveData = new MediatorLiveData<>();

    @Inject
    public RestaurantDetailsViewModel(@NonNull CurrentLocationRepository locationRepository,
                                      @NonNull WorkmatesRepository workmatesRepository,
                                      @NonNull RestaurantsRepository restaurantsRepository,
                                      @ApplicationContext Context context) {
        this.restaurantsRepository = restaurantsRepository;
        this.workmatesRepository = workmatesRepository;
        this.locationRepository = locationRepository;

        viewStatesLiveData.addSource(workmatesRepository.getWorkmatesHaveChosenTodayLiveData(), this::combine);
    }

    @NonNull
    public LiveData<RestaurantDetailsViewState> getRestaurantDetailsLiveData(String id) {

        LiveData<Restaurant> restaurantLiveData = Transformations.switchMap(locationRepository.getLocationMutableLiveData(), location -> {
            String latLng = location.getLat() + "," + location.getLng();
            return restaurantsRepository.getNearRestaurantById(latLng, id);
        });

        return Transformations.map(restaurantLiveData, restaurant -> {
            String picture = "";

            if (restaurant.getPictureUrl().split("photo_reference")[1].equals("=")) {
                picture = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";
            } else {
                picture = restaurant.getPictureUrl();
            }

            currentRestaurantId.setValue(restaurant.getRestaurantId());

            return new RestaurantDetailsViewState(
                    restaurant.getRestaurantId(),
                    restaurant.getRestaurantName(),
                    restaurant.getAddress(),
                    restaurant.getRating().toString(),
                    picture);
        });
    }

    private void combine(@Nullable List<Workmate> workmates) {
        List<WorkmateItemViewState> workmatesItemViews = new ArrayList<>();
        haveChosenSingleLiveEvent.setValue(R.drawable.ic_accept);

        if (workmates != null) {
            for (Workmate workmate : workmates) {
                if (!FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equals(workmate.getName())) {
                    if (currentRestaurantId.getValue().equals(workmate.getRestaurantId())) {
                        workmatesItemViews.add(new WorkmateItemViewState(
                                workmate.getId(),
                                workmate.getName() + " has joined !",
                                workmate.getPictureUrl(),
                                Color.BLACK,
                                Typeface.NORMAL
                        ));
                    }
                } else {
                    if (currentRestaurantId.getValue().equals(workmate.getRestaurantId())) {
                        haveChosenSingleLiveEvent.setValue(R.drawable.ic_remove);
                    }
                }
            }
        }

        viewStatesLiveData.setValue(workmatesItemViews);
    }

    public LiveData<List<WorkmateItemViewState>> getWorkmatesLiveData() {
        return viewStatesLiveData;
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