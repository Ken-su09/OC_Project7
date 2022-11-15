package com.suonk.oc_project7.model.data.workmate;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class Workmate {

    private String id;

    private String name;

    private String email;

    private String pictureUrl;

    private String restaurantId;

    private String restaurantName;

    private List<String> likedRestaurants;

    public Workmate(
            @NonNull String id,
            @NonNull String name,
            @NonNull String email,
            @NonNull String pictureUrl,
            @NonNull String restaurantId,
            @NonNull String restaurantName,
            @NonNull List<String> likedRestaurants
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.pictureUrl = pictureUrl;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.likedRestaurants = likedRestaurants;
    }

    public Workmate() {
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

    @NonNull
    public List<String> getLikedRestaurants() {
        return likedRestaurants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workmate workmate = (Workmate) o;
        return id.equals(workmate.id) && name.equals(workmate.name) && email.equals(workmate.email) && pictureUrl.equals(workmate.pictureUrl) && Objects.equals(restaurantId, workmate.restaurantId) && Objects.equals(restaurantName, workmate.restaurantName) && Objects.equals(likedRestaurants, workmate.likedRestaurants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, pictureUrl, restaurantId, restaurantName, likedRestaurants);
    }

    @NonNull
    @Override
    public String toString() {
        return "Workmate{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", likedRestaurants=" + likedRestaurants +
                '}';
    }
}