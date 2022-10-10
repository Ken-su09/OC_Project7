package com.suonk.oc_project7.background;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.hilt.work.HiltWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.suonk.oc_project7.R;
import com.suonk.oc_project7.domain.notification.GetNotificationUseCase;
import com.suonk.oc_project7.model.data.notification.NotificationEntity;
import com.suonk.oc_project7.ui.restaurants.details.RestaurantDetailsActivity;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;

@HiltWorker
public class NotificationWorker extends Worker {

    @NonNull
    private final GetNotificationUseCase getNotificationUseCase;

    @AssistedInject
    public NotificationWorker(@Assisted @NonNull Context context,
                              @Assisted @NonNull WorkerParameters workerParams,
                              @NonNull GetNotificationUseCase getNotificationUseCase) {
        super(context, workerParams);
        this.getNotificationUseCase = getNotificationUseCase;
    }

    @NonNull
    @Override
    public Result doWork() {
        buildNotificationWorker(getNotificationUseCase.invoke());
        return Result.success();
    }

    private void buildNotificationWorker(@Nullable NotificationEntity notificationEntity) {
        Log.i("buildNotificationWorker", "1 : " + notificationEntity);
        if (notificationEntity != null) {
            final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
            String CHANNEL_ID = "CHANNEL_ID";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(new NotificationChannel(
                        CHANNEL_ID,
                        getApplicationContext().getString(R.string.app_name),
                        NotificationManager.IMPORTANCE_DEFAULT
                ));
            }

            final Intent detailIntent = RestaurantDetailsActivity.navigate(getApplicationContext(), notificationEntity.getRestaurantId());

            final PendingIntent pendingIntent = TaskStackBuilder.create(getApplicationContext())
                    .addNextIntentWithParentStack(detailIntent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationManager.notify(
                    1,
                    new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_restaurant)
                            .setContentTitle(notificationEntity.getRestaurantName())
                            .setContentText(notificationEntity.getListOfWorkmates())
                            .setStyle(new NotificationCompat.BigTextStyle())
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .build()
            );
        }
    }
}
