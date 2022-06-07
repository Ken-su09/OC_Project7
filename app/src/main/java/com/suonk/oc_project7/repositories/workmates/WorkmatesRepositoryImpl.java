package com.suonk.oc_project7.repositories.workmates;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.suonk.oc_project7.model.data.workmate.Workmate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class WorkmatesRepositoryImpl implements WorkmatesRepository {

    @NonNull
    private final FirebaseFirestore firebaseFirestore;

    @Inject
    public WorkmatesRepositoryImpl(@NonNull FirebaseFirestore firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;
    }

    @NonNull
    @Override
    public LiveData<List<Workmate>> getAllWorkmatesFromFirestore() {
        final MutableLiveData<List<Workmate>> workmatesMutableLiveData = new MutableLiveData<>();

        Date currentTime = Calendar.getInstance().getTime();

        firebaseFirestore.collection("today_choice").whereEqualTo("date", currentTime)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (querySnapshot != null) {
                        try {
                            workmatesMutableLiveData.setValue(querySnapshot.toObjects(Workmate.class));
                        } catch (Exception e) {
                            Log.i("", "" + e);
                        }
                    }
                });

        return workmatesMutableLiveData;
    }


//    // Put in Auth Repo
//            firebaseFirestore.collection("workmates")
//                    .document(firebaseUser.getUid())
//            .set(new Workmate(
//            firebaseUser.getUid(),
//                            firebaseUser.getDisplayName(),
//                                    firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null,
//            false
//            ));

    @NonNull
    @Override
    public LiveData<Workmate> getWorkmateByIdFromFirestore(@NonNull String userId) {
        final MutableLiveData<Workmate> workmateMutableLiveData = new MutableLiveData<>();

        firebaseFirestore.collection("workmates")
                .document(userId)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (querySnapshot != null) {
                        try {
                            workmateMutableLiveData.setValue(querySnapshot.toObject(Workmate.class));
                        } catch (Exception e) {
                            Log.i("", "" + e);
                        }
                    }
                });

        return workmateMutableLiveData;
    }
}