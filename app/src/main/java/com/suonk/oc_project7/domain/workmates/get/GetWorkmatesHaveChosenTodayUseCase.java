package com.suonk.oc_project7.domain.workmates.get;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GetWorkmatesHaveChosenTodayUseCase {

    @NonNull
    private final WorkmatesRepository workmatesRepository;

    @Inject
    public GetWorkmatesHaveChosenTodayUseCase(@NonNull WorkmatesRepository workmatesRepository) {
        this.workmatesRepository = workmatesRepository;
    }

    @NonNull
    public LiveData<List<Workmate>> getWorkmatesHaveChosenTodayLiveData() {
        return workmatesRepository.getWorkmatesHaveChosenTodayLiveData();
    }
}
