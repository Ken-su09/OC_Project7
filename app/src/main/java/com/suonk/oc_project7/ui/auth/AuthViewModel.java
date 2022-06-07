package com.suonk.oc_project7.ui.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {

    @NonNull
    private final FirebaseAuth auth;

    @NonNull
    private final FirebaseFirestore firebaseFirestore;

    @NonNull
    private final WorkmatesRepository workmatesRepository;

    private MediatorLiveData<Workmate> mediatorLiveData = new MediatorLiveData<>();

    @Inject
    public AuthViewModel(@NonNull FirebaseAuth auth, @NonNull FirebaseFirestore firebaseFirestore,
                         @NonNull WorkmatesRepository workmatesRepository) {
        this.auth = auth;
        this.firebaseFirestore = firebaseFirestore;
        this.workmatesRepository = workmatesRepository;
    }

//    @NonNull
//    @Override
//    public LiveData<> signIn(@NonNull AuthCredential authCredential) {
//
//    }


    private void addUserToFirestore() {
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser == null) {
// Toast
        } else {
        }
    }
}