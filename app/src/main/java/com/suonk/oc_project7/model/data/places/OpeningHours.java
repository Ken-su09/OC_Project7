package com.suonk.oc_project7.model.data.places;

public class OpeningHours {

    private Boolean open_now = false;

    public OpeningHours(Boolean open_now) {
        this.open_now = open_now;
    }

    public Boolean getOpenNow() {
        return open_now;
    }
}