package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;

public class Viewport {

    @NonNull
    private NorthEast northEast;
    @NonNull
    private SouthWest southWest;

    public Viewport(
            @NonNull NorthEast northeast,
            @NonNull SouthWest southWest){
        this.northEast = northeast;
        this.southWest = southWest;
    }

    @NonNull
    public NorthEast getNorthEast() {
        return northEast;
    }

    @NonNull
    public SouthWest getSouthWest() {
        return southWest;
    }
}
