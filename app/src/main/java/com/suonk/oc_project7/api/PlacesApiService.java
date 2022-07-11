package com.suonk.oc_project7.api;

import com.suonk.oc_project7.BuildConfig;
import com.suonk.oc_project7.model.data.place_details.PlaceDetailsResponse;
import com.suonk.oc_project7.model.data.places.NearbyPlaceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApiService {

    @GET("maps/api/place/nearbysearch/json?" +
            "radius=2000" +
            "&type=restaurant" +
            "&key=" + BuildConfig.MAPS_API_KEY)
    Call<NearbyPlaceResponse> getNearbyPlacesResponse(@Query("location") String location);

    @GET("maps/api/place/details/json?" +
            "&key=" + BuildConfig.MAPS_API_KEY)
    Call<PlaceDetailsResponse> getPlaceDetailsById(@Query("place_id") String placeId);
}