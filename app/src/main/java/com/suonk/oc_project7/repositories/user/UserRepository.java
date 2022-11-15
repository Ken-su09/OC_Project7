package com.suonk.oc_project7.repositories.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.suonk.oc_project7.model.data.user.CustomFirebaseUser;

import javax.inject.Inject;

public class UserRepository {

    @NonNull
    private final FirebaseAuth auth;

    @Inject
    public UserRepository(@NonNull FirebaseAuth auth) {
        this.auth = auth;
    }

    @Nullable
    public CustomFirebaseUser getCustomFirebaseUser() {
        if (auth.getCurrentUser() != null) {
            return new CustomFirebaseUser(
                    auth.getCurrentUser().getUid(),
                    auth.getCurrentUser().getDisplayName() != null ? auth.getCurrentUser().getDisplayName() : "",
                    auth.getCurrentUser().getEmail() != null ? auth.getCurrentUser().getEmail() : "",
                    auth.getCurrentUser().getPhotoUrl() != null ? auth.getCurrentUser().getPhotoUrl().toString() : ""
            );
        }
        return null;
    }
}