package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Viewport {

    @NonNull
    private final NorthEast northEast;
    @NonNull
    private final SouthWest southWest;

    public Viewport(@NonNull NorthEast northeast,
            @NonNull SouthWest southWest){
        this.northEast = northeast;
        this.southWest = southWest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Viewport viewport = (Viewport) o;
        return northEast.equals(viewport.northEast) && southWest.equals(viewport.southWest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(northEast, southWest);
    }

    @NonNull
    @Override
    public String toString() {
        return "Viewport{" +
                "northEast=" + northEast +
                ", southWest=" + southWest +
                '}';
    }
}
