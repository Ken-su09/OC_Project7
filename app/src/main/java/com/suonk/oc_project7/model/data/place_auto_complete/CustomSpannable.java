package com.suonk.oc_project7.model.data.place_auto_complete;

import java.util.Objects;

public class CustomSpannable {

    private final int start;

    private final int end;

    public CustomSpannable(int start,
                           int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomSpannable that = (CustomSpannable) o;
        return start == that.start && end == that.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "CustomSpannable{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
