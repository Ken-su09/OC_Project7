package com.suonk.oc_project7.repositories.places;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.suonk.oc_project7.model.data.place_auto_complete.PlaceAutocomplete;
import com.suonk.oc_project7.model.data.places.Place;

import java.util.List;

public interface PlacesRepository {

    @NonNull
    LiveData<List<Place>> getNearbyPlaceResponse(@NonNull String location);

    @NonNull
    LiveData<List<PlaceAutocomplete>> getPlacesAutocomplete(@NonNull String location,
                                                            @NonNull String language,
                                                            @NonNull String input);
}
