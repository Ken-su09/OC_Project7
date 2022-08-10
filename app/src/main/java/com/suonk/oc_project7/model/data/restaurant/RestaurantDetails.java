package com.suonk.oc_project7.model.data.restaurant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class RestaurantDetails {

    @NonNull
    private final String placeId;

    @NonNull
    private final String restaurantName;

    @NonNull
    private final String phoneNumber;

    @NonNull
    private final String address;

    @Nullable
    private final String image;

    @NonNull
    private final Double rating;

    @NonNull
    private final String websiteLink;

    public RestaurantDetails(@NonNull String placeId,
                             @NonNull String restaurantName,
                             @NonNull String phoneNumber,
                             @NonNull String address,
                             @Nullable String image,
                             @NonNull Double rating,
                             @NonNull String websiteLink) {
        this.placeId = placeId;
        this.restaurantName = restaurantName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.image = image;
        this.rating = rating;
        this.websiteLink = websiteLink;
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
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    @Nullable
    public String getImage() {
        return image;
    }

    @NonNull
    public Double getRating() {
        return rating;
    }

    @NonNull
    public String getWebsiteLink() {
        return websiteLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDetails that = (RestaurantDetails) o;
        return placeId.equals(that.placeId) && restaurantName.equals(that.restaurantName) && phoneNumber.equals(that.phoneNumber) && address.equals(that.address) && image.equals(that.image) && rating.equals(that.rating) && websiteLink.equals(that.websiteLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, restaurantName, phoneNumber, address, image, rating, websiteLink);
    }

    @Override
    public String toString() {
        return "PlaceDetails{" +
                "placeId='" + placeId + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", image='" + image + '\'' +
                ", rating='" + rating + '\'' +
                ", websiteLink='" + websiteLink + '\'' +
                '}';
    }
}