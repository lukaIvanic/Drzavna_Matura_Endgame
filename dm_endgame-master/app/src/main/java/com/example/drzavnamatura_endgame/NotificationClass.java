package com.example.drzavnamatura_endgame;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


public class NotificationClass extends Application {
    public static final String CHANNEL_1_ID = "Channel1";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationsChannel();
    }

    private void createNotificationsChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This is channel 1");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}


