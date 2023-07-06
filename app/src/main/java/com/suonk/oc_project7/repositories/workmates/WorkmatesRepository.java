package com.suonk.oc_project7.repositories.workmates;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.suonk.oc_project7.model.data.workmate.Workmate;

import java.util.List;

public interface WorkmatesRepository {

    @NonNull
    LiveData<List<Workmate>> getAllWorkmatesFromFirestoreLiveData();

    @NonNull
    LiveData<List<Workmate>> getWorkmatesHaveChosenTodayLiveData();

    @NonNull
    Task<Workmate> getCurrentUserWhoHasChosenTodayFromFirestore(@NonNull String userId);

    @NonNull
    LiveData<Workmate> getWorkmateByIdLiveData(@NonNull String id);

    @NonNull
    Task<List<Workmate>> getAllWorkmatesThatHaveChosenToday();

    void addWorkmateToHaveChosenTodayList(@NonNull String id, @NonNull Workmate workmateToAdd);

    void removeWorkmateToHaveChosenTodayList(@NonNull String id);

    void addWorkmateToFirestore(@NonNull String id, @NonNull Workmate workmateToAdd);
}