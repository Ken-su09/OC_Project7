package com.suonk.oc_project7.repositories.restaurants;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.suonk.oc_project7.BuildConfig;
import com.suonk.oc_project7.api.PlacesApiService;
import com.suonk.oc_project7.model.data.restaurant.RestaurantDetails;
import com.suonk.oc_project7.model.data.place_details.PlaceDetailsResponse;
import com.suonk.oc_project7.model.data.place_details.Result;
import com.suonk.oc_project7.model.data.places.NearbyPlaceResponse;
import com.suonk.oc_project7.model.data.places.NearbyPlaceResult;
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
                                for (NearbyPlaceResult nearbyPlaceResult : response.body().getResults()) {

                                    String photoReference = "";
                                    Boolean isOpen = false;

                                    if (nearbyPlaceResult.getPhotos() != null) {
                                        photoReference = nearbyPlaceResult.getPhotos().get(0).getPhotoReference();
                                    }

                                    if (nearbyPlaceResult.getOpeningHours() != null) {
                                        isOpen = nearbyPlaceResult.getOpeningHours().getOpenNow();
                                    }

                                    restaurants.add(new Restaurant(
                                            nearbyPlaceResult.getPlaceId(),
                                            nearbyPlaceResult.getName(),
                                            nearbyPlaceResult.getVicinity(),
                                            isOpen,
                                            nearbyPlaceResult.getRating(),
                                            nearbyPlaceResult.getGeometry().getLocation().getLat(),
                                            nearbyPlaceResult.getGeometry().getLocation().getLng(),
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
    public LiveData<RestaurantDetails> getRestaurantDetailsById(@NonNull String placeId) {
        MutableLiveData<RestaurantDetails> restaurantDetailsLiveData = new MutableLiveData<>();

        if (apiService.getPlaceDetailsById(placeId) != null) {
            apiService.getPlaceDetailsById(placeId).enqueue(new Callback<PlaceDetailsResponse>() {
                @Override
                public void onResponse(@NonNull Call<PlaceDetailsResponse> call, @NonNull Response<PlaceDetailsResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getResult() != null) {
                                Result result = response.body().getResult();

                                restaurantDetailsLiveData.setValue(new RestaurantDetails(
                                        result.getPlace_id(),
                                        result.getName(),
                                        result.getInternational_phone_number(),
                                        result.getFormatted_address(),
                                        result.getIcon_mask_base_uri(),
                                        result.getRating(),
                                        result.getWebsite()
                                ));
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PlaceDetailsResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }

        return restaurantDetailsLiveData;
    }

    private String getRestaurantPictureURL(@NonNull String photo_reference) {
        return "https://maps.googleapis.com/" +
                "maps/api/place/photo" +
                "?maxwidth=400" +
                "&key=" + BuildConfig.MAPS_API_KEY +
                "&photo_reference=" + photo_reference;
    }
}