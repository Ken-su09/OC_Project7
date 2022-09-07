
package com.suonk.oc_project7.model.data.place_details;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Close {

    @NonNull
    private final Long day;

    @NonNull
    private final String time;

    public Close(@NonNull Long day, @NonNull String time) {
        this.day = day;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Close close = (Close) o;
        return day.equals(close.day) && time.equals(close.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, time);
    }

    @NonNull
    @Override
    public String toString() {
        return "Close{" +
                "day=" + day +
                ", time='" + time + '\'' +
                '}';
    }
}