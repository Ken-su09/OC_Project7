package com.suonk.oc_project7.ui.map;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class MapViewState {

    @NonNull
    private final Double latitude;
    @NonNull
    private final Double longitude;
    @NonNull
    private final List<MapMarker> mapMarkers;
    private final int markerIcon;

    public MapViewState(@NonNull Double latitude, @NonNull Double longitude, @NonNull List<MapMarker> mapMarkers, int markerIcon) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.mapMarkers = mapMarkers;
        this.markerIcon = markerIcon;
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
    public List<MapMarker> getMapMarkers() {
        return mapMarkers;
    }

    public int getMarkerIcon() {
        return markerIcon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapViewState that = (MapViewState) o;
        return markerIcon == that.markerIcon && latitude.equals(that.latitude) && longitude.equals(that.longitude) && mapMarkers.equals(that.mapMarkers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, mapMarkers, markerIcon);
    }

    @Override
    public String toString() {
        return "MapsViewState{" + "latitude=" + latitude + ", longitude=" + longitude + ", mapMarkers=" + mapMarkers + ", markerIcon=" + markerIcon + '}';
    }
}
