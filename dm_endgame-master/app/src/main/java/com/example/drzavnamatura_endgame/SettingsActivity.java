package com.example.drzavnamatura_endgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.drzavnamatura_endgame.MainActivity.isNewIntent;
import static com.example.drzavnamatura_endgame.MainMenuActivity.isMusicPlaying;
import static com.example.drzavnamatura_endgame.MainMenuActivity.mediaPlayer;

public class SettingsActivity extends AppCompatActivity {

    Button musicSwitch;
    Button enableReminderButton;
    Button editReminder;
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    boolean reminderEnabled;
    public static boolean reminderOn;
    TextView setTimeTextView;
    ConstraintLayout constLayout_profile;
    boolean isMusicEnabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        musicSwitch = findViewById(R.id.music_switch_button);
        enableReminderButton = findViewById(R.id.toggleButtonSetReminder);
        editReminder = findViewById(R.id.setReminderButton);
        mAuth = FirebaseAuth.getInstance();
        setTimeTextView = findViewById(R.id.timeTV);
        constLayout_profile = findViewById(R.id.profileLayout);
        sharedPreferences = this.getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        isMusicEnabled = sharedPreferences.getBoolean("musicEnabled", false);
        isNewIntent = false;
        if (isMusicEnabled) {
            musicSwitch.setText("ON");
        } else if (!isMusicEnabled) {
            musicSwitch.setText("OFF");
        }
        musicSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMusicEnabled) {
                    musicSwitch.setText("OFF");
                    makeSnackbar("Music Off");
                    sharedPreferences.edit().putBoolean("musicEnabled", false).apply();
                    sharedPreferences.edit().putBoolean("isMusicPlaying", false).apply();
                    isMusicPlaying = false;
                    isMusicEnabled = false;
                    mediaPlayer.stop();
                } else if (!isMusicEnabled) {
                    musicSwitch.setText("ON");
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
            enableReminderButton.setText("ON");
            editReminder.setVisibility(View.VISIBLE);
            setTimeTextView.setVisibility(View.VISIBLE);
            if (sharedPreferences.getInt("minute", 0) < 10) {
                setTimeTextView.setText(sharedPreferences.getInt("hour", 0) + ":0" + sharedPreferences.getInt("minute", 0));
            } else {
                if (sharedPreferences.getString("reminderTime", "").startsWith("0")) {
                    setTimeTextView.setText("0" + sharedPreferences.getString("reminderTime", "No time\n spec."));
                } else {
                    setTimeTextView.setText(sharedPreferences.getString("reminderTime", "No time\n spec."));
                }

            }
        } else {
            enableReminderButton.setText("OFF");
            editReminder.setVisibility(View.INVISIBLE);
            setTimeTextView.setVisibility(View.INVISIBLE);
        }

        enableReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reminderEnabled) {
                    enableReminderButton.setText("OFF");
                    editReminder.setVisibility(View.INVISIBLE);
                    setTimeTextView.setVisibility(View.INVISIBLE);
                    reminderEnabled = false;
                    sharedPreferences.edit().putBoolean("reminderEnabled", false).apply();
                    makeSnackbar("Reminder Disabled");
                } else if (!reminderEnabled) {
                    enableReminderButton.setText("ON");
                    editReminder.setVisibility(View.VISIBLE);
                    setTimeTextView.setVisibility(View.VISIBLE);
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
                Intent intent = new Intent(getApplicationContext(), SetReminderActivity.class);
                mStartActivity(intent);
                finish();
            }
        });

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
        if (sharedPreferences.getBoolean("musicEnabled", false)&&!isMusicPlaying&&!mediaPlayer.isPlaying()){
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

