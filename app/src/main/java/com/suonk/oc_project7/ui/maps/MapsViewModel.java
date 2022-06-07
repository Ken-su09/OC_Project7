package com.suonk.oc_project7.ui.maps;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.suonk.oc_project7.model.data.places.Place;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;
import com.suonk.oc_project7.repositories.places.PlacesRepository;
import com.suonk.oc_project7.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MapsViewModel extends ViewModel {

    @NonNull
    private final LiveData<List<MapMarker>> mapMarkerLiveData;

    private final SingleLiveEvent<LatLng> cameraPositionSingleEvent = new SingleLiveEvent<>();

    private LatLng latLng;

    @Inject
    public MapsViewModel(@NonNull CurrentLocationRepository locationRepository,
                         @NonNull PlacesRepository placesRepository) {

        LiveData<List<Place>> listPlacesLiveData = Transformations.switchMap(locationRepository.getLocationMutableLiveData(), location -> {
            String latLng = location.getLat() + "," + location.getLng();
            this.latLng = new LatLng(location.getLat(), location.getLng());
            return placesRepository.getNearbyPlaceResponse(latLng);
        });

        float color = 0F;

        if (true) {
            color = 120.0F;
        } else {
            color = 0.0F;
        }

        float finalColor = color;

        mapMarkerLiveData = Transformations.map(listPlacesLiveData, places -> {
            List<MapMarker> listMapMaker = new ArrayList<>();

            for (Place place : places) {
                listMapMaker.add(new MapMarker(
                        place.getPlaceId(),
                        place.getLatitude(),
                        place.getLongitude(),
                        place.getRestaurantName(),
                        finalColor
                ));
            }

            return listMapMaker;
        });
    }

    @NonNull
    public LiveData<List<MapMarker>> getMapMakersLiveData() {
        return mapMarkerLiveData;
    }

    @NonNull
    public SingleLiveEvent<LatLng> getCameraPositionSingleEvent() {
        if (cameraPositionSingleEvent.getValue() == null) {
            cameraPositionSingleEvent.setValue(latLng);
        }
        return cameraPositionSingleEvent;
    }
}