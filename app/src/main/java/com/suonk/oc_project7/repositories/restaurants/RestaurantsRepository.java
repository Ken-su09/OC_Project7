package com.suonk.oc_project7.repositories.restaurants;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.suonk.oc_project7.model.data.restaurant.Restaurant;

import java.util.List;

public interface RestaurantsRepository {

    @NonNull
    LiveData<List<Restaurant>> getNearRestaurants(@NonNull String location);

    @NonNull
    LiveData<Restaurant> getNearRestaurantById(@NonNull String location, @NonNull String placeId);
}
