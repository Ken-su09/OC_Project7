package com.suonk.oc_project7.domain.notification;

import android.app.Application;

import androidx.annotation.NonNull;

import com.suonk.oc_project7.R;
import com.suonk.oc_project7.model.data.notification.NotificationEntity;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.notification.NotificationRepository;
import com.suonk.oc_project7.repositories.user.UserRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GetNotificationUseCase {

    @NonNull
    private final NotificationRepository notificationRepository;

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final WorkmatesRepository workmatesRepository;

    @NonNull
    private final Application application;

    @Inject
    public GetNotificationUseCase(@NonNull Application application, @NonNull UserRepository userRepository, @NonNull WorkmatesRepository workmatesRepository, @NonNull NotificationRepository notificationRepository) {
        this.application = application;
        this.userRepository = userRepository;
        this.workmatesRepository = workmatesRepository;
        this.notificationRepository = notificationRepository;
    }

    public void invoke(NotificationCallback callback) {
        if (notificationRepository.getNotificationEnabled()) {
            if (userRepository.getCustomFirebaseUser() != null) {
                workmatesRepository.getCurrentUserWhoHasChosenTodayFromFirestore(userRepository.getCustomFirebaseUser().getId()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        workmatesRepository.getAllWorkmatesThatHaveChosenToday().addOnCompleteListener(secondTask -> {
                            if (secondTask.isSuccessful()) {
                                Workmate currentUser = task.getResult();
                                if (currentUser != null) {
                                    if (!currentUser.getRestaurantId().isEmpty() && !currentUser.getRestaurantName().isEmpty()) {
                                        final List<Workmate> workmates = secondTask.getResult();

                                        if (workmates != null) {
                                            if (!workmates.isEmpty()) {
                                                for (Iterator<Workmate> iterator = workmates.iterator(); iterator.hasNext(); ) {
                                                    Workmate workmate = iterator.next();

                                                    if (!workmate.getRestaurantId().equals(currentUser.getRestaurantId()) && !workmate.getRestaurantName().equals(currentUser.getRestaurantName())) {
                                                        iterator.remove();
                                                    } else if (workmate.getId().equals(currentUser.getId())) {
                                                        iterator.remove();
                                                    }
                                                }
                                            }

                                            String notificationContent = "";

                                            if (workmates.size() == 0) {
                                                notificationContent = application.getString(R.string.no_one_is_joining_you);
                                            } else if (workmates.size() == 1) {
                                                notificationContent = application.getString(R.string.is_joining_you, convertListToString(currentUser, workmates));
                                            } else {
                                                notificationContent = application.getString(R.string.are_joining_you, convertListToString(currentUser, workmates));
                                            }

                                            callback.onNotificationRetrieved(new NotificationEntity(currentUser.getRestaurantId(), currentUser.getRestaurantName(), notificationContent));
                                        }
                                    }
                                }
                            } else {
                                Exception exception = secondTask.getException();
                                if (exception != null) {
                                    callback.onFailure(new Exception("Second Task failed"));
                                }
                            }
                        });
                    } else {
                        Exception exception = task.getException();
                        if (exception != null) {
                            callback.onFailure(new Exception("Task failed"));
                        }
                    }
                });
            }
        }

    }

    private String convertListToString(@NonNull Workmate currentUser, @NonNull List<Workmate> workmates) {
        return workmates.stream().filter(user -> !user.getId().equals(currentUser.getId())).map(Workmate::getName).collect(Collectors.joining(", "));
    }
}
