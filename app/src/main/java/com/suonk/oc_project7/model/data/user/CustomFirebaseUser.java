package com.suonk.oc_project7.model.data.user;

import androidx.annotation.NonNull;

import java.util.Objects;

public class CustomFirebaseUser {

    @NonNull
    private final String id;

    @NonNull
    private final String displayName;

    @NonNull
    private final String email;

    @NonNull
    private final String photoUrl;

    public CustomFirebaseUser(@NonNull String id,
                              @NonNull String displayName,
                              @NonNull String email,
                              @NonNull String photoUrl) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    @NonNull
    public String getId() {
        return id;
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
        CustomFirebaseUser that = (CustomFirebaseUser) o;
        return id.equals(that.id) && displayName.equals(that.displayName) && email.equals(that.email) && photoUrl.equals(that.photoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, displayName, email, photoUrl);
    }

    @Override
    public String toString() {
        return "CustomFirebaseUser{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}