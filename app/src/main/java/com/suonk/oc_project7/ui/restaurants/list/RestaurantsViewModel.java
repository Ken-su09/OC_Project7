package com.suonk.oc_project7.ui.restaurants.list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.suonk.oc_project7.R;
import com.suonk.oc_project7.domain.workmates.get.GetWorkmatesHaveChosenTodayUseCase;
import com.suonk.oc_project7.model.data.places.CurrentLocation;
import com.suonk.oc_project7.model.data.restaurant.Restaurant;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;
import com.suonk.oc_project7.repositories.current_user_search.CurrentUserSearchRepository;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RestaurantsViewModel extends ViewModel {

    @NonNull
    private final MediatorLiveData<List<RestaurantItemViewState>> viewStatesLiveData = new MediatorLiveData<>();

    @NonNull
    private final CurrentLocationRepository locationRepository;

    private CurrentLocation currentLocation;

    @NonNull
    private final Application application;

    @Inject
    public RestaurantsViewModel(@NonNull CurrentLocationRepository locationRepository, @NonNull GetWorkmatesHaveChosenTodayUseCase getWorkmatesHaveChosenTodayUseCase, @NonNull RestaurantsRepository restaurantsRepository, @NonNull CurrentUserSearchRepository currentUserSearchRepository, @NonNull Application application) {
        this.locationRepository = locationRepository;
        this.application = application;

        LiveData<List<Restaurant>> restaurantsLiveData = Transformations.switchMap(locationRepository.getLocationMutableLiveData(), location -> {
            currentLocation = location;
            String latLng = location.getLat() + "," + location.getLng();
            return restaurantsRepository.getNearRestaurants(latLng);
        });

        LiveData<List<Workmate>> workmatesHaveChosen = getWorkmatesHaveChosenTodayUseCase.getWorkmatesHaveChosenTodayLiveData();
        LiveData<CharSequence> currentUserSearchLiveData = currentUserSearchRepository.getCurrentUserSearchLiveData();

        viewStatesLiveData.addSource(workmatesHaveChosen, workmates -> combine(restaurantsLiveData.getValue(), workmates, currentUserSearchLiveData.getValue()));

        viewStatesLiveData.addSource(restaurantsLiveData, restaurants -> combine(restaurants, workmatesHaveChosen.getValue(), currentUserSearchLiveData.getValue()));

        viewStatesLiveData.addSource(currentUserSearchLiveData, query -> combine(restaurantsLiveData.getValue(), workmatesHaveChosen.getValue(), query));
    }

    private void combine(@Nullable List<Restaurant> restaurants, @Nullable List<Workmate> workmates, @Nullable CharSequence query) {
        List<RestaurantItemViewState> restaurantsItemViews = new ArrayList<>();
        List<String> workmateRestaurantIds = new ArrayList<>();

        if (workmates != null) {
            for (Workmate workmate : workmates) {
                workmateRestaurantIds.add(workmate.getRestaurantId());
            }
        }

        if (restaurants != null) {
            for (Restaurant restaurant : restaurants) {
                float distance = locationRepository.getDistanceFromTwoLocations(currentLocation.getLat(), currentLocation.getLng(), restaurant.getLatitude(), restaurant.getLongitude());

                String isOpen;

                if (restaurant.getOpen()) {
                    isOpen = application.getString(R.string.is_open);
                } else {
                    isOpen = application.getString(R.string.is_close);
                }

                String picture;

                if (restaurant.getPictureUrl().split("photo_reference")[1].equals("=")) {
                    picture = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";
                } else {
                    picture = restaurant.getPictureUrl();
                }

                int numberOfWorkmates = 0;

                if (workmateRestaurantIds.size() != 0) {
                    numberOfWorkmates = Collections.frequency(workmateRestaurantIds, restaurant.getRestaurantId());
                }

                double rating = restaurant.getRating() / 1.66666666667;

                if (query == null || restaurant.getRestaurantName().contains(query)) {
                    restaurantsItemViews.add(new RestaurantItemViewState(restaurant.getRestaurantId(), restaurant.getRestaurantName(), restaurant.getAddress(), isOpen, application.getString(R.string.distance_restaurant, (int) distance), distance, application.getString(R.string.number_of_workmates, numberOfWorkmates), (int) rating, picture));
                }
            }
        }

        Collections.sort(restaurantsItemViews, Comparator.comparingDouble(restaurantItemViewState -> (double) restaurantItemViewState.getDistanceValue()));
        viewStatesLiveData.setValue(restaurantsItemViews);
    }

    public LiveData<List<RestaurantItemViewState>> getRestaurantsLiveData() {
        return viewStatesLiveData;
    }
}