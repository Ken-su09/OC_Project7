package com.suonk.oc_project7.ui.restaurants.list;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.model.data.places.CurrentLocation;
import com.suonk.oc_project7.model.data.restaurant.Restaurant;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;
import com.suonk.oc_project7.repositories.places.PlacesRepository;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;
import com.suonk.oc_project7.ui.workmates.WorkmateItemViewState;
import com.suonk.oc_project7.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class RestaurantsViewModel extends ViewModel {

    @NonNull
    private final MediatorLiveData<List<RestaurantItemViewState>> viewStatesLiveData = new MediatorLiveData<>();

    @NonNull
    private CurrentLocation currentLocation;

    @NonNull
    private final SingleLiveEvent<Boolean> ratingSingleLiveEvent = new SingleLiveEvent<>();

    final Context context;

    @Inject
    public RestaurantsViewModel(@NonNull CurrentLocationRepository locationRepository,
                                @NonNull WorkmatesRepository workmatesRepository,
                                @NonNull RestaurantsRepository restaurantsRepository,
                                @ApplicationContext Context context) {
        this.context = context;
        LiveData<List<Restaurant>> restaurantsLiveData = Transformations.switchMap(locationRepository.getLocationMutableLiveData(), location -> {
            currentLocation = location;
            String latLng = location.getLat() + "," + location.getLng();
            return restaurantsRepository.getNearRestaurants(latLng);
        });

        LiveData<List<Workmate>> workmatesHaveChosen = workmatesRepository.getWorkmatesHaveChosenTodayLiveData();

        viewStatesLiveData.addSource(workmatesHaveChosen, workmates -> {
            combine(restaurantsLiveData.getValue(), workmates);
        });

        viewStatesLiveData.addSource(restaurantsLiveData, restaurants -> {
            combine(restaurants, workmatesHaveChosen.getValue());
        });
    }

    private void combine(@Nullable List<Restaurant> restaurants, @Nullable List<Workmate> workmates) {
        List<RestaurantItemViewState> restaurantsItemViews = new ArrayList<>();
        List<String> ids = new ArrayList<>();

        if (workmates != null) {
            for (Workmate workmate : workmates) {
                ids.add(workmate.getRestaurantId());
            }
        }

        if (restaurants != null) {
            for (Restaurant restaurant : restaurants) {
                float distance = getDistanceFromTwoLocations(currentLocation.getLat(), currentLocation.getLng(),
                        restaurant.getLatitude(), restaurant.getLongitude());

                String isOpen;

                if (restaurant.getOpen()) {
                    isOpen = "Is Open";
                } else {
                    isOpen = "Is Close";
                }

                String picture;

                if (restaurant.getPictureUrl().split("photo_reference")[1].equals("=")) {
                    picture = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";
                } else {
                    picture = restaurant.getPictureUrl();
                }

                restaurantsItemViews.add(new RestaurantItemViewState(
                        restaurant.getRestaurantId(),
                        restaurant.getRestaurantName(),
                        restaurant.getAddress(),
                        isOpen,
                        context.getString(R.string.distance_restaurant, (int) distance),
                        context.getString(R.string.number_of_workmates, Collections.frequency(ids, restaurant.getRestaurantId())),
                        restaurant.getRating().toString(),
                        picture
                ));
            }
        }

        viewStatesLiveData.setValue(restaurantsItemViews);
    }

    public LiveData<List<RestaurantItemViewState>> getRestaurantsLiveData() {
        return viewStatesLiveData;
    }

    @NonNull
    public SingleLiveEvent<Boolean> getRatingSingleLiveEvent() {
        return ratingSingleLiveEvent;
    }

    private float getDistanceFromTwoLocations(double startLat, double startLng,
                                              double endLat, double endLng) {
        Location startPoint = new Location("currentLocation");
        startPoint.setLatitude(startLat);
        startPoint.setLongitude(startLng);

        Location endPoint = new Location("restaurantLocation");
        endPoint.setLatitude(endLat);
        endPoint.setLongitude(endLng);

        return startPoint.distanceTo(endPoint);
    }
}