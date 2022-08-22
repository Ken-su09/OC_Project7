
package com.suonk.oc_project7.model.data.place_auto_complete;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class AutocompleteResponse {

    @SerializedName("predictions")
    private  List<Prediction> mPredictions;

    @SerializedName("status")
    private  String mStatus;

    public AutocompleteResponse() {

    }

    public List<Prediction> getPredictions() {
        return mPredictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        mPredictions = predictions;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }
}