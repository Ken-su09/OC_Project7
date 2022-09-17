package com.suonk.oc_project7.ui.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.suonk.oc_project7.domain.workmates.add.AddWorkmateToFirestoreUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {

    @NonNull
    private final AddWorkmateToFirestoreUseCase addWorkmateToFirestoreUseCase;

    @Inject
    public AuthViewModel(@NonNull AddWorkmateToFirestoreUseCase addWorkmateToFirestoreUseCase) {
        this.addWorkmateToFirestoreUseCase = addWorkmateToFirestoreUseCase;
    }

    public void addWorkmateToFirestore() {
        addWorkmateToFirestoreUseCase.addWorkmateToFirestore();
    }
}