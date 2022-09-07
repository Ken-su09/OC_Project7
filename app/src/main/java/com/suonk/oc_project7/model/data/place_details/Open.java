
package com.suonk.oc_project7.model.data.place_details;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.util.Objects;

public class Open {

    @NonNull
    @Expose
    private final Long day;

    @NonNull
    @Expose
    private final String time;

    public Open(@NonNull Long day, @NonNull String time) {
        this.day = day;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Open open = (Open) o;
        return day.equals(open.day) && time.equals(open.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, time);
    }

    @NonNull
    @Override
    public String toString() {
        return "Open{" +
                "day=" + day +
                ", time='" + time + '\'' +
                '}';
    }
}