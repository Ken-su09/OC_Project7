package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.google.protobuf.Any;

import java.util.List;
import java.util.Objects;

public class NearbyPlaceResponse {

    @NonNull
    @SerializedName("html_attributions")
    private final List<Any> htmlAttributions;

    @NonNull
    @SerializedName("next_page_token")
    private final String nextPageToken;

    @NonNull
    @SerializedName("results")
    private final List<NearbyPlaceResult> nearbyPlaceResults;

    @NonNull
    @SerializedName("status")
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

    @NonNull
    public List<NearbyPlaceResult> getResults() {
        return nearbyPlaceResults;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NearbyPlaceResponse that = (NearbyPlaceResponse) o;
        return htmlAttributions.equals(that.htmlAttributions) && nextPageToken.equals(that.nextPageToken) && nearbyPlaceResults.equals(that.nearbyPlaceResults) && status.equals(that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(htmlAttributions, nextPageToken, nearbyPlaceResults, status);
    }

    @NonNull
    @Override
    public String toString() {
        return "NearbyPlaceResponse{" +
                "htmlAttributions=" + htmlAttributions +
                ", nextPageToken='" + nextPageToken + '\'' +
                ", nearbyPlaceResults=" + nearbyPlaceResults +
                ", status='" + status + '\'' +
                '}';
    }
}