package com.example.drzavnamatura_endgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;


import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import static com.example.drzavnamatura_endgame.MainActivity.isNewIntent;
import static com.example.drzavnamatura_endgame.MainMenuActivity.isMusicPlaying;
import static com.example.drzavnamatura_endgame.MainMenuActivity.mediaPlayer;
import static com.example.drzavnamatura_endgame.MainMenuActivity.musicEnabled;
import static com.example.drzavnamatura_endgame.NotificationClass.CHANNEL_1_ID;

public class SetReminderActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    TextView reminderSetNotSet;
    TimePicker timePicker;
    int mHour, mMinutes;
    Date curr;
    private NotificationManagerCompat managerCompat;
    CountDownTimer countDownTimer;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);

        managerCompat = NotificationManagerCompat.from(this);
        reminderSetNotSet = findViewById(R.id.remindersetnotsetTV);
        timePicker = findViewById(R.id.timePicker);

        sharedPreferences = this.getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        String reminderTime = sharedPreferences.getString("reminderTime", "");

        if (reminderTime.isEmpty()) {
            reminderSetNotSet.setText("Reminder Not Set");
        } else {
            reminderSetNotSet.setText("Reminder set for " + reminderTime);
        }

        mHour = timePicker.getHour();
        mMinutes = timePicker.getMinute();
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinutes = minute;
            }
        });




    }

    public void setReminder(View view) {
        sharedPreferences.edit().remove("hour").apply();
        sharedPreferences.edit().remove("minute").apply();
        sharedPreferences.edit().remove("reminderTime").apply();

        sharedPreferences.edit().putInt("hour", mHour).apply();
        sharedPreferences.edit().putInt("minute", mMinutes).apply();
        sharedPreferences.edit().putString("reminderTime", mHour + ":" + mMinutes).apply();

        setAlarm(sharedPreferences.getInt("hour", 0), sharedPreferences.getInt("minute", 0));

        Log.i("time set", "setReminder: " + sharedPreferences.getInt("hour", 0) + sharedPreferences.getInt("minute", 0));

        sharedPreferences.edit().putBoolean("reminderEnabled", true).apply();

        Intent intent = new Intent(SetReminderActivity.this, SettingsActivity.class);
        mStartActivity(intent);
        finish();
    }

    public void setAlarm(int hours, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 1);

        Intent intent = new Intent(SetReminderActivity.this, Notification_reciever.class);
        intent.setAction("MY_NOTIFICATION_MESSAGE");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    protected void onPause() {
        if (!isNewIntent){
            mediaPlayer.stop();
            sharedPreferences.edit().putBoolean("isMusicPlaying", false).apply();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        isNewIntent = false;
        if (sharedPreferences.getBoolean("musicEnabled", false)&&!sharedPreferences.getBoolean("isMusicPlaying", false)&&!mediaPlayer.isPlaying()){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.background_music);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        super.onResume();
    }

    public void mStartActivity(Intent intent){
        isNewIntent = true;
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        isNewIntent = true;
        super.onBackPressed();
    }

}
