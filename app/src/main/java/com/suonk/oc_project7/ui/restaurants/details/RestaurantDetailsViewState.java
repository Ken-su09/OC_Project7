package com.suonk.oc_project7.ui.restaurants.details;

import androidx.annotation.NonNull;

import com.suonk.oc_project7.ui.workmates.WorkmateItemViewState;

import java.util.List;
import java.util.Objects;

public class RestaurantDetailsViewState {

    @NonNull
    private final String placeId;

    @NonNull
    private final String restaurantName;

    @NonNull
    private final String address;

    private final int rating;

    @NonNull
    private final String pictureUrl;

    @NonNull
    private final String phoneNumber;

    @NonNull
    private final String websiteLink;

    private final int selectButtonIcon;

    private final int likeButtonText;

    private final List<WorkmateItemViewState> workmatesHaveChosen;

    public RestaurantDetailsViewState(@NonNull String placeId,
                                      @NonNull String restaurantName,
                                      @NonNull String address,
                                      int rating,
                                      @NonNull String pictureUrl,
                                      @NonNull String phoneNumber,
                                      @NonNull String websiteLink,
                                      int selectButtonIcon,
                                      int likeButtonText,
                                      List<WorkmateItemViewState> workmatesHaveChosen) {
        this.placeId = placeId;
        this.restaurantName = restaurantName;
        this.address = address;
        this.rating = rating;
        this.pictureUrl = pictureUrl;
        this.phoneNumber = phoneNumber;
        this.websiteLink = websiteLink;
        this.selectButtonIcon = selectButtonIcon;
        this.likeButtonText = likeButtonText;
        this.workmatesHaveChosen = workmatesHaveChosen;
    }

    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    @NonNull
    public String getRestaurantName() {
        return restaurantName;
    }

    public int getRating() {
        return rating;
    }

    @NonNull
    public String getPictureUrl() {
        return pictureUrl;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @NonNull
    public String getWebsiteLink() {
        return websiteLink;
    }

    public int getSelectButtonIcon() {
        return selectButtonIcon;
    }

    public int getLikeButtonText() {
        return likeButtonText;
    }

    public List<WorkmateItemViewState> getWorkmatesHaveChosen() {
        return workmatesHaveChosen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDetailsViewState that = (RestaurantDetailsViewState) o;
        return rating == that.rating && selectButtonIcon == that.selectButtonIcon && likeButtonText == that.likeButtonText && placeId.equals(that.placeId) && restaurantName.equals(that.restaurantName) && address.equals(that.address) && pictureUrl.equals(that.pictureUrl) && phoneNumber.equals(that.phoneNumber) && websiteLink.equals(that.websiteLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, restaurantName, address, rating, pictureUrl, phoneNumber, websiteLink, selectButtonIcon, likeButtonText);
    }

    @NonNull
    @Override
    public String toString() {
        return "RestaurantDetailsViewState{" +
                "placeId='" + placeId + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", address='" + address + '\'' +
                ", rating=" + rating +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", websiteLink='" + websiteLink + '\'' +
                ", selectButtonIcon=" + selectButtonIcon +
                ", likeButtonText=" + likeButtonText +
                '}';
    }
}