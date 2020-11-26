package com.example.drzavnamatura_endgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drzavnamatura_endgame.GameDirectory.GameActivity2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static com.example.drzavnamatura_endgame.MainActivity.isNewIntent;
import static com.example.drzavnamatura_endgame.MainActivity.user;
import static com.example.drzavnamatura_endgame.MainMenuActivity.isMusicPlaying;
import static com.example.drzavnamatura_endgame.MainMenuActivity.mediaPlayer;

public class ProfileSetupActivity extends AppCompatActivity {

    Button odaberiSlikuButton;
    Button spremiProfilnuButton;
    ImageView profilnaImageView;
    public static final int PICK_IMAGE = 1;
    Uri imageURI;
    SharedPreferences sharedPreferences;
    private StorageReference mStorageRef;
    FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersCollectionRef = db.collection("users");
    int temp;
    boolean izSettings = false;

    String urlPic;

    TextView postotak_textview;
    ProgressBar postotak_progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));
        setContentView(R.layout.activity_profile_setup);

        odaberiSlikuButton = findViewById(R.id.odaberiSLikuButton);
        spremiProfilnuButton = findViewById(R.id.spremiButtonProfilna);
        profilnaImageView = findViewById(R.id.imageViewProfileSetup);
        postotak_progressBar = findViewById(R.id.postotak_progressBar);

        izSettings = false;
        Intent intent = getIntent();
        izSettings = intent.getBooleanExtra("izSettings", false);
        sharedPreferences = this.getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        //
        spremiProfilnuButton.setEnabled(true);
        odaberiSlikuButton.setEnabled(true);
        if (user.isMale()) {
            temp = R.drawable.avatar_male;
            profilnaImageView.setImageResource(R.drawable.avatar_male);
        } else {
            temp = R.drawable.avatar_male;
            profilnaImageView.setImageResource(R.drawable.avatar_female);
        }
        if (user.isMale()) {
            urlPic = "https://firebasestorage.googleapis.com/v0/b/drzavna-matura-endgame.appspot.com/o/slike_profila%2Favatar_male.jpg?alt=media&token=b67fea89-8926-44a0-85c9-64fbef3d4998";
        } else {
            urlPic = "https://firebasestorage.googleapis.com/v0/b/drzavna-matura-endgame.appspot.com/o/slike_profila%2Favatar_female.jpg?alt=media&token=b4e35ba5-364b-4524-826d-6b1b933ef1d9";
        }

        Picasso.get()
                .load(urlPic)
                .transform(new CropCircleTransformation())
                .placeholder(temp)
                .fit()
                .into(profilnaImageView);

        odaberiSlikuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(ProfileSetupActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

            }
        });

        spremiProfilnuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spremiProfilnuButton.setEnabled(false);
                odaberiSlikuButton.setEnabled(false);
                if (imageURI != null) {
                    final StorageReference filePath = mStorageRef.child("slike_profila").child("myimage" + user.getUsername());
                    UploadTask uploadTask = filePath.putFile(imageURI);
                    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            float percent = (float) taskSnapshot.getBytesTransferred() / (float) taskSnapshot.getTotalByteCount();
                            postotak_progressBar.setProgress((int) (percent * 100));
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    urlPic = uri.toString();
                                    updateProfilePic(urlPic);
                                    isNewIntent = true;
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileSetupActivity.this, "Nešto je pošlo po krivu, pokušaj opet.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSetupActivity.this);
                    builder.setTitle("Želiš ostaviti zadanu profilnu?");
                    builder.setMessage("Profilnu uvijek možeš promijeniti u postavkama");
                    builder.setPositiveButton(Html.fromHtml("<font color='#FF0050'>Ok</font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (user.isMale()) {
                                updateProfilePic("https://firebasestorage.googleapis.com/v0/b/drzavna-matura-endgame.appspot.com/o/slike_profila%2Favatar_male.jpg?alt=media&token=b67fea89-8926-44a0-85c9-64fbef3d4998");
                            } else {
                                updateProfilePic("https://firebasestorage.googleapis.com/v0/b/drzavna-matura-endgame.appspot.com/o/slike_profila%2Favatar_female.jpg?alt=media&token=b4e35ba5-364b-4524-826d-6b1b933ef1d9");
                            }
                        }
                    }).setNegativeButton(Html.fromHtml("<font color='#FF0050'>Cancel</font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            spremiProfilnuButton.setEnabled(true);
                            odaberiSlikuButton.setEnabled(true);
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageURI = result.getUri();
                Picasso.get()
                        .load(imageURI)
                        .transform(new CropCircleTransformation())
                        .placeholder(temp)
                        .fit()
                        .into(profilnaImageView);
            }
        }
    }

    public void mStartActivity(Intent intent) {
        isNewIntent = true;
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        if (!isNewIntent && mediaPlayer != null) {
            mediaPlayer.stop();
            sharedPreferences.edit().putBoolean("isMusicPlaying", false).apply();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        spremiProfilnuButton.setEnabled(true);
        odaberiSlikuButton.setEnabled(true);
        isNewIntent = false;
        if (sharedPreferences.getBoolean("musicEnabled", false) && !sharedPreferences.getBoolean("isMusicPlaying", false) && !mediaPlayer.isPlaying()) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.background_music);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (izSettings) {
            mStartActivity(new Intent(ProfileSetupActivity.this, SettingsActivity.class));
            finish();
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }

    }

    public void updateProfilePic(final String newProfilePicURL) {
        try {
            usersCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                        String usernameCurrent = Objects.requireNonNull(documentSnapshot.get("username")).toString();
                        if (usernameCurrent.equals(currentUser.getDisplayName())) {
                            user.setProfilePicUrl(newProfilePicURL);
                            final DocumentReference mDocumentReference = usersCollectionRef.document(documentSnapshot.getId());
                            usersCollectionRef.document(documentSnapshot.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    Map<String, Object> newPic = new HashMap<>();
                                    newPic.put("profilePicUrl", newProfilePicURL);

                                    mDocumentReference.update(newPic).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent intent = new Intent(ProfileSetupActivity.this, MainMenuActivity.class);
                                            mStartActivity(intent);
                                            Toast.makeText(ProfileSetupActivity.this, "Profilna spremljena", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isNewIntent = true;
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setCropShape(CropImageView.CropShape.OVAL)
                            .setAspectRatio(1, 1)
                            .start(ProfileSetupActivity.this);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ProfileSetupActivity.this, "Treba nam dozvola da uzmemo sliku iz mobitela.", Toast.LENGTH_SHORT).show();
                }
            }

        }


    }
}



