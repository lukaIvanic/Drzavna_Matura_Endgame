package com.example.drzavnamatura_endgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

import static com.example.drzavnamatura_endgame.MainActivity.disableDoubleClick;
import static com.example.drzavnamatura_endgame.MainActivity.isNewIntent;
import static com.example.drzavnamatura_endgame.MainActivity.user;
import static com.example.drzavnamatura_endgame.MainMenuActivity.isMusicPlaying;
import static com.example.drzavnamatura_endgame.MainMenuActivity.mediaPlayer;

public class LoginActivity extends AppCompatActivity {
    EditText usernameLoginEditText;
    EditText passwordLoginEditText;
    Button login_button, back_to_register;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private CollectionReference usersCollectionRef = db.collection("users");
    String loginEmail = "";
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameLoginEditText = findViewById(R.id.emailOrName_editText);
        passwordLoginEditText = findViewById(R.id.passwordLogin_editText);
        login_button = findViewById(R.id.login_Button);
        back_to_register = findViewById(R.id.back_to_register_button);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = this.getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBar_login);
        progressBar.setVisibility(View.INVISIBLE);
        user = null;
        back_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableDoubleClick(login_button, 2500);
                final String emailOrUsername = usernameLoginEditText.getText().toString();
                final String password = passwordLoginEditText.getText().toString();
                //username Sign In

                usersCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                        for (int i = 0; i < documentSnapshots.size(); i++) {
                            DocumentSnapshot documentSnapshot = documentSnapshots.get(i);
                            if (usernameLoginEditText.getText().toString().equals(Objects.requireNonNull(documentSnapshot.get("username")).toString())) {
                                loginEmail = Objects.requireNonNull(documentSnapshot.get("email")).toString();
                            }

                        }
                        //Email sign in
                        if (!TextUtils.isEmpty(emailOrUsername) && !TextUtils.isEmpty(password)) {
                            progressBar.setVisibility(View.VISIBLE);
                            if (loginEmail != null && loginEmail.length() > 0) {
                                mAuth.signInWithEmailAndPassword(loginEmail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        if (authResult != null) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            currentUser = authResult.getUser();

                                            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                                            intent.putExtra("username", currentUser.getDisplayName());
                                            intent.putExtra("userId", currentUser.getUid());

                                            mStartActivity(intent);
                                            finish();
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(LoginActivity.this, "E-mail/Username and password do not match", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                mAuth.signInWithEmailAndPassword(emailOrUsername, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        if (authResult != null) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            currentUser = authResult.getUser();

                                            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                                            intent.putExtra("username", currentUser.getDisplayName());
                                            intent.putExtra("userId", currentUser.getUid());

                                            mStartActivity(intent);
                                            finish();
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(LoginActivity.this, "E-mail/Username and password do not match", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } else {
                            Toast.makeText(LoginActivity.this, "Field(s) cannot be empty", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });
    }
    public void mStartActivity(Intent intent){
        isNewIntent = true;
        startActivity(intent);
    }
    @Override
    protected void onPause() {
        if (!isNewIntent && mediaPlayer!= null){
            mediaPlayer.stop();
            sharedPreferences.edit().putBoolean("isMusicPlaying", false).apply();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        isNewIntent = false;

        if (sharedPreferences.getBoolean("musicEnabled", false) && !isMusicPlaying && !mediaPlayer.isPlaying()){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.background_music);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        super.onResume();
    }
    @Override
    public void onBackPressed() {
        isNewIntent = true;
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
