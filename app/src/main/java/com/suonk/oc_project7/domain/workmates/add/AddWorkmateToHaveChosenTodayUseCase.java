package com.suonk.oc_project7.domain.workmates.add;

import androidx.annotation.NonNull;

import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AddWorkmateToHaveChosenTodayUseCase {

    @NonNull
    private final WorkmatesRepository workmatesRepository;

    @Inject
    public AddWorkmateToHaveChosenTodayUseCase(
            @NonNull WorkmatesRepository workmatesRepository
    ) {
        this.workmatesRepository = workmatesRepository;
    }

    public void addWorkmateToHaveChosenTodayList(@NonNull Workmate currentUser,
                                                 @NonNull String restaurantId,
                                                 @NonNull String restaurantName) {
        final String id = currentUser.getId();

        final Workmate workmateToAdd = new Workmate(
                id,
                currentUser.getName(),
                currentUser.getEmail(),
                currentUser.getPictureUrl(),
                restaurantId,
                restaurantName,
                currentUser.getLikedRestaurants()
        );

        workmatesRepository.addWorkmateToHaveChosenTodayList(id, workmateToAdd);
    }
}
