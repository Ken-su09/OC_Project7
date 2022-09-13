
package com.suonk.oc_project7.model.data.place_auto_complete;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class AutocompleteResponse {

    @NonNull
    private final List<Prediction> mPredictions;

    @NonNull
    private final String mStatus;

    public AutocompleteResponse(@NonNull List<Prediction> mPredictions, @NonNull String mStatus) {
        this.mPredictions = mPredictions;
        this.mStatus = mStatus;
    }

    @NonNull
    public List<Prediction> getPredictions() {
        return mPredictions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutocompleteResponse that = (AutocompleteResponse) o;
        return mPredictions.equals(that.mPredictions) && mStatus.equals(that.mStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mPredictions, mStatus);
    }

    @NonNull
    @Override
    public String toString() {
        return "AutocompleteResponse{" +
                "mPredictions=" + mPredictions +
                ", mStatus='" + mStatus + '\'' +
                '}';
    }
}