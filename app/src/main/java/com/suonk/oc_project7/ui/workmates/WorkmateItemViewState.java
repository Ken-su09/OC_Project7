package com.suonk.oc_project7.ui.workmates;

import androidx.annotation.NonNull;

import java.util.Objects;

public class WorkmateItemViewState {

    @NonNull
    private final String id;

    @NonNull
    private final CharSequence sentence;

    @NonNull
    private final String pictureUrl;

    private final int textColor;

    private final int textStyle;

    public WorkmateItemViewState(
            @NonNull String id,
            @NonNull CharSequence sentence,
            @NonNull String pictureUrl,
            int textColor,
            int textStyle
    ) {
        this.id = id;
        this.sentence = sentence;
        this.pictureUrl = pictureUrl;
        this.textColor = textColor;
        this.textStyle = textStyle;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public CharSequence getSentence() {
        return sentence;
    }

    @NonNull
    public String getPictureUrl() {
        return pictureUrl;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getTextStyle() {
        return textStyle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkmateItemViewState that = (WorkmateItemViewState) o;
        return textColor == that.textColor && textStyle == that.textStyle && id.equals(that.id) && sentence.equals(that.sentence) && pictureUrl.equals(that.pictureUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sentence, pictureUrl, textColor, textStyle);
    }

    @NonNull
    @Override
    public String toString() {
        return "WorkmateItemViewState{" +
                "id='" + id + '\'' +
                ", sentence='" + sentence + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", textColor=" + textColor +
                ", textStyle=" + textStyle +
                '}';
    }
}