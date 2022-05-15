package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;

public class Geometry {

    @NonNull
    private Location location;
    @NonNull
    private Viewport viewport;

    public Geometry(@NonNull Location location,
                    @NonNull Viewport viewport) {
        this.location = location;
        this.viewport = viewport;
    }

    @NonNull
    public Location getLocation() {
        return location;
    }

    @NonNull
    public Viewport getViewport() {
        return viewport;
    }
}