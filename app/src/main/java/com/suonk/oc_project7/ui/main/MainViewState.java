package com.suonk.oc_project7.ui.main;

import androidx.annotation.NonNull;

import java.util.Objects;

public class MainViewState {

    @NonNull
    private final String displayName;

    @NonNull
    private final String email;

    @NonNull
    private final String photoUrl;

    public MainViewState(@NonNull String displayName, @NonNull String email, @NonNull String photoUrl) {
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    @NonNull
    public String getDisplayName() {
        return displayName;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainViewState that = (MainViewState) o;
        return displayName.equals(that.displayName) && email.equals(that.email) && photoUrl.equals(that.photoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, email, photoUrl);
    }

    @NonNull
    @Override
    public String toString() {
        return "MainViewState{" +
                "displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
