package com.suonk.oc_project7.ui.map;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.domain.workmates.get.GetWorkmatesHaveChosenTodayUseCase;
import com.suonk.oc_project7.model.data.places.CurrentLocation;
import com.suonk.oc_project7.model.data.places.Place;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;
import com.suonk.oc_project7.repositories.current_user_search.CurrentUserSearchRepository;
import com.suonk.oc_project7.repositories.places.PlacesRepository;
import com.suonk.oc_project7.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MapViewModel extends ViewModel {

    @NonNull
    private final MediatorLiveData<List<MapMarker>> viewStatesLiveData = new MediatorLiveData<>();

    private final SingleLiveEvent<LatLng> cameraPositionSingleEvent = new SingleLiveEvent<>();

    private LatLng latLng;

    private final Application application;

    @Inject
    public MapViewModel(@NonNull CurrentLocationRepository locationRepository, @NonNull PlacesRepository placesRepository, @NonNull GetWorkmatesHaveChosenTodayUseCase getWorkmatesHaveChosenTodayUseCase, @NonNull CurrentUserSearchRepository currentUserSearchRepository, Application application) {
        this.application = application;

        LiveData<CurrentLocation> currentLocationLiveData = locationRepository.getLocationMutableLiveData();

        LiveData<List<Place>> listPlacesLiveData = Transformations.switchMap(currentLocationLiveData, location -> {
            latLng = new LatLng(location.getLat(), location.getLng());
            String latLng = location.getLat() + "," + location.getLng();
            return placesRepository.getNearbyPlaceResponse(latLng);
        });

        LiveData<List<Workmate>> workmatesHaveChosen = getWorkmatesHaveChosenTodayUseCase.getWorkmatesHaveChosenTodayLiveData();
        LiveData<CharSequence> currentUserSearchLiveData = currentUserSearchRepository.getCurrentUserSearchLiveData();

        viewStatesLiveData.addSource(currentLocationLiveData, location -> combine(location, listPlacesLiveData.getValue(), workmatesHaveChosen.getValue(), currentUserSearchLiveData.getValue()));
        viewStatesLiveData.addSource(listPlacesLiveData, places -> combine(currentLocationLiveData.getValue(), places, workmatesHaveChosen.getValue(), currentUserSearchLiveData.getValue()));
        viewStatesLiveData.addSource(workmatesHaveChosen, workmates -> combine(currentLocationLiveData.getValue(), listPlacesLiveData.getValue(), workmates, currentUserSearchLiveData.getValue()));
        viewStatesLiveData.addSource(currentUserSearchLiveData, query -> combine(currentLocationLiveData.getValue(), listPlacesLiveData.getValue(), workmatesHaveChosen.getValue(), query));
    }

    private void combine(@Nullable CurrentLocation currentLocation, @Nullable List<Place> places, @Nullable List<Workmate> workmates, @Nullable CharSequence query) {
        List<String> restaurantIds = new ArrayList<>();
        if (workmates != null) {
            for (Workmate workmate : workmates) {
                restaurantIds.add(workmate.getRestaurantId());
            }
        }

        List<MapMarker> listMapMaker = new ArrayList<>();

        if (places != null) {
            for (Place place : places) {
                int defaultIcon;

                if (restaurantIds.contains(place.getPlaceId())) {
                    defaultIcon = R.drawable.ic_custom_google_marker_blue;
                } else {
                    defaultIcon = R.drawable.ic_custom_google_marker_red;
                }

                if (query == null || place.getRestaurantName().contains(query)) {
                    listMapMaker.add(new MapMarker(place.getPlaceId(), place.getLatitude(), place.getLongitude(), place.getRestaurantName(), place.getRestaurantAddress(), defaultIcon));
                }
            }

            if (currentLocation != null) {
                listMapMaker.add(new MapMarker("", currentLocation.getLat(), currentLocation.getLng(), application.getString(R.string.my_position), "", R.drawable.custom_google_marker_user));
            }
        }

        viewStatesLiveData.setValue(listMapMaker);
    }

    @NonNull
    public LiveData<List<MapMarker>> getMapViewStateLiveData() {
        return viewStatesLiveData;
    }

    @NonNull
    public SingleLiveEvent<LatLng> getCameraPositionSingleEvent() {
        if (cameraPositionSingleEvent.getValue() == null) {
            cameraPositionSingleEvent.setValue(latLng);
        }
        return cameraPositionSingleEvent;
    }
}