package com.suonk.oc_project7.repositories.restaurants;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.suonk.oc_project7.BuildConfig;
import com.suonk.oc_project7.api.PlacesApiService;
import com.suonk.oc_project7.model.data.places.NearbyPlaceResponse;
import com.suonk.oc_project7.model.data.places.Place;
import com.suonk.oc_project7.model.data.places.Result;
import com.suonk.oc_project7.model.data.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsRepositoryImpl implements RestaurantsRepository {

    @NonNull
    private final PlacesApiService apiService;

    @Inject
    public RestaurantsRepositoryImpl(@NonNull PlacesApiService apiService) {
        this.apiService = apiService;
    }

    @NonNull
    @Override
    public LiveData<List<Restaurant>> getNearRestaurants(@NonNull String location) {
        MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();

        if (apiService.getNearbyPlacesResponse(location) != null) {
            apiService.getNearbyPlacesResponse(location).enqueue(new Callback<NearbyPlaceResponse>() {
                @Override
                public void onResponse(@NonNull Call<NearbyPlaceResponse> call, @NonNull Response<NearbyPlaceResponse> response) {
                    if (response.isSuccessful()) {
                        List<Restaurant> restaurants = new ArrayList<>();

                        if (response.body() != null) {
                            if (response.body().getResults() != null) {
                                for (Result result : response.body().getResults()) {

                                    String photoReference = "";
                                    Boolean isOpen = false;

                                    if (result.getPhotos() != null) {
                                        photoReference = result.getPhotos().get(0).getPhotoReference();
                                    }

                                    if (result.getOpeningHours() != null) {
                                        isOpen = result.getOpeningHours().getOpenNow();
                                    }

                                    restaurants.add(new Restaurant(
                                            result.getPlaceId(),
                                            result.getName(),
                                            result.getVicinity(),
                                            isOpen,
                                            result.getRating(),
                                            result.getGeometry().getLocation().getLat(),
                                            result.getGeometry().getLocation().getLng(),
                                            getRestaurantPictureURL(photoReference)
                                    ));
                                }
                            }
                        }

                        restaurantsLiveData.setValue(restaurants);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NearbyPlaceResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }

        return restaurantsLiveData;
    }

    @NonNull
    @Override
    public LiveData<Restaurant> getNearRestaurantById(@NonNull String location, @NonNull String placeId) {
        MutableLiveData<Restaurant> restaurantLiveData = new MutableLiveData<>();

        apiService.getNearbyPlacesResponse(location).enqueue(new Callback<NearbyPlaceResponse>() {
            @Override
            public void onResponse(@NonNull Call<NearbyPlaceResponse> call, @NonNull Response<NearbyPlaceResponse> response) {
                if (response.isSuccessful()) {
                    Restaurant restaurant = null;

                    if (response.body() != null) {

                        if (response.body().getResults() != null) {
                            for (Result result : response.body().getResults()) {
                                String photoReference = "";
                                Boolean isOpen = false;

                                if (result.getPhotos() != null) {
                                    photoReference = result.getPhotos().get(0).getPhotoReference();
                                }

                                if (result.getOpeningHours() != null) {
                                    isOpen = result.getOpeningHours().getOpenNow();
                                }

                                if (result.getPlaceId().equals(placeId)) {
                                    restaurant = new Restaurant(
                                            result.getPlaceId(),
                                            result.getName(),
                                            result.getVicinity(),
                                            isOpen,
                                            result.getRating(),
                                            result.getGeometry().getLocation().getLat(),
                                            result.getGeometry().getLocation().getLng(),
                                            getRestaurantPictureURL(photoReference)
                                    );
                                }
                            }
                        }
                    }

                    restaurantLiveData.setValue(restaurant);
                }
            }

            @Override
            public void onFailure(@NonNull Call<NearbyPlaceResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

        return restaurantLiveData;
    }

    private String getRestaurantPictureURL(@NonNull String photo_reference) {
        return "https://maps.googleapis.com/" +
                "maps/api/place/photo" +
                "?maxwidth=400" +
                "&key=" + BuildConfig.MAPS_API_KEY +
                "&photo_reference=" + photo_reference;
    }
}