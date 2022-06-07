package com.suonk.oc_project7.ui.restaurants.details;

import androidx.annotation.NonNull;

public class RestaurantDetailsViewState {

    @NonNull
    private final String placeId;

    @NonNull
    private final String restaurantName;

    @NonNull
    private final String address;

    @NonNull
    private final String rating;

    @NonNull
    private final String pictureUrl;

    public RestaurantDetailsViewState(@NonNull String placeId, @NonNull String restaurantName,@NonNull String address, @NonNull String rating, @NonNull String pictureUrl) {
        this.placeId = placeId;
        this.restaurantName = restaurantName;
        this.address = address;
        this.rating = rating;
        this.pictureUrl = pictureUrl;
    }

    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    @NonNull
    public String getRestaurantName() {
        return restaurantName;
    }

    @NonNull
    public String getRating() {
        return rating;
    }

    @NonNull
    public String getPictureUrl() {
        return pictureUrl;
    }

    @NonNull
    public String getAddress() {
        return address;
    }
}
