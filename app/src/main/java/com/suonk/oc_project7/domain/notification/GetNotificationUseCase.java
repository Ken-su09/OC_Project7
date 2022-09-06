package com.suonk.oc_project7.domain.notification;

import androidx.annotation.Nullable;

import com.suonk.oc_project7.model.data.notification.NotificationEntity;

public interface GetNotificationUseCase {

    @Nullable
    NotificationEntity invoke();
}
