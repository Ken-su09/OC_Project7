package com.suonk.oc_project7.repositories.workmates;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.suonk.oc_project7.model.data.workmate.Workmate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

public class WorkmatesRepositoryImpl implements WorkmatesRepository {

    @NonNull
    private final FirebaseFirestore firebaseFirestore;

    private static final String ALL_WORKMATES = "all_workmates";
    private static final String HAVE_CHOSEN_TODAY = "have_chosen_today";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PICTURE_URL = "pictureUrl";

    @Inject
    public WorkmatesRepositoryImpl(@NonNull FirebaseFirestore firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;
    }

    @NonNull
    @Override
    public Task<Workmate> getCurrentUserWhoHasChosenTodayFromFirestore(@NonNull String userId) {
        LocalDate dateToday = LocalDate.now();
        int year = dateToday.getYear();
        int month = dateToday.getMonthValue();
        int day = dateToday.getDayOfMonth();

        String today = year + "-" + month + "-" + day;

        final Task<DocumentSnapshot> taskDocumentSnapshot = firebaseFirestore.collection(HAVE_CHOSEN_TODAY + "_" + today).document(userId).get();

        return taskDocumentSnapshot.continueWith(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    return documentSnapshot.toObject(Workmate.class);
                }
            }
            return new Workmate("", "", "", "", "", "", new ArrayList<>());
        });
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
    public Task<List<Workmate>> getAllWorkmatesThatHaveChosenToday() {
        final List<Workmate> workmates = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String today = year + "-" + month + "-" + day;

        final Task<QuerySnapshot> taskQuerySnapshot = firebaseFirestore.collection(HAVE_CHOSEN_TODAY + "_" + today).get();

        return taskQuerySnapshot.continueWith(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    workmates.addAll(querySnapshot.toObjects(Workmate.class));
                }
            }

            return workmates;
        });
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

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

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
    public void removeWorkmateToHaveChosenTodayList(@NonNull String id) {
        LocalDate dateToday = LocalDate.now();
        int year = dateToday.getYear();
        int month = dateToday.getMonthValue();
        int day = dateToday.getDayOfMonth();

        String today = year + "-" + month + "-" + day;
        firebaseFirestore.collection(HAVE_CHOSEN_TODAY + "_" + today).document(id).delete();
    }

    @Override
    public void addWorkmateToFirestore(@NonNull String id, @NonNull Workmate workmateToAdd) {
        if (workmateToAdd.getEmail().isEmpty()) {
            firebaseFirestore.collection(ALL_WORKMATES)
                    .whereEqualTo(NAME, workmateToAdd.getName())
                    .whereEqualTo(PICTURE_URL, workmateToAdd.getPictureUrl())
                    .get()
                    .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        firebaseFirestore.collection(ALL_WORKMATES).document(id).set(workmateToAdd);
                    }
                }
            });
        } else {
            firebaseFirestore.collection(ALL_WORKMATES).whereEqualTo(EMAIL, workmateToAdd.getEmail()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        firebaseFirestore.collection(ALL_WORKMATES).document(id).set(workmateToAdd);
                    }
                }
            });
        }
    }
}