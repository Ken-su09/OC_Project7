package com.suonk.oc_project7.domain.notification;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.model.data.notification.NotificationEntity;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.notification.NotificationRepository;
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
    private final FirebaseAuth firebaseAuth;

    @NonNull
    private final WorkmatesRepository workmatesRepository;

    @NonNull
    private final Application application;

    @Inject
    public GetNotificationUseCase(@NonNull Application application, @NonNull FirebaseAuth firebaseAuth, @NonNull WorkmatesRepository workmatesRepository, @NonNull NotificationRepository notificationRepository) {
        this.application = application;
        this.firebaseAuth = firebaseAuth;
        this.workmatesRepository = workmatesRepository;
        this.notificationRepository = notificationRepository;
    }

    public void invoke(NotificationCallback callback) {
        if (notificationRepository.getNotificationEnabled()) {
            if (firebaseAuth.getCurrentUser() != null) {
                workmatesRepository.getCurrentUserWhoHasChosenTodayFromFirestore(firebaseAuth.getCurrentUser().getUid()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i("GetNotification", "firebaseAuth.getCurrentUser().getDisplayName() : " + firebaseAuth.getCurrentUser().getDisplayName());
                        Log.i("GetNotification", "firebaseAuth.getCurrentUser().getEmail() : " + firebaseAuth.getCurrentUser().getEmail());
                        Log.i("GetNotification", "firebaseAuth.getCurrentUser().getPhoneNumber() : " + firebaseAuth.getCurrentUser().getPhoneNumber());
                        Log.i("GetNotification", "task.getResult() : " + task.getResult());
                        workmatesRepository.getAllWorkmatesThatHaveChosenToday().addOnCompleteListener(secondTask -> {
                            if (secondTask.isSuccessful()) {
                                Log.i("GetNotification", "secondTask.getResult() : " + secondTask.getResult());

                                Workmate currentUser = task.getResult();
                                if (!currentUser.getRestaurantId().isEmpty() && !currentUser.getRestaurantName().isEmpty()) {
                                    final List<Workmate> workmates = secondTask.getResult();

                                    if (!workmates.isEmpty()) {
                                        for (Iterator<Workmate> iterator = workmates.iterator(); iterator.hasNext(); ) {
                                            Workmate workmate = iterator.next();

                                            if (!workmate.getRestaurantId().equals(currentUser.getRestaurantId()) && !workmate.getRestaurantName().equals(currentUser.getRestaurantName())) {
                                                iterator.remove();
                                            }
                                        }
                                    }

                                    String notificationContent = "";

                                    if (workmates.size() == 2) {
                                        notificationContent = application.getString(R.string.is_joining_you, convertListToString(currentUser, workmates));
                                    } else {
                                        notificationContent = application.getString(R.string.are_joining_you, convertListToString(currentUser, workmates));
                                    }

                                    callback.onNotificationRetrieved(new NotificationEntity(currentUser.getRestaurantId(), currentUser.getRestaurantName(), notificationContent));
                                }
                            }
                        });
                    } else {
                        Exception exception = task.getException();
                        if (exception != null) {
                            Log.e("Exception", "" + exception);
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
