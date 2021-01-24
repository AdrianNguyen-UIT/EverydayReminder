package com.example.everydayreminderapp.Utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.everydayreminderapp.R;

public class NotificationHelper extends ContextWrapper {
    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CreateNotificationChannel();
        }
    }

    public static final String REMINDER_CHANNEL_NAME= "Reminder";
    private static final String REMINDER_CHANNEL_ID = "ReminderID";
    private NotificationManager notificationManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(REMINDER_CHANNEL_ID, REMINDER_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.GREEN);
        notificationChannel.enableVibration(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getNotificationManager().createNotificationChannel(notificationChannel);
    }

    public NotificationManager getNotificationManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public NotificationCompat.Builder getNotificationChannel(String title, String message) {
        return new NotificationCompat.Builder(getApplicationContext(), REMINDER_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_message_24);
    }
}
