package com.suonk.oc_project7.ui.restaurants.details;

import android.content.Context;

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
import com.suonk.oc_project7.ui.restaurants.list.RestaurantItemViewState;
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
    private CurrentLocation currentLocation;

    @NonNull
    private final SingleLiveEvent<Boolean> ratingSingleLiveEvent = new SingleLiveEvent<>();

    @Inject
    public RestaurantDetailsViewModel(@NonNull CurrentLocationRepository locationRepository,
                                      @NonNull PlacesRepository placesRepository,
                                      @NonNull RestaurantsRepository restaurantsRepository,
                                      @ApplicationContext Context context) {
        this.restaurantsRepository = restaurantsRepository;
        this.locationRepository = locationRepository;
    }

    @NonNull
    public LiveData<RestaurantDetailsViewState> getRestaurantDetailsLiveData(String id) {
        LiveData<Restaurant> restaurantLiveData = Transformations.switchMap(locationRepository.getLocationMutableLiveData(), location -> {
            String latLng = location.getLat() + "," + location.getLng();
            return restaurantsRepository.getNearRestaurantById(latLng, id);
        });

        return Transformations.map(restaurantLiveData, restaurant ->
                new RestaurantDetailsViewState(
                        restaurant.getRestaurantId(),
                        restaurant.getRestaurantName(),
                        restaurant.getAddress(),
                        restaurant.getRating().toString(),
                        restaurant.getPictureUrl()));
    }

    @NonNull
    public SingleLiveEvent<Boolean> getRatingSingleLiveEvent() {
        return ratingSingleLiveEvent;
    }
}
