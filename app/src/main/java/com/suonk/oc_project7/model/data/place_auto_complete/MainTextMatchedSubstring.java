
package com.suonk.oc_project7.model.data.place_auto_complete;

import com.google.gson.annotations.SerializedName;

public class MainTextMatchedSubstring {

    @SerializedName("length")
    private Long mLength;
    @SerializedName("offset")
    private Long mOffset;

    public Long getLength() {
        return mLength;
    }

    public void setLength(Long length) {
        mLength = length;
    }

    public Long getOffset() {
        return mOffset;
    }

    public void setOffset(Long offset) {
        mOffset = offset;
    }

}
