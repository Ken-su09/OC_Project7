package com.suonk.oc_project7.di;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.work.Configuration;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.suonk.oc_project7.background.NotificationWorker;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class App extends Application implements Configuration.Provider {

    @Inject
    HiltWorkerFactory workerFactory;

    @Override
    public void onCreate() {
        super.onCreate();

        Calendar calendar = Calendar.getInstance();
        calendar.getTimeInMillis();

        final PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                NotificationWorker.class, 1, TimeUnit.DAYS)
                .setInitialDelay(
                        Duration.between(LocalTime.now(), LocalTime.NOON).toMillis(),
                        TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(this).enqueue(periodicWorkRequest);
    }

    @NonNull
    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build();
    }
}