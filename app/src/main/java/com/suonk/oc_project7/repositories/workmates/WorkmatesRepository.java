package com.suonk.oc_project7.repositories.workmates;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;
import com.suonk.oc_project7.model.data.workmate.Workmate;

import java.util.List;

public interface WorkmatesRepository {

    @NonNull
    LiveData<List<Workmate>> getAllWorkmatesFromFirestoreLiveData();

    @NonNull
    LiveData<List<Workmate>> getWorkmatesHaveChosenTodayLiveData();

    @Nullable
    Workmate getUserByIdFromFirestore(@NonNull String userId);

    @NonNull
    LiveData<Workmate> getCurrentUserLiveData(@NonNull String userId);

    @Nullable
    List<Workmate> getWorkmatesThatHaveChosenThisRestaurant(@NonNull String restaurantId);

    void addWorkmateToHaveChosenTodayList(@NonNull Workmate currentUser, @NonNull String restaurantId,
                                          @NonNull String restaurantName);

    void addWorkmateToFirestore(@NonNull FirebaseUser firebaseUser);
}