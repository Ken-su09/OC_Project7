package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.protobuf.Any;

import java.util.List;

public class NearbyPlaceResponse {

    @Nullable
    @SerializedName("html_attributions")
    @Expose
    private final List<Any> htmlAttributions;
    @Nullable
    @SerializedName("next_page_token")
    @Expose
    private final String nextPageToken;
    @Nullable
    @SerializedName("results")
    @Expose
    private final List<NearbyPlaceResult> nearbyPlaceResults;
    @Nullable
    @SerializedName("status")
    @Expose
    private final String status;

    public NearbyPlaceResponse(@NonNull List<Any> htmlAttributions,
                               @NonNull String nextPageToken,
                               @NonNull List<NearbyPlaceResult> nearbyPlaceResults,
                               @NonNull String status) {
        this.htmlAttributions = htmlAttributions;
        this.nextPageToken = nextPageToken;
        this.nearbyPlaceResults = nearbyPlaceResults;
        this.status = status;
    }

    @Nullable
    public List<Any> getHtmlAttributions() {
        return htmlAttributions;
    }

    @Nullable
    public String getNextPageToken() {
        return nextPageToken;
    }

    @Nullable
    public List<NearbyPlaceResult> getResults() {
        return nearbyPlaceResults;
    }

    @Nullable
    public String getStatus() {
        return status;
    }
}