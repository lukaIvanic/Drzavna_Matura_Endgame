package com.example.drzavnamatura_endgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import static android.view.View.VISIBLE;
import static com.example.drzavnamatura_endgame.MainActivity.isNewIntent;
import static com.example.drzavnamatura_endgame.MainMenuActivity.isMusicPlaying;
import static com.example.drzavnamatura_endgame.MainMenuActivity.mediaPlayer;

public class SettingsActivity extends AppCompatActivity {

    Button musicSwitch;
    Button enableReminderButton;
    Button editReminder;
    Button aboutButton;
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    boolean reminderEnabled;
    public static boolean reminderOn;
    TextView setTimeTextView;
    ConstraintLayout constLayout_profile;
    boolean isMusicEnabled;
    Button changeProfilePicButton;
    TextView reminderSetNotSet;
    TimePicker timePicker;
    int mHour, mMinutes;

    private static int MUSIC_BUTTON = 0;
    private static int NOTIFICATION_BUTTON = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));
        setContentView(R.layout.activity_settings);
        aboutButton = findViewById(R.id.button2);
        musicSwitch = findViewById(R.id.music_switch_button);
        enableReminderButton = findViewById(R.id.toggleButtonSetReminder);
        editReminder = findViewById(R.id.setReminderButton);
        mAuth = FirebaseAuth.getInstance();
        setTimeTextView = findViewById(R.id.timeTV);
        constLayout_profile = findViewById(R.id.profileLayout);
        changeProfilePicButton = findViewById(R.id.chgProfilePic);
        sharedPreferences = this.getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        isMusicEnabled = sharedPreferences.getBoolean("musicEnabled", false);
        isNewIntent = false;
        if (isMusicEnabled) {
            button_on(musicSwitch, MUSIC_BUTTON);
        } else if (!isMusicEnabled) {
            button_off(musicSwitch, MUSIC_BUTTON);
        }
        musicSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMusicEnabled) {
                    button_off(musicSwitch, MUSIC_BUTTON);
                    makeSnackbar("Music Off");
                    sharedPreferences.edit().putBoolean("musicEnabled", false).apply();
                    sharedPreferences.edit().putBoolean("isMusicPlaying", false).apply();
                    isMusicPlaying = false;
                    isMusicEnabled = false;
                    mediaPlayer.stop();
                } else if (!isMusicEnabled) {
                    button_on(musicSwitch, MUSIC_BUTTON);
                    makeSnackbar("Music On");
                    sharedPreferences.edit().putBoolean("musicEnabled", true).apply();
                    sharedPreferences.edit().putBoolean("isMusicPlaying", true).apply();
                    isMusicPlaying = true;
                    isMusicEnabled = true;
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.background_music);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
            }
        });

        musicSwitch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, adminActivity.class);
                startActivity(intent);
                return false;
            }
        });


        if (!sharedPreferences.contains("reminderEnabled")) {
            sharedPreferences.edit().putBoolean("reminderEnabled", false).apply();
        }
        reminderEnabled = sharedPreferences.getBoolean("reminderEnabled", false);
        if (reminderEnabled) {
            button_on(enableReminderButton, NOTIFICATION_BUTTON);
            editReminder.setVisibility(VISIBLE);
            setTimeTextView.setVisibility(VISIBLE);
            setTextViewForTime();
        } else {
            button_off(enableReminderButton, NOTIFICATION_BUTTON);
            editReminder.setVisibility(View.INVISIBLE);
            setTimeTextView.setVisibility(View.INVISIBLE);
        }

        enableReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reminderEnabled) {
                    button_off(enableReminderButton, NOTIFICATION_BUTTON);
                    editReminder.setVisibility(View.INVISIBLE);
                    setTimeTextView.setVisibility(View.INVISIBLE);
                    reminderEnabled = false;
                    sharedPreferences.edit().putBoolean("reminderEnabled", false).apply();
                    makeSnackbar("Reminder Disabled");
                } else if (!reminderEnabled) {
                    button_on(enableReminderButton, NOTIFICATION_BUTTON);
                    editReminder.setVisibility(VISIBLE);
                    setTimeTextView.setVisibility(VISIBLE);
                    reminderEnabled = true;
                    sharedPreferences.edit().putBoolean("reminderEnabled", true).apply();
                    setTimeTextView.setText(sharedPreferences.getString("reminderTime", "No time\n spec."));
                    makeSnackbar("Reminder Enabled");
                }
            }
        });

        editReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        mMinutes = minute;
                        setReminder();
                        setTextViewForTime();
                    }
                }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(SettingsActivity.this));
                timePickerDialog.show();
            }
        });

        changeProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ProfileSetupActivity.class);
                intent.putExtra("izSettings", true);
                mStartActivity(intent);
                finish();
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle(Html.fromHtml("<font color='#FF0050'>About</font>"));
                builder.setMessage("Made by Luka and Marin");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public void setReminder() {
        sharedPreferences.edit().remove("hour").apply();
        sharedPreferences.edit().remove("minute").apply();
        sharedPreferences.edit().remove("reminderTime").apply();

        sharedPreferences.edit().putInt("hour", mHour).apply();
        sharedPreferences.edit().putInt("minute", mMinutes).apply();
        sharedPreferences.edit().putString("reminderTime", mHour + ":" + mMinutes).apply();

        setAlarm(sharedPreferences.getInt("hour", 0), sharedPreferences.getInt("minute", 0));

        sharedPreferences.edit().putBoolean("reminderEnabled", true).apply();
    }
    public void setAlarm(int hours, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 1);

        Intent intent = new Intent(getApplicationContext(), Notification_reciever.class);
        intent.setAction("MY_NOTIFICATION_MESSAGE");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void signOut(View view) {
        mAuth.signOut();
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        mStartActivity(intent);
        finish();
    }

    public void makeSnackbar(String text) {
        Snackbar snackbar = Snackbar.make(constLayout_profile, text, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
    @Override
    protected void onPause() {
        if (!isNewIntent){
            mediaPlayer.stop();
            sharedPreferences.edit().putBoolean("isMusicPlaying", false).apply();
            isMusicPlaying = false;
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        isNewIntent = false;
        if (sharedPreferences.getBoolean("musicEnabled", false)&&!sharedPreferences.getBoolean("isMusicPlaying", false)&& !mediaPlayer.isPlaying()){
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

    public void setTextViewForTime(){
        if (sharedPreferences.getInt("minute", 0) < 10) {
            setTimeTextView.setText(sharedPreferences.getInt("hour", 0) + ":0" + sharedPreferences.getInt("minute", 0));
        } else {
            if (sharedPreferences.getString("reminderTime", "").startsWith("0")) {
                setTimeTextView.setText("0" + sharedPreferences.getString("reminderTime", "No time\n spec."));
            } else {
                setTimeTextView.setText(sharedPreferences.getString("reminderTime", "No time\n spec."));
            }
        }
    }



    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    private void button_off(final Button button, int WHICH_BUTTON) {

        button.setBackground(getResources().getDrawable(R.drawable.white_outline));


        button.setText("OFF");
        button.setTextColor(Color.WHITE);
        if(WHICH_BUTTON == MUSIC_BUTTON) {
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_music_white, 0);
        }else{
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_alarm_white, 0);
        }
        button.setPadding(50, 0, 50, 0);
        button.setCompoundDrawablePadding(20);
    }

    private void button_on(final Button button, int WHICH_BUTTON) {

        button.setBackground(getResources().getDrawable(R.drawable.white_button));


        button.setText("ON");
        button.setTextColor(Color.BLACK);
        if(WHICH_BUTTON == MUSIC_BUTTON) {
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_music_black, 0);
        }else{
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_alarm_black, 0);
        }
        button.setPadding(50, 0, 50, 0);
        button.setCompoundDrawablePadding(20);
    }
}

