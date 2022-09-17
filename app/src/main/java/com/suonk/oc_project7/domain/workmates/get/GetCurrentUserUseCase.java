package com.suonk.oc_project7.domain.workmates.get;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;

import javax.inject.Inject;

public class GetCurrentUserUseCase {

    @NonNull
    private final WorkmatesRepository workmatesRepository;

    @Inject
    public GetCurrentUserUseCase(@NonNull WorkmatesRepository workmatesRepository) {
        this.workmatesRepository = workmatesRepository;
    }

    @NonNull
    public LiveData<Workmate> getCurrentUserByIdLiveData(@NonNull String userId) {
        return workmatesRepository.getCurrentUserByIdLiveData(userId);
    }
}