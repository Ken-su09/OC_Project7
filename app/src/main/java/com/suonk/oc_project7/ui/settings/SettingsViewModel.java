package com.suonk.oc_project7.ui.settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.suonk.oc_project7.repositories.notification.NotificationRepository;
import com.suonk.oc_project7.repositories.user.UserRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final NotificationRepository notificationRepository;

    private final MutableLiveData<SettingsViewState> settingsViewStateLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> notificationEnabledLiveData = new MutableLiveData<>();

    @Inject
    public SettingsViewModel(@NonNull UserRepository userRepository,
                             @NonNull NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;

        notificationEnabledLiveData.setValue(notificationRepository.getNotificationEnabled());
    }

    public LiveData<SettingsViewState> getSettingsViewStateLiveData() {
        if (userRepository.getCustomFirebaseUser() != null) {
            settingsViewStateLiveData.setValue(
                    new SettingsViewState(
                            userRepository.getCustomFirebaseUser().getDisplayName(),
                            userRepository.getCustomFirebaseUser().getEmail(),
                            userRepository.getCustomFirebaseUser().getPhotoUrl()
                    )
            );
        } else {
            settingsViewStateLiveData.setValue(
                    new SettingsViewState(
                            "",
                            "",
                            ""
                    )
            );
        }

        return settingsViewStateLiveData;
    }

    public LiveData<Boolean> getNotificationEnabled() {
        return notificationEnabledLiveData;
    }

    public void setNotificationEnabled(Boolean isEnabled) {
        notificationRepository.setNotificationEnabled(isEnabled);
    }
}