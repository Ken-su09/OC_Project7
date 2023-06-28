package com.suonk.oc_project7.ui.main;

import androidx.annotation.NonNull;

import java.util.Objects;

public class MainItemViewState {

    @NonNull
    private final String placeId;

    @NonNull
    private final String restaurantName;

    @NonNull
    private final String address;

    private final int start;

    private final int end;

    @NonNull
    private final String pictureUrl;

    public MainItemViewState(@NonNull String placeId, @NonNull String restaurantName, @NonNull String address, int start, int end, @NonNull String pictureUrl) {
        this.placeId = placeId;
        this.restaurantName = restaurantName;
        this.address = address;
        this.start = start;
        this.end = end;
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
    public String getAddress() {
        return address;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @NonNull
    public String getPictureUrl() {
        return pictureUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainItemViewState that = (MainItemViewState) o;
        return start == that.start && end == that.end && placeId.equals(that.placeId) && restaurantName.equals(that.restaurantName) && address.equals(that.address) && pictureUrl.equals(that.pictureUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, restaurantName, address, start, end, pictureUrl);
    }

    @NonNull
    @Override
    public String toString() {
        return "MainItemViewState{" + "placeId='" + placeId + '\'' + ", restaurantName='" + restaurantName + '\'' + ", address='" + address + '\'' + ", start=" + start + ", end=" + end + ", pictureUrl='" + pictureUrl + '\'' + '}';
    }
}