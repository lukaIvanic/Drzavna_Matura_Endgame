package com.example.drzavnamatura_endgame.MainFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.drzavnamatura_endgame.MainMenuActivity;
import com.example.drzavnamatura_endgame.R;
import com.example.drzavnamatura_endgame.SetReminderActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    Button musicSwitch;
    Button enableReminderButton;
    Button editReminder;
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    boolean reminderEnabled;
    public static boolean reminderOn;
    TextView setTimeTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        musicSwitch = view.findViewById(R.id.music_switch_button);
        enableReminderButton = view.findViewById(R.id.toggleButtonSetReminder);
        editReminder = view.findViewById(R.id.setReminderButton);
        mAuth = FirebaseAuth.getInstance();
        setTimeTextView = view.findViewById(R.id.timeTV);
        sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("com.example.drzavnamatura_endgame", Context.MODE_PRIVATE);
        boolean musicEnabled = sharedPreferences.getBoolean("musicEnabled", true);
        if (musicEnabled) {
            musicSwitch.setText("ON");
        } else if (!musicEnabled) {
            musicSwitch.setText("OFF");
        }

        musicSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicSwitch.getText().toString().equals("ON")) {
                    String OFF = "OFF";
                    musicSwitch.setText(OFF);
                    sharedPreferences.edit().clear().apply();
                    sharedPreferences.edit().putBoolean("musicEnabled", false).apply();
                    MainMenuActivity.mediaPlayer.stop();
                } else if (musicSwitch.getText().toString().equals("OFF")) {
                    String ON = "ON";
                    musicSwitch.setText(ON);
                    sharedPreferences.edit().clear().apply();
                    sharedPreferences.edit().putBoolean("musicEnabled", true).apply();
                    MainMenuActivity.mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.background_music);

                    MainMenuActivity.mediaPlayer.start();
                }
            }
        });


        if (!sharedPreferences.contains("reminderEnabled")) {
            sharedPreferences.edit().putBoolean("reminderEnabled", false).apply();
        } else {
            reminderEnabled = sharedPreferences.getBoolean("reminderEnabled", false);
        }
        if (!sharedPreferences.contains("reminderOn")) {
            sharedPreferences.edit().putBoolean("reminderOn", reminderEnabled).apply();
            reminderOn = sharedPreferences.getBoolean("reminderOn", false);
        } else {
            reminderOn = sharedPreferences.getBoolean("reminderOn", false);
        }
        if (reminderOn) {
            enableReminderButton.setText("UKLJ");
            editReminder.setVisibility(View.VISIBLE);
            setTimeTextView.setVisibility(View.VISIBLE);
            setTimeTextView.setText(sharedPreferences.getString("reminderTime", "No time\n spec."));
        } else {
            enableReminderButton.setText("ISKLJ");
            editReminder.setVisibility(View.INVISIBLE);
            setTimeTextView.setVisibility(View.INVISIBLE);
        }

        enableReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enableReminderButton.getText().toString().equals("UKLJ")) {
                    String OFF = "ISKLJ";
                    enableReminderButton.setText(OFF);
                    editReminder.setVisibility(View.INVISIBLE);
                    setTimeTextView.setVisibility(View.INVISIBLE);
                    reminderOn = false;
                    sharedPreferences.edit().putBoolean("reminderEnabled", false).apply();
                    sharedPreferences.edit().putBoolean("reminderOn", false).apply();
                } else if (enableReminderButton.getText().toString().equals("ISKLJ")) {
                    String ON = "UKLJ";
                    enableReminderButton.setText(ON);
                    editReminder.setVisibility(View.VISIBLE);
                    reminderOn = true;
                    sharedPreferences.edit().putBoolean("reminderEnabled", true).apply();
                    sharedPreferences.edit().putBoolean("reminderOn", reminderOn).apply();
                    setTimeTextView.setVisibility(View.VISIBLE);
                    setTimeTextView.setText(sharedPreferences.getString("reminderTime", "No time specified"));
                }
            }
        });
        editReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SetReminderActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
            }
        });


        super.onViewCreated(view, savedInstanceState);

    }

}
