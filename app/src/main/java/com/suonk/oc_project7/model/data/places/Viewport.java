package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Viewport {

    @Nullable
    private final NorthEast northEast;

    @Nullable
    private final SouthWest southWest;

    public Viewport(@Nullable NorthEast northeast,
                    @Nullable SouthWest southWest) {
        this.northEast = northeast;
        this.southWest = southWest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Viewport viewport = (Viewport) o;
        return Objects.equals(northEast, viewport.northEast) && Objects.equals(southWest, viewport.southWest);
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
