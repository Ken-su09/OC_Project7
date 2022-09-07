
package com.suonk.oc_project7.model.data.place_auto_complete;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class StructuredFormatting {

    @NonNull
    private final String mMainText;
    @NonNull
    private final List<MainTextMatchedSubstring> mMainTextMatchedSubstrings;
    @NonNull
    private final String mSecondaryText;

    public StructuredFormatting(@NonNull String mMainText,
                                @NonNull List<MainTextMatchedSubstring> mMainTextMatchedSubstrings,
                                @NonNull String mSecondaryText) {
        this.mMainText = mMainText;
        this.mMainTextMatchedSubstrings = mMainTextMatchedSubstrings;
        this.mSecondaryText = mSecondaryText;
    }

    public String getMainText() {
        return mMainText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StructuredFormatting that = (StructuredFormatting) o;
        return mMainText.equals(that.mMainText) && mMainTextMatchedSubstrings.equals(that.mMainTextMatchedSubstrings) && mSecondaryText.equals(that.mSecondaryText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mMainText, mMainTextMatchedSubstrings, mSecondaryText);
    }

    @NonNull
    @Override
    public String toString() {
        return "StructuredFormatting{" +
                "mMainText='" + mMainText + '\'' +
                ", mMainTextMatchedSubstrings=" + mMainTextMatchedSubstrings +
                ", mSecondaryText='" + mSecondaryText + '\'' +
                '}';
    }
}