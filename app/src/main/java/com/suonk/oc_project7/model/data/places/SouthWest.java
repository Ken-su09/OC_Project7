package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;

import java.util.Objects;

public class SouthWest {

    @NonNull
    private final Double lat;
    @NonNull
    private final Double lng;

    public SouthWest(
            @NonNull Double lat,
            @NonNull Double lng
    ) {
        this.lat = lat;
        this.lng = lng;
    }

    @NonNull
    public Double getLat() {
        return lat;
    }

    @NonNull
    public Double getLng() {
        return lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SouthWest southWest = (SouthWest) o;
        return lat.equals(southWest.lat) && lng.equals(southWest.lng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lng);
    }

    @NonNull
    @Override
    public String toString() {
        return "SouthWest{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}