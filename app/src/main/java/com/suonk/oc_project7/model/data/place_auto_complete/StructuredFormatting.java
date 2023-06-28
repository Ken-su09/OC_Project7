
package com.suonk.oc_project7.model.data.place_auto_complete;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class StructuredFormatting {

    @NonNull
    private final String main_text;
    @NonNull
    private final List<MainTextMatchedSubstring> main_text_matched_substrings;
    @NonNull
    private final String secondary_text;

    public StructuredFormatting(@NonNull String main_text,
                                @NonNull List<MainTextMatchedSubstring> main_text_matched_substrings,
                                @NonNull String secondary_text) {
        this.main_text = main_text;
        this.main_text_matched_substrings = main_text_matched_substrings;
        this.secondary_text = secondary_text;
    }

    @NonNull
    public String getMainText() {
        return main_text;
    }

    @NonNull
    public String getSecondaryText() {
        return secondary_text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StructuredFormatting that = (StructuredFormatting) o;
        return main_text.equals(that.main_text) && main_text_matched_substrings.equals(that.main_text_matched_substrings) && secondary_text.equals(that.secondary_text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(main_text, main_text_matched_substrings, secondary_text);
    }

    @NonNull
    @Override
    public String toString() {
        return "StructuredFormatting{" +
                "main_text='" + main_text + '\'' +
                ", main_text_matched_substrings=" + main_text_matched_substrings +
                ", secondary_text='" + secondary_text + '\'' +
                '}';
    }
}