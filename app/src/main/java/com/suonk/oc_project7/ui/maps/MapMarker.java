package com.suonk.oc_project7.ui.maps;

import androidx.annotation.NonNull;

import java.util.Objects;

public class MapMarker {

    @NonNull
    private final String placeId;
    @NonNull
    private final Double latitude;
    @NonNull
    private final Double longitude;
    @NonNull
    private final String restaurantName;

    private final int markerIcon;

    public MapMarker(@NonNull String placeId, @NonNull Double latitude, @NonNull Double longitude,
                     @NonNull String restaurantName, int markerIcon) {
        this.placeId = placeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.restaurantName = restaurantName;
        this.markerIcon = markerIcon;
    }

    @NonNull
    public String getPlaceId() {
        return placeId;
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
    public String getRestaurantName() {
        return restaurantName;
    }

    public int getMarkerIcon() {
        return markerIcon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapMarker mapMarker = (MapMarker) o;
        return markerIcon == mapMarker.markerIcon && placeId.equals(mapMarker.placeId) && latitude.equals(mapMarker.latitude) && longitude.equals(mapMarker.longitude) && restaurantName.equals(mapMarker.restaurantName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, latitude, longitude, restaurantName, markerIcon);
    }

    @NonNull
    @Override
    public String toString() {
        return "MapMarker{" +
                "placeId='" + placeId + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", restaurantName='" + restaurantName + '\'' +
                ", markerIcon=" + markerIcon +
                '}';
    }
}
