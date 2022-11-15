package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;

import java.util.Objects;

public class OpeningHours {

    private final Boolean open_now;

    public OpeningHours(Boolean open_now) {
        this.open_now = open_now;
    }

    public Boolean getOpenNow() {
        return open_now;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpeningHours that = (OpeningHours) o;
        return Objects.equals(open_now, that.open_now);
    }

    @Override
    public int hashCode() {
        return Objects.hash(open_now);
    }

    @NonNull
    @Override
    public String toString() {
        return "OpeningHours{" +
                "open_now=" + open_now +
                '}';
    }
}