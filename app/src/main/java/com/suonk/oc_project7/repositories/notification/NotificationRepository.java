package com.suonk.oc_project7.repositories.notification;

public interface NotificationRepository {

    boolean getNotificationEnabled();

    void setNotificationEnabled(Boolean isEnabled);
}
