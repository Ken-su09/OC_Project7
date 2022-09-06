package com.suonk.oc_project7.ui.workmates;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.current_user_search.CurrentUserSearchRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class WorkmatesViewModel extends ViewModel {

    @NonNull
    private final MediatorLiveData<List<WorkmateItemViewState>> viewStatesLiveData = new MediatorLiveData<>();

    @NonNull
    private final FirebaseUser firebaseUser;

    @NonNull
    private final Context context;

    @Inject
    public WorkmatesViewModel(@NonNull WorkmatesRepository workmatesRepository,
                              @NonNull CurrentUserSearchRepository currentUserSearchRepository,
                              @NonNull FirebaseAuth firebaseAuth,
                              @NonNull @ApplicationContext Context context) {
        firebaseUser = firebaseAuth.getCurrentUser();
        this.context = context;

        LiveData<List<Workmate>> allWorkmates = workmatesRepository.getAllWorkmatesFromFirestoreLiveData();
        LiveData<List<Workmate>> workmatesHaveChosen = workmatesRepository.getWorkmatesHaveChosenTodayLiveData();
        LiveData<CharSequence> currentUserSearchLiveData = currentUserSearchRepository.getCurrentUserSearchLiveData();

        viewStatesLiveData.addSource(allWorkmates, workmates ->
                combine(workmates, workmatesHaveChosen.getValue(), currentUserSearchLiveData.getValue()));

        viewStatesLiveData.addSource(workmatesHaveChosen, workmates ->
                combine(allWorkmates.getValue(), workmates, currentUserSearchLiveData.getValue()));

        viewStatesLiveData.addSource(currentUserSearchLiveData, query ->
                combine(allWorkmates.getValue(), workmatesHaveChosen.getValue(), query));
    }

    private void combine(@Nullable List<Workmate> allWorkmates, @Nullable List<Workmate> workmatesHaveChosen,
                         @Nullable CharSequence query) {
        ArrayList<WorkmateItemViewState> workmatesItemViews = new ArrayList<>();
        List<String> ids = new ArrayList<>();

        if (allWorkmates == null || workmatesHaveChosen == null) {
            viewStatesLiveData.setValue(workmatesItemViews);
            return;
        }

        for (Workmate workmateHasChosen : workmatesHaveChosen) {
            if (!firebaseUser.getUid().equals(workmateHasChosen.getId())) {
                CharSequence sentence = context.getString(R.string.has_chosen,
                        workmateHasChosen.getName(), workmateHasChosen.getRestaurantName());

                if (query == null || workmateHasChosen.getRestaurantName().contains(query)) {
                    WorkmateItemViewState workmateItemViewState = new WorkmateItemViewState(
                            workmateHasChosen.getId(),
                            sentence,
                            workmateHasChosen.getPictureUrl(),
                            Color.BLACK,
                            Typeface.NORMAL
                    );
                    workmatesItemViews.add(workmateItemViewState);
                }
                ids.add(workmateHasChosen.getId());
            }
        }

        for (Workmate workmate : allWorkmates) {
            if (!firebaseUser.getUid().equals(workmate.getId()) && !ids.contains(workmate.getId())) {
                if (query == null || workmate.getRestaurantName().contains(query)) {
                    WorkmateItemViewState workmateItemViewState = new WorkmateItemViewState(
                            workmate.getId(),
                            context.getString(R.string.has_not_chosen_yet, workmate.getName()),
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