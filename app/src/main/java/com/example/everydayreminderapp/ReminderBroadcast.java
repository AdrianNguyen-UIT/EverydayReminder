package com.example.everydayreminderapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.everydayreminderapp.Model.EventModel;

import java.util.List;

public class ReminderBroadcast extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);

        List<EventModel> nextEventList = EventHandler.GetNextEventList();

        for (int index = 0; index < nextEventList.size(); ++index) {
            String title = nextEventList.get(index).getTitle();
            String des = nextEventList.get(index).getNotifyTime();
            NotificationCompat.Builder builder = notificationHelper.getNotificationChannel(title, des);
            notificationHelper.getNotificationManager().notify(index, builder.build());
        }

        EventHandler.RepeatCurrentEvent();
        listener.onNotify();
    }


    public interface OnNotifyListener {
        public void onNotify();
    }

    private static OnNotifyListener listener;

    public static void AddOnNotifyListener(OnNotifyListener p_listener) {
        listener = p_listener;
    }

    public void RemoveOnNotifyListener() {
        listener = null;
    }
}
