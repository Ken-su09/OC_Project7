
package com.suonk.oc_project7.model.data.place_auto_complete;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Term {

    @NonNull
    private final Long mOffset;

    @NonNull
    private final String mValue;

    public Term(@NonNull Long mOffset,
                @NonNull String mValue) {

        this.mOffset = mOffset;
        this.mValue = mValue;
    }

    @NonNull
    public Long getmOffset() {
        return mOffset;
    }

    public String getValue() {
        return mValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return mOffset.equals(term.mOffset) && mValue.equals(term.mValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mOffset, mValue);
    }

    @Override
    public String toString() {
        return "Term{" +
                "mOffset=" + mOffset +
                ", mValue='" + mValue + '\'' +
                '}';
    }
}