
package com.suonk.oc_project7.model.data.place_details;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.protobuf.Any;

public class PlaceDetailsResponse {

    @Nullable
    @SerializedName("html_attributions")
    @Expose
    private List<Any> htmlAttributions;
    @Expose
    private Result result;
    @Expose
    private String status;

    public PlaceDetailsResponse(){

    }

    public List<Any> getHtmlAttributions() {
        return htmlAttributions;
    }

    public Result getResult() {
        return result;
    }

    public String getStatus() {
        return status;
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

    @Override
    public String toString() {
        return "PlaceDetailsResponse{" +
                "htmlAttributions=" + htmlAttributions +
                ", result=" + result +
                ", status='" + status + '\'' +
                '}';
    }
}