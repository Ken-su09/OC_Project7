package com.suonk.oc_project7.repositories.notification;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class NotificationRepositoryImpl implements NotificationRepository {

    private static final String NOTIFICATIONS_ENABLED = "NOTIFICATIONS_ENABLED";

    @NonNull
    private final Context context;

    @Inject
    public NotificationRepositoryImpl(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public boolean getNotificationEnabled() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NOTIFICATIONS_ENABLED, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(NOTIFICATIONS_ENABLED, false);
    }

    @Override
    public void setNotificationEnabled(Boolean isEnabled) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NOTIFICATIONS_ENABLED, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(NOTIFICATIONS_ENABLED, isEnabled);
        editor.apply();
    }
}
