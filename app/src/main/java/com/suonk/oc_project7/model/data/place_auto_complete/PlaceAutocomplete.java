package com.suonk.oc_project7.model.data.place_auto_complete;

import androidx.annotation.NonNull;

import java.util.Objects;

public class PlaceAutocomplete {

    @NonNull
    private final String placeId;

    @NonNull
    private final String restaurantName;

    @NonNull
    private final String address;

    public PlaceAutocomplete(@NonNull String placeId, @NonNull String restaurantName, @NonNull String address) {
        this.placeId = placeId;
        this.restaurantName = restaurantName;
        this.address = address;
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
    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceAutocomplete that = (PlaceAutocomplete) o;
        return placeId.equals(that.placeId) && restaurantName.equals(that.restaurantName) && address.equals(that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, restaurantName, address);
    }

    @Override
    public String toString() {
        return "PlaceAutocomplete{" +
                "placeId='" + placeId + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}