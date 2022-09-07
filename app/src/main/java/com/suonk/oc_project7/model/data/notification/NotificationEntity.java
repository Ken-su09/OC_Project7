package com.suonk.oc_project7.model.data.notification;

import androidx.annotation.NonNull;

import java.util.Objects;

public class NotificationEntity {

    @NonNull
    private final String restaurantId;

    @NonNull
    private final String restaurantName;

    @NonNull
    private final String listOfWorkmates;

    public NotificationEntity(@NonNull String restaurantId, @NonNull String restaurantName,
                              @NonNull String listOfWorkmates
    ) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.listOfWorkmates = listOfWorkmates;
    }

    @NonNull
    public String getRestaurantId() {
        return restaurantId;
    }

    @NonNull
    public String getRestaurantName() {
        return restaurantName;
    }

    @NonNull
    public String getListOfWorkmates() {
        return listOfWorkmates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationEntity that = (NotificationEntity) o;
        return restaurantId.equals(that.restaurantId) && restaurantName.equals(that.restaurantName) && listOfWorkmates.equals(that.listOfWorkmates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId, restaurantName, listOfWorkmates);
    }

    @NonNull
    @Override
    public String toString() {
        return "Notification{" +
                "restaurantId='" + restaurantId + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", listOfWorkmates='" + listOfWorkmates + '\'' +
                '}';
    }
}
