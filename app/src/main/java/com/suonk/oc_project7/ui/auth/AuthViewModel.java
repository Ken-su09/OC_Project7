package com.suonk.oc_project7.ui.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {

    @NonNull
    private final WorkmatesRepository workmatesRepository;

    @Inject
    public AuthViewModel(@NonNull WorkmatesRepository workmatesRepository) {
        this.workmatesRepository = workmatesRepository;
    }

    public void addWorkmateToFirestore(@NonNull FirebaseUser firebaseUser) {
        workmatesRepository.addWorkmateToFirestore(firebaseUser);
    }
}