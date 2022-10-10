
package com.suonk.oc_project7.model.data.place_details;

import androidx.annotation.NonNull;

import com.google.protobuf.Any;

import java.util.List;
import java.util.Objects;

public class PlaceDetailsResponse {

    @NonNull
    private final List<Any> htmlAttributions;

    @NonNull
    private final Result result;

    @NonNull
    private final String status;

    public PlaceDetailsResponse(@NonNull List<Any> htmlAttributions, @NonNull Result result, @NonNull String status) {
        this.htmlAttributions = htmlAttributions;
        this.result = result;
        this.status = status;
    }
    @NonNull
    public Result getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceDetailsResponse that = (PlaceDetailsResponse) o;
        return Objects.equals(htmlAttributions, that.htmlAttributions) && Objects.equals(result, that.result) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(htmlAttributions, result, status);
    }

    @NonNull
    @Override
    public String toString() {
        return "PlaceDetailsResponse{" +
                "htmlAttributions=" + htmlAttributions +
                ", result=" + result +
                ", status='" + status + '\'' +
                '}';
    }
}