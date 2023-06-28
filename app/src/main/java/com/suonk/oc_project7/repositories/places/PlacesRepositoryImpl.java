package com.suonk.oc_project7.repositories.places;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.suonk.oc_project7.api.PlacesApiService;
import com.suonk.oc_project7.model.data.place_auto_complete.AutocompleteResponse;
import com.suonk.oc_project7.model.data.place_auto_complete.PlaceAutocomplete;
import com.suonk.oc_project7.model.data.place_auto_complete.Prediction;
import com.suonk.oc_project7.model.data.places.NearbyPlaceResponse;
import com.suonk.oc_project7.model.data.places.NearbyPlaceResult;
import com.suonk.oc_project7.model.data.places.Place;

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
                if (response.isSuccessful()) {
                    List<Place> places = new ArrayList<>();

                    if (response.body() != null) {
                        for (NearbyPlaceResult nearbyPlaceResult : response.body().getResults()) {
                            places.add(new Place(nearbyPlaceResult.getPlaceId(), nearbyPlaceResult.getName(), "", nearbyPlaceResult.getGeometry().getLocation().getLat(), nearbyPlaceResult.getGeometry().getLocation().getLng(), true));
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

    @NonNull
    @Override
    public LiveData<List<PlaceAutocomplete>> getPlacesAutocomplete(@NonNull String location, @NonNull String language, @NonNull String input) {
        MutableLiveData<List<PlaceAutocomplete>> placesLiveData = new MutableLiveData<>();

        if (apiService.getPlacesAutocomplete(location, language, input) != null) {
            apiService.getPlacesAutocomplete(location, language, input).enqueue(new Callback<AutocompleteResponse>() {
                @Override
                public void onResponse(@NonNull Call<AutocompleteResponse> call, @NonNull Response<AutocompleteResponse> response) {

                    if (response.isSuccessful()) {
                        List<PlaceAutocomplete> places = new ArrayList<>();

                        if (response.body() != null) {
                            if (response.body().getPredictions() != null) {
                                for (Prediction prediction : response.body().getPredictions()) {
                                    places.add(new PlaceAutocomplete(prediction.getPlaceId(), prediction.getStructuredFormatting().getMainText(), prediction.getStructuredFormatting().getSecondaryText()));
                                }
                            }
                        }

                        placesLiveData.setValue(places);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AutocompleteResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }

        return placesLiveData;
    }
}