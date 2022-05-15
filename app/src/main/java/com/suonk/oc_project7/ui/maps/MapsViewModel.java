package com.suonk.oc_project7.ui.maps;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.suonk.oc_project7.model.data.places.Place;
import com.suonk.oc_project7.repositories.location.LocationRepository;
import com.suonk.oc_project7.repositories.places.PlacesRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class MapsViewModel extends ViewModel {

    @NonNull
    private final LiveData<List<MapMarker>> mapMarkerLiveData;

    @Inject
    public MapsViewModel(@NonNull LocationRepository locationRepository,
                         @NonNull PlacesRepository placesRepository,
                         @ApplicationContext Context context) {

        LiveData<List<Place>> listPlacesLiveData = Transformations.switchMap(locationRepository.getLocationMutableLiveData(), location -> {
            String latLng = location.getLatitude() + "," + location.getLongitude();
            return placesRepository.getNearbyPlaceResponse(latLng);
        });

        mapMarkerLiveData = Transformations.map(listPlacesLiveData, places -> {
            List<MapMarker> listMapMaker = new ArrayList<>();

            for (Place place : places) {
                listMapMaker.add(new MapMarker(
                        place.getPlaceId(),
                        place.getLatitude(),
                        place.getLongitude(),
                        place.getRestaurantName()
                ));
            }

            return listMapMaker;
        });
    }

    @NonNull
    public LiveData<List<MapMarker>> getMapMakersLiveData() {
        return mapMarkerLiveData;
    }
}