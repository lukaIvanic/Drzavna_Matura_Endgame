package com.example.drzavnamatura_endgame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SetupProfileActivity extends AppCompatActivity {

    private static final int GALLERY_CODE = 14;
    Button buttonFinish;
    ImageView imageSetProfilna;
    Uri ImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        buttonFinish = findViewById(R.id.buttonFinish);
        imageSetProfilna = findViewById(R.id.imageSetProfilna);

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
                startActivity(new Intent(SetupProfileActivity.this, MainMenuActivity.class));
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
}
