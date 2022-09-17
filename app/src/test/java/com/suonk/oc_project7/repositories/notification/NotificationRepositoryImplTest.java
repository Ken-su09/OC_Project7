package com.suonk.oc_project7.repositories.notification;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doReturn;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class NotificationRepositoryImplTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final Application application = Mockito.mock(Application.class);
    private final SharedPreferences sharedPreferencesMock = Mockito.mock(SharedPreferences.class);
    private final SharedPreferences.Editor sharedPreferencesEditorMock = Mockito.mock(SharedPreferences.Editor.class);

    private static final Boolean DEFAULT_ENABLED = false;
    private static final String NOTIFICATIONS_ENABLED = "NOTIFICATIONS_ENABLED";

    private NotificationRepository repository;

    @Before
    public void setup() {
        doReturn(sharedPreferencesMock).when(application).getSharedPreferences(NOTIFICATIONS_ENABLED, Context.MODE_PRIVATE);
        doReturn(sharedPreferencesEditorMock).when(sharedPreferencesMock).edit();

        repository = new NotificationRepositoryImpl(application);
    }

    @Test
    public void get_notification_enabled_should_return_false_by_default() {
        // GIVEN
        doReturn(DEFAULT_ENABLED).when(sharedPreferencesMock).getBoolean(NOTIFICATIONS_ENABLED, DEFAULT_ENABLED);

        // WHEN
        boolean isNotificationEnabled = repository.getNotificationEnabled();

        // THEN
        assertFalse(isNotificationEnabled);
    }

    @Test
    public void get_notification_enabled_should_return_true_after_set_notification_true() {
        // GIVEN
        repository.setNotificationEnabled(true);
        doReturn(true).when(sharedPreferencesMock).getBoolean(NOTIFICATIONS_ENABLED, true);

        // WHEN
        boolean isNotificationEnabled = repository.getNotificationEnabled();

        // THEN
        assertFalse(isNotificationEnabled);
    }
}