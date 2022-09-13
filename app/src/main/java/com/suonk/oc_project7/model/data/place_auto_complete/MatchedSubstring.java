
package com.suonk.oc_project7.model.data.place_auto_complete;

import androidx.annotation.NonNull;

import java.util.Objects;

public class MatchedSubstring {

    @NonNull
    private final Long mLength;

    @NonNull
    private final Long mOffset;

    public MatchedSubstring(@NonNull Long mLength, @NonNull Long mOffset) {
        this.mLength = mLength;
        this.mOffset = mOffset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchedSubstring that = (MatchedSubstring) o;
        return mLength.equals(that.mLength) && mOffset.equals(that.mOffset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mLength, mOffset);
    }

    @NonNull
    @Override
    public String toString() {
        return "MatchedSubstring{" +
                "mLength=" + mLength +
                ", mOffset=" + mOffset +
                '}';
    }
}