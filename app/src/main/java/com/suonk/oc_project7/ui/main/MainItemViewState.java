package com.suonk.oc_project7.ui.main;

import android.text.SpannableString;

import androidx.annotation.NonNull;

import java.util.Objects;

public class MainItemViewState {

    @NonNull
    private final String placeId;

//    @NonNull
//    private final String restaurantName;

    @NonNull
    private final String address;

    @NonNull
    private final SpannableString textToHighlight;

    public MainItemViewState(@NonNull String placeId, @NonNull String address,
                             @NonNull SpannableString textToHighlight) {
        this.placeId = placeId;
//        this.restaurantName = restaurantName;
        this.address = address;
        this.textToHighlight = textToHighlight;
    }

    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    @NonNull
    public SpannableString getTextToHighlight() {
        return textToHighlight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainItemViewState that = (MainItemViewState) o;
        return placeId.equals(that.placeId) && address.equals(that.address) && textToHighlight.equals(that.textToHighlight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, address, textToHighlight);
    }

    @Override
    public String toString() {
        return "MainItemViewState{" +
                "placeId='" + placeId + '\'' +
                ", address='" + address + '\'' +
                ", textToHighlight=" + textToHighlight +
                '}';
    }
}