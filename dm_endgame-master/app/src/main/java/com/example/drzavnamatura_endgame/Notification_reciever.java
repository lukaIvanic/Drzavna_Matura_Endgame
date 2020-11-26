package com.example.drzavnamatura_endgame;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import static android.content.Context.MODE_PRIVATE;
import static com.example.drzavnamatura_endgame.NotificationClass.CHANNEL_1_ID;

public class Notification_reciever extends BroadcastReceiver {
    public NotificationManagerCompat managerCompat;


    @Override
    public void onReceive(Context context, Intent intent) {


        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        Intent onCLickIntent = new Intent(context, MainActivity.class);
        onCLickIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        managerCompat = NotificationManagerCompat.from(context);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, onCLickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_public)
                .setContentTitle("LESSON REMINDER")
                .setContentText("DONE MISS UR LESSON")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .build();

        if (sharedPreferences.getBoolean("reminderEnabled", true) && !MainMenuActivity.isForeground(context, "com.example.drzavnamatura_endgame")) {
            managerCompat.notify(1, notification);
        }


    }
}