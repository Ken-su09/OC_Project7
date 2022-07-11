
package com.suonk.oc_project7.model.data.place_details;

import androidx.annotation.Nullable;

import java.util.List;
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

}