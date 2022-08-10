package com.suonk.oc_project7.ui.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.model.data.places.Place;
import com.suonk.oc_project7.model.data.restaurant.Restaurant;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;
import com.suonk.oc_project7.repositories.places.PlacesRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;
import com.suonk.oc_project7.ui.restaurants.list.RestaurantItemViewState;
import com.suonk.oc_project7.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MapsViewModel extends ViewModel {

    @NonNull
    private final MediatorLiveData<List<MapMarker>> viewStatesLiveData = new MediatorLiveData<>();

    private final SingleLiveEvent<LatLng> cameraPositionSingleEvent = new SingleLiveEvent<>();

    @Inject
    public MapsViewModel(@NonNull CurrentLocationRepository locationRepository,
                         @NonNull PlacesRepository placesRepository,
                         @NonNull WorkmatesRepository workmatesRepository) {

        LiveData<List<Place>> listPlacesLiveData = Transformations.switchMap(locationRepository.getLocationMutableLiveData(), location -> {
            String latLng = location.getLat() + "," + location.getLng();
            return placesRepository.getNearbyPlaceResponse(latLng);
        });

        LiveData<List<Workmate>> workmatesHaveChosen = workmatesRepository.getWorkmatesHaveChosenTodayLiveData();

        viewStatesLiveData.addSource(listPlacesLiveData, places -> {
            combine(places, workmatesHaveChosen.getValue());
        });

        viewStatesLiveData.addSource(workmatesHaveChosen, workmates -> {
            combine(listPlacesLiveData.getValue(), workmates);
        });
    }

    private void combine(@Nullable List<Place> places, @Nullable List<Workmate> workmates) {
        List<String> restaurantIds = new ArrayList<>();

        if (workmates != null) {
            for (Workmate workmate : workmates) {
                restaurantIds.add(workmate.getRestaurantId());
            }
        }
        List<MapMarker> listMapMaker = new ArrayList<>();

        if (places != null) {
            for (Place place : places) {
                float color = 0F;

                if (true) {
                    color = 120.0F;
                } else {
                    color = 0.0F;
                }

                float finalColor = color;

                if (restaurantIds.contains(place.getPlaceId())){

                }


                    listMapMaker.add(new MapMarker(
                            place.getPlaceId(),
                            place.getLatitude(),
                            place.getLongitude(),
                            place.getRestaurantName(),
                            finalColor
                    ));
            }
        }

        viewStatesLiveData.setValue(listMapMaker);
    }

    @NonNull
    public LiveData<List<MapMarker>> getMapMakersLiveData() {
        return viewStatesLiveData;
    }

//    @NonNull
//    public SingleLiveEvent<LatLng> getCameraPositionSingleEvent() {
//        if (cameraPositionSingleEvent.getValue() == null) {
//            cameraPositionSingleEvent.setValue(latLng);
//        }
//        return cameraPositionSingleEvent;
//    }
}