package com.suonk.oc_project7.ui.chat.list.workmates;

import androidx.annotation.NonNull;

import java.util.Objects;

public class OnlineWorkmatesViewState {

    @NonNull
    private final String name;

    @NonNull
    private final String pictureUrl;

    public OnlineWorkmatesViewState(
            @NonNull String name,
            @NonNull String pictureUrl) {
        this.name = name;
        this.pictureUrl = pictureUrl;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getPictureUrl() {
        return pictureUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnlineWorkmatesViewState that = (OnlineWorkmatesViewState) o;
        return name.equals(that.name) && pictureUrl.equals(that.pictureUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pictureUrl);
    }

    @Override
    public String toString() {
        return "OnlineWorkmatesViewState{" +
                "name='" + name + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                '}';
    }
}