package com.suonk.oc_project7.repositories.workmates;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suonk.oc_project7.model.data.workmate.Workmate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
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
    public LiveData<List<Workmate>> getAllWorkmatesFromFirestoreLiveData() {
        final MutableLiveData<List<Workmate>> workmatesMutableLiveData = new MutableLiveData<>();

        firebaseFirestore.collection("all_workmates")
                .addSnapshotListener((querySnapshot, error) -> {
                    if (querySnapshot != null) {
                        try {
                            Log.i("getWorkmates", "workmatesMutableLiveData : " + querySnapshot.toObjects(Workmate.class));
                            workmatesMutableLiveData.setValue(querySnapshot.toObjects(Workmate.class));
                        } catch (Exception e) {
                            Log.i("getWorkmates", "" + e);
                        }
                    }
                });

        return workmatesMutableLiveData;
    }

    @NonNull
    @Override
    public LiveData<List<Workmate>> getWorkmatesHaveChosenTodayLiveData() {
        final MutableLiveData<List<Workmate>> workmatesMutableLiveData = new MutableLiveData<>();

        LocalDate dateToday = LocalDate.now();
        int year = dateToday.getYear();
        int month = dateToday.getMonthValue();
        int day = dateToday.getDayOfMonth();

        String today = year + "-" + month + "-" + day;

        Log.i("addWorkmate", "" + today);

        firebaseFirestore.collection("have_chosen_today" + "_" + today)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (querySnapshot != null) {
                        try {
                            Log.i("WorkmatesChosenToday", "workmatesMutableLiveData : " + querySnapshot.toObjects(Workmate.class));
                            workmatesMutableLiveData.setValue(querySnapshot.toObjects(Workmate.class));
                        } catch (Exception e) {
                            Log.i("", "" + e);
                        }
                    }
                });

        return workmatesMutableLiveData;
    }

    @Override
    public void addWorkmateToHaveChosenTodayList(@NonNull FirebaseUser firebaseUser, @NonNull String restaurantId) {
        final String id = firebaseUser.getUid();

        if (firebaseUser.getEmail() != null && firebaseUser.getDisplayName() != null) {
            final Workmate workmateToAdd = new Workmate(
                    id,
                    firebaseUser.getDisplayName(),
                    firebaseUser.getEmail(),
                    firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null,
                    restaurantId
            );

            LocalDate dateToday = LocalDate.now();
            int year = dateToday.getYear();
            int month = dateToday.getMonthValue();
            int day = dateToday.getDayOfMonth();

            String today = year + "-" + month + "-" + day;

            firebaseFirestore.collection("have_chosen_today" + "_" + today)
                    .document(id)
                    .set(workmateToAdd)
                    .addOnSuccessListener(unused -> {

                    })
                    .addOnFailureListener(e -> {

                    });
        }
    }

    @Override
    public void addWorkmateToFirestore(@NonNull FirebaseUser firebaseUser) {
        final String id = firebaseUser.getUid();

        if (firebaseUser.getEmail() != null && firebaseUser.getDisplayName() != null) {
            final Workmate workmateToAdd = new Workmate(
                    id,
                    firebaseUser.getDisplayName(),
                    firebaseUser.getEmail(),
                    firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null,
                    ""
            );
            firebaseFirestore.collection("all_workmates")
                    .document(id)
                    .set(workmateToAdd)
                    .addOnSuccessListener(unused -> {

                    })
                    .addOnFailureListener(e -> {

                    });
        }
    }
}