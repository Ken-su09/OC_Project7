package com.suonk.oc_project7.model.data.workmate;

import androidx.annotation.NonNull;

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

    private String restaurantName;

    public Workmate() {

    }

    public Workmate(
            @NonNull String id,
            @NonNull String name,
            @NonNull String email,
            @NonNull String pictureUrl,
            @NonNull String restaurantId,
            @NonNull String restaurantName
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.pictureUrl = pictureUrl;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
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

    public String getRestaurantName() {
        return restaurantName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workmate workmate = (Workmate) o;
        return id.equals(workmate.id) && name.equals(workmate.name) && email.equals(workmate.email) && pictureUrl.equals(workmate.pictureUrl) && Objects.equals(restaurantId, workmate.restaurantId) && Objects.equals(restaurantName, workmate.restaurantName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, pictureUrl, restaurantId, restaurantName);
    }

    @Override
    public String toString() {
        return "Workmate{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                '}';
    }
}