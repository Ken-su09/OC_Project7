package com.suonk.oc_project7.ui.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
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

    public void addUserToFirestore(@NonNull FirebaseUser firebaseUser) {
        final String id = firebaseUser.getUid();

        mediatorLiveData.addSource(getWorkmateFromFirestore(id), workmate -> {
            combine(workmate, firebaseUser);
        });
    }

    private void combine(@Nullable Workmate workmate, FirebaseUser firebaseUser) {
        final String id = firebaseUser.getUid();

        if (workmate == null && firebaseUser.getEmail() != null && firebaseUser.getDisplayName() != null) {
            final Workmate workmateToAdd = new Workmate(
                    id,
                    firebaseUser.getDisplayName(),
                    firebaseUser.getEmail(),
                    firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null,
                    false
            );
            addUser(id, workmate);
        }
    }


    private void addUser(@NonNull String userId, @NonNull Workmate workmate) {
        firebaseFirestore.collection("workmates")
                .document(userId)
                .set(workmate);
    }

    @NonNull
    private LiveData<Workmate> getWorkmateFromFirestore(@NonNull String userId) {
        final MutableLiveData<Workmate> userMutableLiveData = new MutableLiveData<>();

        firebaseFirestore.collection("workmates")
                .document(userId)
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (documentSnapshot != null) {
                        userMutableLiveData.setValue(documentSnapshot.toObject(Workmate.class));
                    }
                });

        return userMutableLiveData;
    }
}