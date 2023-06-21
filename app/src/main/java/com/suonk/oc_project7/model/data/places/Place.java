package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Place {

    @NonNull
    private final String placeId;

    @NonNull
    private final String restaurantName;

    @NonNull
    private final String restaurantAddress;

    @NonNull
    private final Double latitude;

    @NonNull
    private final Double longitude;

    private final boolean isOpen;


    public Place(@NonNull String placeId, @NonNull String restaurantName, @NonNull String restaurantAddress, @NonNull Double latitude, @NonNull Double longitude, Boolean isOpen) {
        this.placeId = placeId;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isOpen = isOpen;
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
    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public boolean isOpen() {
        return isOpen;
    }

    @NonNull
    public Double getLatitude() {
        return latitude;
    }

    @NonNull
    public Double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return isOpen == place.isOpen && placeId.equals(place.placeId) && restaurantName.equals(place.restaurantName) && restaurantAddress.equals(place.restaurantAddress) && latitude.equals(place.latitude) && longitude.equals(place.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, restaurantName, restaurantAddress, latitude, longitude, isOpen);
    }

    @Override
    public String toString() {
        return "Place{" +
                "placeId='" + placeId + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", restaurantAddress='" + restaurantAddress + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", isOpen=" + isOpen +
                '}';
    }
}