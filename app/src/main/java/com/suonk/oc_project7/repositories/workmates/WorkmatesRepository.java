package com.suonk.oc_project7.repositories.workmates;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.suonk.oc_project7.model.data.workmate.Workmate;

import java.util.List;

public interface WorkmatesRepository {

    @NonNull
    LiveData<List<Workmate>> getAllWorkmatesFromFirestoreLiveData();

    @NonNull
    LiveData<List<Workmate>> getWorkmatesHaveChosenTodayLiveData();

    @NonNull
    Workmate getUserByIdFromFirestore(@NonNull String userId);

    @NonNull
    LiveData<Workmate> getWorkmateByIdLiveData(@NonNull String id);

    @NonNull
    List<Workmate> getAllWorkmatesThatHaveChosenToday();

    void addWorkmateToHaveChosenTodayList(@NonNull String id, @NonNull Workmate workmateToAdd);

    void removeWorkmateToHaveChosenTodayList(@NonNull String id, @NonNull Workmate workmateToRemove);

    void addWorkmateToFirestore(@NonNull String id, @NonNull Workmate workmateToAdd);
}