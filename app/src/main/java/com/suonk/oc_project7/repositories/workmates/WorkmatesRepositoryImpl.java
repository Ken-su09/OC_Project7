package com.suonk.oc_project7.repositories.workmates;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suonk.oc_project7.model.data.workmate.Workmate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    @Nullable
    @Override
    public Workmate getUserByIdFromFirestore(@NonNull String userId) {
        try {
            LocalDate dateToday = LocalDate.now();
            int year = dateToday.getYear();
            int month = dateToday.getMonthValue();
            int day = dateToday.getDayOfMonth();

            String today = year + "-" + month + "-" + day;

            final DocumentSnapshot documentSnapshot = Tasks.await(
                    firebaseFirestore.collection(HAVE_CHOSEN_TODAY + "_" + today)
                            .document(userId)
                            .get()
            );

            if (documentSnapshot == null) {
                return null;
            }

            return documentSnapshot.toObject(Workmate.class);

        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    @NonNull
    @Override
    public LiveData<Workmate> getCurrentUserLiveData(@NonNull String userId) {
        MutableLiveData<Workmate> currentUserLiveData = new MutableLiveData<>();
        firebaseFirestore.collection(ALL_WORKMATES)
                .document(userId)
                .addSnapshotListener((querySnapshot, error) -> {
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

    @Nullable
    @Override
    public List<Workmate> getWorkmatesThatHaveChosenThisRestaurant(@NonNull String restaurantId) {
        final List<Workmate> workmates = new ArrayList<>();

        LocalDate dateToday = LocalDate.now();
        int year = dateToday.getYear();
        int month = dateToday.getMonthValue();
        int day = dateToday.getDayOfMonth();

        String today = year + "-" + month + "-" + day;

        firebaseFirestore.collection(HAVE_CHOSEN_TODAY + "_" + today)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (querySnapshot != null) {
                        try {
                            workmates.addAll(querySnapshot.toObjects(Workmate.class));
                        } catch (Exception e) {
                            Log.i("", "" + e);
                        }
                    }
                });

        if (!workmates.isEmpty()) {
            for (Iterator<Workmate> iterator = workmates.iterator(); iterator.hasNext(); ) {
                Workmate workmate = iterator.next();
                if (workmate.getRestaurantId().equals(restaurantId)) {
                    iterator.remove();
                    break;
                }
            }
        }

        return workmates;
    }

    @NonNull
    @Override
    public LiveData<List<Workmate>> getAllWorkmatesFromFirestoreLiveData() {
        final MutableLiveData<List<Workmate>> workmatesMutableLiveData = new MutableLiveData<>();

        firebaseFirestore.collection(ALL_WORKMATES)
                .addSnapshotListener((querySnapshot, error) -> {
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

        firebaseFirestore.collection(HAVE_CHOSEN_TODAY + "_" + today)
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

    @Override
    public void addWorkmateToHaveChosenTodayList(@NonNull Workmate currentUser,
                                                 @NonNull String restaurantId,
                                                 @NonNull String restaurantName) {
        final String id = currentUser.getId();

        final Workmate workmateToAdd = new Workmate(
                id,
                currentUser.getName(),
                currentUser.getEmail(),
                currentUser.getPictureUrl(),
                restaurantId,
                restaurantName,
                currentUser.getLikedRestaurants()
        );

        LocalDate dateToday = LocalDate.now();
        int year = dateToday.getYear();
        int month = dateToday.getMonthValue();
        int day = dateToday.getDayOfMonth();

        String today = year + "-" + month + "-" + day;

        firebaseFirestore.collection(HAVE_CHOSEN_TODAY + "_" + today)
                .document(id)
                .set(workmateToAdd)
                .addOnSuccessListener(unused -> {

                })
                .addOnFailureListener(e -> {

                });
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
                    "",
                    "",
                    new ArrayList<>()
            );
            firebaseFirestore.collection(ALL_WORKMATES)
                    .document(id)
                    .set(workmateToAdd)
                    .addOnSuccessListener(unused -> {

                    })
                    .addOnFailureListener(e -> {

                    });
        }
    }
}