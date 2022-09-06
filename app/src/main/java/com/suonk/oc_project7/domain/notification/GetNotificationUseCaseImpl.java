package com.suonk.oc_project7.domain.notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.suonk.oc_project7.model.data.notification.NotificationEntity;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.notification.NotificationRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class GetNotificationUseCaseImpl implements GetNotificationUseCase {

    @NonNull
    private final NotificationRepository notificationRepository;

    @NonNull
    private final FirebaseAuth firebaseAuth;

    @NonNull
    private final WorkmatesRepository workmatesRepository;

    @Inject
    public GetNotificationUseCaseImpl(
            @NonNull FirebaseAuth firebaseAuth,
            @NonNull WorkmatesRepository workmatesRepository,
            @NonNull NotificationRepository notificationRepository) {
        this.firebaseAuth = firebaseAuth;
        this.workmatesRepository = workmatesRepository;
        this.notificationRepository = notificationRepository;
    }

    @Nullable
    @Override
    public NotificationEntity invoke() {
        if (notificationRepository.getNotificationEnabled()) {
            if (firebaseAuth.getCurrentUser() != null) {
                Workmate currentUser = workmatesRepository.getUserByIdFromFirestore(firebaseAuth.getCurrentUser().getUid());

                if (currentUser != null) {
                    final List<Workmate> workmates = workmatesRepository.getWorkmatesThatHaveChosenThisRestaurant(currentUser.getRestaurantId());

                    if (workmates != null) {
                        return new NotificationEntity(
                                currentUser.getRestaurantId(),
                                currentUser.getRestaurantName(),
                                convertListToString(currentUser, workmates)
                        );
                    }
                }
            }
        }

        return null;
    }

    private String convertListToString(@NonNull Workmate currentUser,
                                       @NonNull List<Workmate> workmates) {
        return workmates.stream()
                .filter(user -> !user.getId().equals(currentUser.getId()))
                .map(Workmate::getName)
                .collect(Collectors.joining(", "));
    }
}
