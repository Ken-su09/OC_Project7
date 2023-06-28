
package com.suonk.oc_project7.model.data.place_auto_complete;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class AutocompleteResponse {

    @NonNull
    private final List<Prediction> predictions;

    @NonNull
    private final String status;

    public AutocompleteResponse(@NonNull List<Prediction> predictions, @NonNull String status) {
        this.predictions = predictions;
        this.status = status;
    }

    @NonNull
    public List<Prediction> getPredictions() {
        return predictions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutocompleteResponse that = (AutocompleteResponse) o;
        return predictions.equals(that.predictions) && status.equals(that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(predictions, status);
    }

    @NonNull
    @Override
    public String toString() {
        return "AutocompleteResponse{" +
                "predictions=" + predictions +
                ", status='" + status + '\'' +
                '}';
    }
}