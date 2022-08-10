package com.suonk.oc_project7.ui.workmates;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WorkmatesViewModel extends ViewModel {

    @NonNull
    WorkmatesRepository workmatesRepository;

    @NonNull
    private final MediatorLiveData<List<WorkmateItemViewState>> viewStatesLiveData = new MediatorLiveData<>();

    @NonNull
    private final FirebaseUser firebaseUser;

    @Inject
    public WorkmatesViewModel(@NonNull WorkmatesRepository workmatesRepository,
                              @NonNull FirebaseAuth firebaseAuth) {
        this.workmatesRepository = workmatesRepository;
        firebaseUser = firebaseAuth.getCurrentUser();

        LiveData<List<Workmate>> allWorkmates = workmatesRepository.getAllWorkmatesFromFirestoreLiveData();
        LiveData<List<Workmate>> workmatesHaveChosen = workmatesRepository.getWorkmatesHaveChosenTodayLiveData();

        viewStatesLiveData.addSource(allWorkmates, workmates -> {
            combine(workmates, workmatesHaveChosen.getValue());
        });

        viewStatesLiveData.addSource(workmatesHaveChosen, workmates -> {
            combine(allWorkmates.getValue(), workmates);
        });
    }

    private void combine(@Nullable List<Workmate> allWorkmates, @Nullable List<Workmate> workmatesHaveChosen) {
        List<WorkmateItemViewState> workmatesItemViews = new ArrayList<>();
        List<String> ids = new ArrayList<>();

        if (allWorkmates == null || workmatesHaveChosen == null) {
            viewStatesLiveData.setValue(workmatesItemViews);
            return;
        }

        for (Workmate workmateHasChosen : workmatesHaveChosen) {
            if (!firebaseUser.getUid().equals(workmateHasChosen.getId())) {
                WorkmateItemViewState workmateItemViewState = new WorkmateItemViewState(
                        workmateHasChosen.getId(),
                        workmateHasChosen.getName() + " has decided",
                        workmateHasChosen.getPictureUrl(),
                        Color.BLACK,
                        Typeface.NORMAL
                );
                workmatesItemViews.add(workmateItemViewState);
                ids.add(workmateHasChosen.getId());
            }
        }

        for (Workmate workmate : allWorkmates) {
            if (!firebaseUser.getUid().equals(workmate.getId())) {
                if (!ids.contains(workmate.getId())) {
                    WorkmateItemViewState workmateItemViewState = new WorkmateItemViewState(
                            workmate.getId(),
                            workmate.getName() + " hasn't decided yet",
                            workmate.getPictureUrl(),
                            Color.GRAY,
                            Typeface.ITALIC
                    );
                    workmatesItemViews.add(workmateItemViewState);
                }
            }
        }

        viewStatesLiveData.setValue(workmatesItemViews);
    }

    public LiveData<List<WorkmateItemViewState>> getWorkmatesLiveData() {
        return viewStatesLiveData;
    }
}