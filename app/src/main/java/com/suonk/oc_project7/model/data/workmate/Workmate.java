package com.suonk.oc_project7.model.data.workmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Workmate {

    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String pictureUrl;

    private boolean isConnected;

    public Workmate() {

    }

    public Workmate(
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
        Workmate workmate = (Workmate) o;
        return isConnected == workmate.isConnected && id.equals(workmate.id) && name.equals(workmate.name) && pictureUrl.equals(workmate.pictureUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pictureUrl, isConnected);
    }

    @Override
    public String toString() {
        return "Workmate{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", isConnected=" + isConnected +
                '}';
    }
}