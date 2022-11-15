
package com.suonk.oc_project7.model.data.place_details;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.util.Objects;

public class Period {

    @NonNull
    @Expose
    private final Close close;
    @NonNull
    @Expose
    private final Open open;

    public Period(@NonNull Close close, @NonNull Open open) {
        this.close = close;
        this.open = open;
    }

    @NonNull
    public Close getClose() {
        return close;
    }

    @NonNull
    public Open getOpen() {
        return open;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return close.equals(period.close) && open.equals(period.open);
    }

    @Override
    public int hashCode() {
        return Objects.hash(close, open);
    }

    @NonNull
    @Override
    public String toString() {
        return "Period{" +
                "close=" + close +
                ", open=" + open +
                '}';
    }
}