package com.suonk.oc_project7.ui.restaurants.list;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.suonk.oc_project7.model.data.places.CurrentLocation;
import com.suonk.oc_project7.model.data.restaurant.Restaurant;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;
import com.suonk.oc_project7.repositories.places.PlacesRepository;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepository;
import com.suonk.oc_project7.utils.SingleLiveEvent;

import java.util.ArrayList;
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

    @Inject
    public RestaurantsViewModel(@NonNull CurrentLocationRepository locationRepository,
                                @NonNull PlacesRepository placesRepository,
                                @NonNull RestaurantsRepository restaurantsRepository,
                                @ApplicationContext Context context) {

        LiveData<List<Restaurant>> restaurantsLiveData = Transformations.switchMap(locationRepository.getLocationMutableLiveData(), location -> {
            currentLocation = location;
            String latLng = location.getLat() + "," + location.getLng();
            return restaurantsRepository.getNearRestaurants(latLng);
        });

        viewStatesLiveData.addSource(restaurantsLiveData, this::combine);
    }

    private void combine(@NonNull List<Restaurant> restaurants) {
        List<RestaurantItemViewState> restaurantsItemViews = new ArrayList<>();

        for (Restaurant restaurant : restaurants) {
            float distance = getDistanceFromTwoLocations(currentLocation.getLat(), currentLocation.getLng(),
                    restaurant.getLatitude(), restaurant.getLongitude());

            String isOpen = "";

            if (restaurant.getOpen()) {
                isOpen = "Is Open";
            } else {
                isOpen = "Is Close";
            }

            String picture = "";

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
                    (int) distance + "m",
                    "",
                    restaurant.getRating().toString(),
                    picture
            ));
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