package com.suonk.oc_project7.repositories.places;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.suonk.oc_project7.api.PlacesApiService;
import com.suonk.oc_project7.model.data.places.NearbyPlaceResponse;
import com.suonk.oc_project7.model.data.places.Place;
import com.suonk.oc_project7.model.data.places.Result;

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

        apiService.getNearbyPlacesResponse(location).enqueue(new Callback<NearbyPlaceResponse>() {
            @Override
            public void onResponse(@NonNull Call<NearbyPlaceResponse> call, @NonNull Response<NearbyPlaceResponse> response) {
                Log.i("NearbyPlaceResponse", "response.isSuccessful() : " + response.isSuccessful());
                if (response.isSuccessful()) {
                    List<Place> places = new ArrayList<>();

                    Log.i("NearbyPlaceResponse", "response.body() : " + response.body());
                    if (response.body() != null) {
                        if (response.body().getResults() != null) {
                            for (Result result : response.body().getResults()) {
                                Log.i("NearbyPlaceResponse", "result : " + result);
                                places.add(new Place(
                                        result.getPlace_id(),
                                        result.getName(),
                                        result.getGeometry().getLocation().getLat(),
                                        result.getGeometry().getLocation().getLng(),
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

        return placesLiveData;
    }
}