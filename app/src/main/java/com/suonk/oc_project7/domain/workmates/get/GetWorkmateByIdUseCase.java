package com.suonk.oc_project7.domain.workmates.get;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GetWorkmateByIdUseCase {

    @NonNull
    private final WorkmatesRepository workmatesRepository;

    @Inject
    public GetWorkmateByIdUseCase(@NonNull WorkmatesRepository workmatesRepository) {
        this.workmatesRepository = workmatesRepository;
    }

    @NonNull
    public LiveData<Workmate> getWorkmateByIdLiveData(@NonNull String id) {
        return workmatesRepository.getWorkmateByIdLiveData(id);
    }
}