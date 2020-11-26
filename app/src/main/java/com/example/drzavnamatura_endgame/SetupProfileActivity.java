package com.example.drzavnamatura_endgame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static com.example.drzavnamatura_endgame.MainActivity.isNewIntent;
import static com.example.drzavnamatura_endgame.MainActivity.user;
import static com.example.drzavnamatura_endgame.MainMenuActivity.isMusicPlaying;
import static com.example.drzavnamatura_endgame.MainMenuActivity.mediaPlayer;

public class SetupProfileActivity extends AppCompatActivity {

    private static final int GALLERY_CODE = 14;
    Button buttonFinish;
    ImageView imageSetProfilna;
    Uri ImageUri;
    boolean izSettings = false;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
        izSettings = false;
        Intent intent = getIntent();
        izSettings = intent.getBooleanExtra("izSettings", false);
        sharedPreferences = this.getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        Log.i("TAG", "onCreate: " + izSettings);

        buttonFinish = findViewById(R.id.buttonFinish);
        imageSetProfilna = findViewById(R.id.imageSetProfilna);
        int male_or_female_pic;
        if(user.isMale()){
            male_or_female_pic = R.drawable.avatar_male;
        }else {
            male_or_female_pic = R.drawable.avatar_female;
        }


        if (MainActivity.user.getProfilePicUrl() != null){
            Picasso.get()
                    .load(user.getProfilePicUrl())
                    .transform(new CropCircleTransformation())
                    .placeholder(male_or_female_pic)
                    .fit()
                    .into(imageSetProfilna);
        }

        imageSetProfilna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartActivity(new Intent(SetupProfileActivity.this, MainMenuActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                ImageUri = data.getData();
                imageSetProfilna.setImageURI(ImageUri);
            }
        }

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
        if (izSettings){
            startActivity(new Intent(SetupProfileActivity.this, SettingsActivity.class));
            finish();
        }else{
            super.onBackPressed();
        }
    }
}
