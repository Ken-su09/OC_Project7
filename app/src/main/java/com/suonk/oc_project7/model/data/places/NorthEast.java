package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;

import java.util.Objects;

public class NorthEast {

    @NonNull
    private final Double lat;
    @NonNull
    private final Double lng;

    public NorthEast(
            @NonNull Double lat,
            @NonNull Double lng
    ) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NorthEast northEast = (NorthEast) o;
        return lat.equals(northEast.lat) && lng.equals(northEast.lng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lng);
    }

    @NonNull
    @Override
    public String toString() {
        return "NorthEast{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
