package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;

public class Location {

    @NonNull
    private Double lat = 0.0;
    @NonNull
    private Double lng = 0.0;

    public Location(
            @NonNull Double lat,
            @NonNull Double lng
    ){
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
}
