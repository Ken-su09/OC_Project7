package com.suonk.oc_project7.domain.workmates;

import androidx.annotation.NonNull;

import com.suonk.oc_project7.domain.workmates.add.AddWorkmateToFirestoreUseCase;
import com.suonk.oc_project7.domain.workmates.add.AddWorkmateToHaveChosenTodayUseCase;
import com.suonk.oc_project7.domain.workmates.get.GetAllWorkmatesFromFirestoreUseCase;
import com.suonk.oc_project7.domain.workmates.get.GetCurrentUserUseCase;
import com.suonk.oc_project7.domain.workmates.get.GetWorkmatesHaveChosenTodayUseCase;

import javax.inject.Inject;

public class WorkmatesUseCases {

    @NonNull
    private final AddWorkmateToFirestoreUseCase addWorkmateToFirestoreUseCase;

    @NonNull
    private final AddWorkmateToHaveChosenTodayUseCase addWorkmateToHaveChosenTodayUseCase;

    @NonNull
    private final GetAllWorkmatesFromFirestoreUseCase getAllWorkmatesFromFirestoreUseCase;

    @NonNull
    private final GetWorkmatesHaveChosenTodayUseCase getWorkmatesHaveChosenTodayUseCase;

    @NonNull
    private final GetCurrentUserUseCase getCurrentUserUseCase;

    @Inject
    public WorkmatesUseCases(
            @NonNull AddWorkmateToFirestoreUseCase addWorkmateToFirestoreUseCase,
            @NonNull AddWorkmateToHaveChosenTodayUseCase addWorkmateToHaveChosenTodayUseCase,
            @NonNull GetAllWorkmatesFromFirestoreUseCase getAllWorkmatesFromFirestoreUseCase,
            @NonNull GetWorkmatesHaveChosenTodayUseCase getWorkmatesHaveChosenTodayUseCase,
            @NonNull GetCurrentUserUseCase getCurrentUserUseCase
    ) {
        this.addWorkmateToFirestoreUseCase = addWorkmateToFirestoreUseCase;
        this.addWorkmateToHaveChosenTodayUseCase = addWorkmateToHaveChosenTodayUseCase;
        this.getAllWorkmatesFromFirestoreUseCase = getAllWorkmatesFromFirestoreUseCase;
        this.getWorkmatesHaveChosenTodayUseCase = getWorkmatesHaveChosenTodayUseCase;
        this.getCurrentUserUseCase = getCurrentUserUseCase;
    }

    @NonNull
    public AddWorkmateToFirestoreUseCase getAddWorkmateToFirestoreUseCase() {
        return addWorkmateToFirestoreUseCase;
    }

    @NonNull
    public AddWorkmateToHaveChosenTodayUseCase getAddWorkmateToHaveChosenTodayUseCase() {
        return addWorkmateToHaveChosenTodayUseCase;
    }

    @NonNull
    public GetAllWorkmatesFromFirestoreUseCase getGetAllWorkmatesFromFirestoreUseCase() {
        return getAllWorkmatesFromFirestoreUseCase;
    }

    @NonNull
    public GetWorkmatesHaveChosenTodayUseCase getGetWorkmatesHaveChosenTodayUseCase() {
        return getWorkmatesHaveChosenTodayUseCase;
    }

    @NonNull
    public GetCurrentUserUseCase getGetCurrentUserUseCase() {
        return getCurrentUserUseCase;
    }
}
