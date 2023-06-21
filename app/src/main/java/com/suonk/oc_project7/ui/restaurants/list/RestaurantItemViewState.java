package com.suonk.oc_project7.ui.restaurants.list;

import androidx.annotation.NonNull;

import java.util.Objects;

public class RestaurantItemViewState {

    @NonNull
    private final String placeId;

    @NonNull
    private final String restaurantName;

    @NonNull
    private final String address;

    @NonNull
    private final String openDescription;

    @NonNull
    private final String distance;

    @NonNull
    private final float distanceValue;

    @NonNull
    private final String numberOfWorkmates;

    private final int rating;

    @NonNull
    private final String pictureUrl;

    public RestaurantItemViewState(@NonNull String placeId, @NonNull String restaurantName, @NonNull String address, @NonNull String openDescription, @NonNull String distance, @NonNull Float distanceValue, @NonNull String numberOfWorkmates, int rating, @NonNull String pictureUrl) {
        this.placeId = placeId;
        this.restaurantName = restaurantName;
        this.address = address;
        this.openDescription = openDescription;
        this.distance = distance;
        this.distanceValue = distanceValue;
        this.numberOfWorkmates = numberOfWorkmates;
        this.rating = rating;
        this.pictureUrl = pictureUrl;
    }

    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    @NonNull
    public String getRestaurantName() {
        return restaurantName;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    @NonNull
    public String getOpenDescription() {
        return openDescription;
    }

    @NonNull
    public String getDistance() {
        return distance;
    }

    @NonNull
    public float getDistanceValue() {
        return distanceValue;
    }

    @NonNull
    public String getNumberOfWorkmates() {
        return numberOfWorkmates;
    }

    public int getRating() {
        return rating;
    }

    @NonNull
    public String getPictureUrl() {
        return pictureUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantItemViewState that = (RestaurantItemViewState) o;
        return Float.compare(that.distanceValue, distanceValue) == 0 && rating == that.rating && placeId.equals(that.placeId) && restaurantName.equals(that.restaurantName) && address.equals(that.address) && openDescription.equals(that.openDescription) && distance.equals(that.distance) && numberOfWorkmates.equals(that.numberOfWorkmates) && pictureUrl.equals(that.pictureUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, restaurantName, address, openDescription, distance, distanceValue, numberOfWorkmates, rating, pictureUrl);
    }

    @Override
    public String toString() {
        return "RestaurantItemViewState{" + "placeId='" + placeId + '\'' + ", restaurantName='" + restaurantName + '\'' + ", address='" + address + '\'' + ", openDescription='" + openDescription + '\'' + ", distance='" + distance + '\'' + ", distanceValue=" + distanceValue + ", numberOfWorkmates='" + numberOfWorkmates + '\'' + ", rating=" + rating + ", pictureUrl='" + pictureUrl + '\'' + '}';
    }
}