package com.suonk.oc_project7.repositories.workmates;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.suonk.oc_project7.model.data.workmate.Workmate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class WorkmatesRepositoryImpl implements WorkmatesRepository {

    @NonNull
    private final FirebaseFirestore firebaseFirestore;

    private static final String ALL_WORKMATES = "all_workmates";
    private static final String HAVE_CHOSEN_TODAY = "have_chosen_today";

    @Inject
    public WorkmatesRepositoryImpl(@NonNull FirebaseFirestore firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;
    }

    @NonNull
    @Override
    public Workmate getUserByIdFromFirestore(@NonNull String userId) {
        Workmate currentUser = null;

        LocalDate dateToday = LocalDate.now();
        int year = dateToday.getYear();
        int month = dateToday.getMonthValue();
        int day = dateToday.getDayOfMonth();

        String today = year + "-" + month + "-" + day;

        final DocumentSnapshot documentSnapshot = firebaseFirestore.collection(HAVE_CHOSEN_TODAY + "_" + today).document(userId).get().getResult();

        if (documentSnapshot != null) {
            if (documentSnapshot.toObject(Workmate.class) != null) {
                currentUser = documentSnapshot.toObject(Workmate.class);
            }
        }

        return currentUser != null ? currentUser : new Workmate("", "", "", "", "", "", new ArrayList<>());
    }

    @NonNull
    @Override
    public LiveData<Workmate> getWorkmateByIdLiveData(@NonNull String id) {
        MutableLiveData<Workmate> currentUserLiveData = new MutableLiveData<>();
        firebaseFirestore.collection(ALL_WORKMATES).document(id).addSnapshotListener((querySnapshot, error) -> {
            if (querySnapshot != null) {
                try {
                    currentUserLiveData.setValue(querySnapshot.toObject(Workmate.class));
                } catch (Exception e) {
                    Log.i("", "" + e);
                }
            }
        });

        return currentUserLiveData;
    }

    @NonNull
    @Override
    public List<Workmate> getAllWorkmatesThatHaveChosenToday() {
        final List<Workmate> workmates = new ArrayList<>();

        LocalDate dateToday = LocalDate.now();
        int year = dateToday.getYear();
        int month = dateToday.getMonthValue();
        int day = dateToday.getDayOfMonth();

        String today = year + "-" + month + "-" + day;

        final QuerySnapshot querySnapshot = firebaseFirestore.collection(HAVE_CHOSEN_TODAY + "_" + today).get().getResult();

        if (querySnapshot != null) {
            workmates.addAll(querySnapshot.toObjects(Workmate.class));
        }

        return workmates;
    }

    @NonNull
    @Override
    public LiveData<List<Workmate>> getAllWorkmatesFromFirestoreLiveData() {
        final MutableLiveData<List<Workmate>> workmatesMutableLiveData = new MutableLiveData<>();

        firebaseFirestore.collection(ALL_WORKMATES).addSnapshotListener((querySnapshot, error) -> {
            if (querySnapshot != null) {
                try {
                    List<Workmate> list = querySnapshot.toObjects(Workmate.class);
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

        firebaseFirestore.collection(HAVE_CHOSEN_TODAY + "_" + today).addSnapshotListener((querySnapshot, error) -> {
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

    @Override
    public void addWorkmateToHaveChosenTodayList(@NonNull String id, @NonNull Workmate workmateToAdd) {
        LocalDate dateToday = LocalDate.now();
        int year = dateToday.getYear();
        int month = dateToday.getMonthValue();
        int day = dateToday.getDayOfMonth();

        String today = year + "-" + month + "-" + day;
        firebaseFirestore.collection(HAVE_CHOSEN_TODAY + "_" + today).document(id).set(workmateToAdd);
    }

    @Override
    public void removeWorkmateToHaveChosenTodayList(@NonNull String id, @NonNull Workmate workmateToRemove) {
        LocalDate dateToday = LocalDate.now();
        int year = dateToday.getYear();
        int month = dateToday.getMonthValue();
        int day = dateToday.getDayOfMonth();

        String today = year + "-" + month + "-" + day;
        firebaseFirestore.collection(HAVE_CHOSEN_TODAY + "_" + today).document(id).delete();
    }

    @Override
    public void addWorkmateToFirestore(@NonNull String id, @NonNull Workmate workmateToAdd) {
        firebaseFirestore.collection(ALL_WORKMATES).document(id).set(workmateToAdd);
    }
}