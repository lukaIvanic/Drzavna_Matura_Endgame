package com.example.drzavnamatura_endgame.GameDirectory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.drzavnamatura_endgame.R;

import java.util.Objects;

import static com.example.drzavnamatura_endgame.MainActivity.isNewIntent;
import static com.example.drzavnamatura_endgame.MainMenuActivity.isMusicPlaying;
import static com.example.drzavnamatura_endgame.MainMenuActivity.mediaPlayer;

public class GameActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_game);
        isNewIntent = false;
        sharedPreferences = this.getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
    }

    public void proceedWithGame(View view) {
        Intent intent = new Intent(GameActivity.this, GameActivity2.class);
        mStartActivity(intent);
        finish();
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }

    @Override
    public void onBackPressed() {
        isNewIntent = true;
        super.onBackPressed();
    }
}
