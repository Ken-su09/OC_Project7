package com.suonk.oc_project7.domain.workmates.remove;

import androidx.annotation.NonNull;

import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RemoveWorkmateToHaveChosenTodayUseCase {

    @NonNull
    private final WorkmatesRepository workmatesRepository;

    @Inject
    public RemoveWorkmateToHaveChosenTodayUseCase(@NonNull WorkmatesRepository workmatesRepository) {
        this.workmatesRepository = workmatesRepository;
    }

    public void invoke(@NonNull Workmate currentUser) {
        final String id = currentUser.getId();

        workmatesRepository.removeWorkmateToHaveChosenTodayList(id);
    }
}
