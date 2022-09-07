package com.suonk.oc_project7.ui.settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.suonk.oc_project7.repositories.notification.NotificationRepository;
import com.suonk.oc_project7.repositories.user.UserRepository;
import com.suonk.oc_project7.ui.main.MainViewState;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final NotificationRepository notificationRepository;

    private final MutableLiveData<MainViewState> mainViewStateLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> notificationEnabledLiveData = new MutableLiveData<>();

    @Inject
    public SettingsViewModel(@NonNull UserRepository userRepository,
                             @NonNull NotificationRepository notificationRepository
    ) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;

        notificationEnabledLiveData.setValue(notificationRepository.getNotificationEnabled());
    }

    public LiveData<MainViewState> getMainViewStateLiveData() {
        if (userRepository.getCustomFirebaseUser() != null) {
            mainViewStateLiveData.setValue(new MainViewState(
                    userRepository.getCustomFirebaseUser().getDisplayName(),
                    userRepository.getCustomFirebaseUser().getEmail(),
                    userRepository.getCustomFirebaseUser().getPhotoUrl()
            ));
        } else {
            mainViewStateLiveData.setValue(new MainViewState(
                    "",
                    "",
                    ""
            ));
        }

        return mainViewStateLiveData;
    }

    public LiveData<Boolean> getNotificationEnabled() {
        return notificationEnabledLiveData;
    }

    public void setNotificationEnabled(Boolean isEnabled) {
        notificationRepository.setNotificationEnabled(isEnabled);
    }
}