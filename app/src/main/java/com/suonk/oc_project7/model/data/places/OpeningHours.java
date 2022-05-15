package com.suonk.oc_project7.model.data.places;

public class OpeningHours {

    private Boolean openNow = false;

    public OpeningHours(Boolean openNow) {
        this.openNow = openNow;
    }

    public Boolean getOpenNow() {
        return openNow;
    }
}
