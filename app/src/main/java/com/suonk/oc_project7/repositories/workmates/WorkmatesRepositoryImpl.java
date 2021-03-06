package com.suonk.oc_project7.repositories.workmates;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suonk.oc_project7.model.data.workmate.Workmate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
                            List<Workmate> list = querySnapshot.toObjects(Workmate.class);
                            Log.i("getWorkmates", "workmatesMutableLiveData : " + list);
                            workmatesMutableLiveData.setValue(list);
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
            List<String> ids = new ArrayList<>();

            final Workmate workmateToAdd = new Workmate(
                    id,
                    firebaseUser.getDisplayName(),
                    firebaseUser.getEmail(),
                    firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null,
                    restaurantId,
                    ids
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
        if (firebaseUser != null) {
            final String id = firebaseUser.getUid();

            if (firebaseUser.getEmail() != null && firebaseUser.getDisplayName() != null) {
                List<String> ids = new ArrayList<>();
                final Workmate workmateToAdd = new Workmate(
                        id,
                        firebaseUser.getDisplayName(),
                        firebaseUser.getEmail(),
                        firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null,
                        "",
                        ids
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

    @Override
    public void likeRestaurant(@NonNull FirebaseUser firebaseUser, List<String> listOfLikes) {
        if (firebaseUser != null) {
            final String id = firebaseUser.getUid();

            if (firebaseUser.getEmail() != null && firebaseUser.getDisplayName() != null) {
                final Workmate workmateToAdd = new Workmate(
                        id,
                        firebaseUser.getDisplayName(),
                        firebaseUser.getEmail(),
                        firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null,
                        "",
                        listOfLikes
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
}