package com.suonk.oc_project7.repositories.places;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.suonk.oc_project7.model.data.restaurant.RestaurantDetails;
import com.suonk.oc_project7.model.data.places.Place;

import java.util.List;

public interface PlacesRepository {

    @NonNull
    LiveData<List<Place>> getNearbyPlaceResponse(@NonNull String location);
}
