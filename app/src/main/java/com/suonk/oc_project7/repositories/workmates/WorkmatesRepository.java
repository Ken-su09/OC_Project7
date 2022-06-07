package com.suonk.oc_project7.repositories.workmates;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.suonk.oc_project7.model.data.workmate.Workmate;

import java.util.List;

public interface WorkmatesRepository {

    @NonNull
    LiveData<List<Workmate>> getAllWorkmatesFromFirestore();

    @NonNull
    LiveData<Workmate> getWorkmateByIdFromFirestore(@NonNull String userId);
}
