package com.suonk.oc_project7.repositories.places;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.suonk.oc_project7.api.PlacesApiService;
import com.suonk.oc_project7.model.data.places.NearbyPlaceResponse;
import com.suonk.oc_project7.model.data.places.Place;
import com.suonk.oc_project7.model.data.places.NearbyPlaceResult;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesRepositoryImpl implements PlacesRepository {

    @NonNull
    private final PlacesApiService apiService;

    @Inject
    public PlacesRepositoryImpl(@NonNull PlacesApiService apiService) {
        this.apiService = apiService;
    }

    @NonNull
    @Override
    public LiveData<List<Place>> getNearbyPlaceResponse(@NonNull String location) {
        MutableLiveData<List<Place>> placesLiveData = new MutableLiveData<>();

        if (apiService.getNearbyPlacesResponse(location) != null) {
            apiService.getNearbyPlacesResponse(location).enqueue(new Callback<NearbyPlaceResponse>() {
                @Override
                public void onResponse(@NonNull Call<NearbyPlaceResponse> call, @NonNull Response<NearbyPlaceResponse> response) {
                    if (response.isSuccessful()) {
                        List<Place> places = new ArrayList<>();

                        if (response.body() != null) {
                            if (response.body().getResults() != null) {
                                for (NearbyPlaceResult nearbyPlaceResult : response.body().getResults()) {
                                    places.add(new Place(
                                            nearbyPlaceResult.getPlaceId(),
                                            nearbyPlaceResult.getName(),
                                            nearbyPlaceResult.getGeometry().getLocation().getLat(),
                                            nearbyPlaceResult.getGeometry().getLocation().getLng(),
                                            true));
                                }
                            }
                        }

                        placesLiveData.setValue(places);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NearbyPlaceResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }

        return placesLiveData;
    }
}