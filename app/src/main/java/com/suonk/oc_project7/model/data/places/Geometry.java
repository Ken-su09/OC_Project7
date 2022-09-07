package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Geometry {

    @NonNull
    private final CurrentLocation location;

    @NonNull
    private final Viewport viewport;

    public Geometry(@NonNull CurrentLocation location,
                    @NonNull Viewport viewport) {
        this.location = location;
        this.viewport = viewport;
    }

    @NonNull
    public CurrentLocation getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Geometry geometry = (Geometry) o;
        return location.equals(geometry.location) && viewport.equals(geometry.viewport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, viewport);
    }

    @NonNull
    @Override
    public String toString() {
        return "Geometry{" +
                "location=" + location +
                ", viewport=" + viewport +
                '}';
    }
}