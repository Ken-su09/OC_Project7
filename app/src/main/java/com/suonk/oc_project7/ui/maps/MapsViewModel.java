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
import com.suonk.oc_project7.repositories.current_user_search.CurrentUserSearchRepository;
import com.suonk.oc_project7.repositories.places.PlacesRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;
import com.suonk.oc_project7.ui.restaurants.list.RestaurantItemViewState;
import com.suonk.oc_project7.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
                         @NonNull WorkmatesRepository workmatesRepository,
                         @NonNull CurrentUserSearchRepository currentUserSearchRepository) {

        LiveData<List<Place>> listPlacesLiveData = Transformations.switchMap(locationRepository.getLocationMutableLiveData(), location -> {
            String latLng = location.getLat() + "," + location.getLng();
            return placesRepository.getNearbyPlaceResponse(latLng);
        });

        LiveData<List<Workmate>> workmatesHaveChosen = workmatesRepository.getWorkmatesHaveChosenTodayLiveData();
        LiveData<CharSequence> currentUserSearchLiveData = currentUserSearchRepository.getCurrentUserSearchLiveData();

        viewStatesLiveData.addSource(listPlacesLiveData, places -> {
            combine(places, workmatesHaveChosen.getValue(), currentUserSearchLiveData.getValue());
        });

        viewStatesLiveData.addSource(workmatesHaveChosen, workmates -> {
            combine(listPlacesLiveData.getValue(), workmates, currentUserSearchLiveData.getValue());
        });

        viewStatesLiveData.addSource(currentUserSearchLiveData, query -> {
            combine(listPlacesLiveData.getValue(), workmatesHaveChosen.getValue(), query);
        });
    }

    private void combine(@Nullable List<Place> places, @Nullable List<Workmate> workmates,
                         @Nullable CharSequence query) {
        List<String> restaurantIds = new ArrayList<>();

        if (workmates != null) {
            for (Workmate workmate : workmates) {
                restaurantIds.add(workmate.getRestaurantId());
            }
        }
        List<MapMarker> listMapMaker = new ArrayList<>();

        if (places != null) {
            for (Place place : places) {
                int defaultIcon = R.drawable.ic_custom_google_marker_red;

                if (restaurantIds.contains(place.getPlaceId())) {
                    defaultIcon = R.drawable.ic_custom_google_marker_blue;
                }

                if(query == null || place.getRestaurantName().contains(query)){
                    listMapMaker.add(new MapMarker(
                            place.getPlaceId(),
                            place.getLatitude(),
                            place.getLongitude(),
                            place.getRestaurantName(),
                            defaultIcon
                    ));
                }
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