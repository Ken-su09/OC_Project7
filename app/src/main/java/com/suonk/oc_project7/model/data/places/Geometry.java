package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;

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

    @NonNull
    public Viewport getViewport() {
        return viewport;
    }
}