package com.suonk.oc_project7.ui.workmates;

import androidx.annotation.NonNull;

import com.suonk.oc_project7.model.data.workmate.Workmate;

import java.util.Objects;

public class WorkmateItemViewState {

    @NonNull
    private final String id;

    @NonNull
    private final String name;

    @NonNull
    private final String pictureUrl;

    private int textColor;

    private int textStyle;

    public WorkmateItemViewState(
            @NonNull String id,
            @NonNull String name,
            @NonNull String pictureUrl,
            int textColor,
            int textStyle
    ) {
        this.id = id;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.textColor = textColor;
        this.textStyle = textStyle;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
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
        return textColor == that.textColor && textStyle == that.textStyle && id.equals(that.id) && name.equals(that.name) && pictureUrl.equals(that.pictureUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pictureUrl, textColor, textStyle);
    }

    @Override
    public String toString() {
        return "WorkmateItemViewState{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", textColor=" + textColor +
                ", textStyle=" + textStyle +
                '}';
    }
}