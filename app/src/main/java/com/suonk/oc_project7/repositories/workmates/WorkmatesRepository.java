package com.suonk.oc_project7.repositories.workmates;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;
import com.suonk.oc_project7.model.data.workmate.Workmate;

import java.util.List;

public interface WorkmatesRepository {

    @NonNull
    LiveData<List<Workmate>> getAllWorkmatesFromFirestoreLiveData();

    @NonNull
    LiveData<List<Workmate>> getWorkmatesHaveChosenTodayLiveData();

    void addWorkmateToHaveChosenTodayList(@NonNull FirebaseUser firebaseUser, @NonNull String restaurantId);

    void addWorkmateToFirestore(@NonNull FirebaseUser firebaseUser);

    void likeRestaurant(@NonNull FirebaseUser firebaseUser, List<String> listOfLikes);
}