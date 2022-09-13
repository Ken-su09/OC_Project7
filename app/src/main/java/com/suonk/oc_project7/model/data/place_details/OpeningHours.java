package com.suonk.oc_project7.model.data.place_details;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class OpeningHours {

    @NonNull
    private final Boolean open_now;

    @NonNull
    private final List<Period> periods;

    @NonNull
    private final List<String> weekday_text;

    public OpeningHours(@NonNull Boolean open_now,
                        @NonNull List<Period> periods,
                        @NonNull List<String> weekday_text) {
        this.open_now = open_now;
        this.periods = periods;
        this.weekday_text = weekday_text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpeningHours that = (OpeningHours) o;
        return open_now.equals(that.open_now) && periods.equals(that.periods) && weekday_text.equals(that.weekday_text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(open_now, periods, weekday_text);
    }

    @NonNull
    @Override
    public String toString() {
        return "OpeningHours{" +
                "open_now=" + open_now +
                ", periods=" + periods +
                ", weekday_text=" + weekday_text +
                '}';
    }
}