package com.suonk.oc_project7.model.data.workmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class Workmate {

    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @NonNull
    private String pictureUrl;

    private String restaurantId;

    public Workmate() {

    }

    public Workmate(
            @NonNull String id,
            @NonNull String name,
            @NonNull String email,
            @NonNull String pictureUrl,
            String restaurantId
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.pictureUrl = pictureUrl;
        this.restaurantId = restaurantId;
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
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getPictureUrl() {
        return pictureUrl;
    }

    @NonNull
    public String getRestaurantId() {
        return restaurantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workmate workmate = (Workmate) o;
        return id.equals(workmate.id) && name.equals(workmate.name) && email.equals(workmate.email) && pictureUrl.equals(workmate.pictureUrl) && Objects.equals(restaurantId, workmate.restaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, pictureUrl, restaurantId);
    }

    @Override
    public String toString() {
        return "Workmate{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                '}';
    }
}