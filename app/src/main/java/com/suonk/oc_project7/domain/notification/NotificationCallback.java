package com.suonk.oc_project7.domain.notification;

import com.suonk.oc_project7.model.data.notification.NotificationEntity;

public interface NotificationCallback {
    void onNotificationRetrieved(NotificationEntity notificationEntity);
    void onFailure(Exception exception);
}
