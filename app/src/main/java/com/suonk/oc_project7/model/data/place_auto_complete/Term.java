
package com.suonk.oc_project7.model.data.place_auto_complete;

import com.google.gson.annotations.SerializedName;

public class Term {

    @SerializedName("offset")
    private Long mOffset;
    @SerializedName("value")
    private String mValue;

    public Long getOffset() {
        return mOffset;
    }

    public void setOffset(Long offset) {
        mOffset = offset;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }
}