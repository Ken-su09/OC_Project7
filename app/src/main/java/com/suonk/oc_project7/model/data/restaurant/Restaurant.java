package com.suonk.oc_project7.model.data.restaurant;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Restaurant {

    @NonNull
    private final String restaurantId;

    @NonNull
    private final String restaurantName;

    @NonNull
    private final String address;

    @NonNull
    private final Boolean isOpen;

    @NonNull
    private final Double rating;

    @NonNull
    private final Double latitude;

    @NonNull
    private final Double longitude;

    @NonNull
    private final String pictureUrl;


    public Restaurant(@NonNull String restaurantId, @NonNull String restaurantName, @NonNull String address,
                      @NonNull Boolean isOpen, @NonNull Double rating, @NonNull Double latitude,
                      @NonNull Double longitude, @NonNull String pictureUrl) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.address = address;
        this.isOpen = isOpen;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pictureUrl = pictureUrl;
    }

    @NonNull
    public String getRestaurantId() {
        return restaurantId;
    }

    @NonNull
    public String getRestaurantName() {
        return restaurantName;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    @NonNull
    public Boolean getOpen() {
        return isOpen;
    }

    @NonNull
    public Double getRating() {
        return rating;
    }

    @NonNull
    public Double getLatitude() {
        return latitude;
    }

    @NonNull
    public Double getLongitude() {
        return longitude;
    }

    @NonNull
    public String getPictureUrl() {
        return pictureUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return restaurantId.equals(that.restaurantId) && restaurantName.equals(that.restaurantName) && address.equals(that.address) && isOpen.equals(that.isOpen) && rating.equals(that.rating) && latitude.equals(that.latitude) && longitude.equals(that.longitude) && pictureUrl.equals(that.pictureUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId, restaurantName, address, isOpen, rating, latitude, longitude, pictureUrl);
    }

    @NonNull
    @Override
    public String toString() {
        return "Restaurant{" +
                "restaurantId='" + restaurantId + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", address='" + address + '\'' +
                ", isOpen=" + isOpen +
                ", rating=" + rating +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", pictureUrl='" + pictureUrl + '\'' +
                '}';
    }
}
