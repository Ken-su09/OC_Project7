package com.suonk.oc_project7.repositories.user;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.suonk.oc_project7.model.data.user.CustomFirebaseUser;

import javax.inject.Inject;

public class UserRepository {

    @NonNull
    private final FirebaseAuth auth;

    @Inject
    public UserRepository(@NonNull FirebaseAuth auth) {
        this.auth = auth;
    }

    public CustomFirebaseUser getCustomFirebaseUser() {
        return new CustomFirebaseUser(
                auth.getCurrentUser().getDisplayName(),
                auth.getCurrentUser().getEmail(),
                auth.getCurrentUser().getPhotoUrl().toString()
        );
    }
}