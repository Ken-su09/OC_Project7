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

    private final boolean isConnected;

    public WorkmateItemViewState(
            @NonNull String id,
            @NonNull String name,
            @NonNull String pictureUrl,
            boolean isConnected
    ) {
        this.id = id;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.isConnected = isConnected;
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

    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkmateItemViewState workmate = (WorkmateItemViewState) o;
        return isConnected == workmate.isConnected && id.equals(workmate.id) && name.equals(workmate.name) && pictureUrl.equals(workmate.pictureUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pictureUrl, isConnected);
    }

    @Override
    public String toString() {
        return "WorkmateItemViewState{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", isConnected=" + isConnected +
                '}';
    }
}