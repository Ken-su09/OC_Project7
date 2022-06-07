package com.suonk.oc_project7.ui.workmates;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.suonk.oc_project7.model.data.restaurant.Restaurant;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;
import com.suonk.oc_project7.ui.restaurants.list.RestaurantItemViewState;

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

    @Inject
    public WorkmatesViewModel(@NonNull WorkmatesRepository workmatesRepository) {
        this.workmatesRepository = workmatesRepository;

        viewStatesLiveData.addSource(workmatesRepository.getAllWorkmatesFromFirestore(), this::combine);
    }

    private void combine(@Nullable List<Workmate> workmates) {
        List<WorkmateItemViewState> workmatesItemViews = new ArrayList<>();

        if (workmates != null) {
            for (Workmate workmate : workmates) {
                workmatesItemViews.add(new WorkmateItemViewState(
                        workmate.getId(),
                        workmate.getName(),
                        workmate.getPictureUrl(),
                        workmate.isConnected()
                ));
            }
        }

        viewStatesLiveData.setValue(workmatesItemViews);
    }

    public LiveData<List<WorkmateItemViewState>> getWorkmatesLiveData() {
        return viewStatesLiveData;
    }
}