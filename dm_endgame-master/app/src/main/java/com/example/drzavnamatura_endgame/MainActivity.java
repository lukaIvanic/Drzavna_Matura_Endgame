package com.example.drzavnamatura_endgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static com.example.drzavnamatura_endgame.MainMenuActivity.isMusicPlaying;
import static com.example.drzavnamatura_endgame.MainMenuActivity.mediaPlayer;
import static com.example.drzavnamatura_endgame.MainMenuActivity.musicEnabled;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    private CollectionReference usersCollectionRef = db.collection("users");

    private TextInputLayout inputLayout_username;
    private TextInputLayout inputLayout_password;
    private TextInputLayout inputLayout_repeatPassword;
    private TextInputLayout inputLayout_email;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;
    private EditText emailEditText;

    private RadioGroup mfRadioGroup;

    private Button registerButton;
    private Button goToLoginButton;
    ConstraintLayout wholeLayoutRegister;

    final String score = "0";
    public static ArrayList<String> allUsernames;
    public static ArrayList<User> allUsers;
    boolean isTakenUsername;
    boolean isUsernameValid;
    boolean autoLoginCheck = false;
    ProgressBar loadingBar;
    boolean isNewUserMale;
    boolean genderChecked = false;
    public static User user;
    SharedPreferences sharedPreferences;

    public static boolean isNewIntent;

    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputLayout_username = findViewById(R.id.username_textInputLayout);
        inputLayout_password = findViewById(R.id.password_textInputLayout);
        inputLayout_repeatPassword = findViewById(R.id.passwordRepeat_textInputLayout);
        inputLayout_email = findViewById(R.id.email_textInputLayout);
        usernameEditText = findViewById(R.id.username_editText);
        passwordEditText = findViewById(R.id.password_editText);
        repeatPasswordEditText = findViewById(R.id.passwordRepeat_editText);
        emailEditText = findViewById(R.id.email_editText);
        registerButton = findViewById(R.id.register_button);
        goToLoginButton = findViewById(R.id.go_to_login_Button);
        loadingBar = findViewById(R.id.progressBar_register);
        wholeLayoutRegister = findViewById(R.id.outsideRelativeLayout);
        mfRadioGroup = findViewById(R.id.radioGroupGender);
        mAuth = FirebaseAuth.getInstance();
        isNewIntent = false;
        sharedPreferences = this.getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        //
        wholeLayoutRegister.setAlpha(0);
        wholeLayoutRegister.setVisibility(View.GONE);
        user = null;

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.background_music);
            mediaPlayer.setLooping(true);
        }

        //sharedPreferences.edit().clear().apply();

        if (!sharedPreferences.contains("musicEnabled")) {
            sharedPreferences.edit().putBoolean("musicEnabled", true).apply();
        }
        musicEnabled = sharedPreferences.getBoolean("musicEnabled", false);
        sharedPreferences.edit().putBoolean("isMusicPlaying", isMusicPlaying).apply();
        if (musicEnabled && !isMusicPlaying) {
            mediaPlayer.start();

        }

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().contains(" ")) {
                    inputLayout_username.setErrorEnabled(true);
                    inputLayout_username.setError("No spaces allowed");
                } else {
                    inputLayout_username.setErrorEnabled(false);
                }
            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() < 6) {
                    inputLayout_password.setErrorEnabled(true);
                    inputLayout_password.setError("Password must be at least 6 characters");
                } else if (s.length() >= 6) {
                    inputLayout_password.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 6) {
                    inputLayout_password.setErrorEnabled(true);
                    inputLayout_password.setError("Password must be at least 6 characters");
                } else if (s.length() >= 6) {
                    inputLayout_password.setErrorEnabled(false);
                }
            }
        });
        repeatPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (String.valueOf(s).equals(passwordEditText.getText().toString())) {
                    inputLayout_repeatPassword.setErrorEnabled(false);
                } else {
                    inputLayout_repeatPassword.setErrorEnabled(true);
                    inputLayout_repeatPassword.setError("Passwords do not match");
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (String.valueOf(s).equals(passwordEditText.getText().toString())) {
                    inputLayout_repeatPassword.setErrorEnabled(false);
                } else {
                    inputLayout_repeatPassword.setErrorEnabled(true);
                    inputLayout_repeatPassword.setError("Passwords do not match");
                }
            }
        });
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() < 5) {
                    inputLayout_email.setErrorEnabled(true);
                    inputLayout_email.setError("Please insert a valid e-mail address");
                } else {
                    inputLayout_email.setErrorEnabled(false);
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        allUsernames = new ArrayList<>();
        allUsers = new ArrayList<>();
        usersCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                for (int i = 0; i < documentSnapshots.size(); i++) {
                    DocumentSnapshot documentSnapshot = documentSnapshots.get(i);
                    User user = documentSnapshot.toObject(User.class);
                    String names = Objects.requireNonNull(documentSnapshot.get("username")).toString();
                    allUsernames.add(names);
                    allUsers.add(user);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                wholeLayoutRegister.animate().alpha(1).setDuration(1000);
                loadingBar.setVisibility(View.INVISIBLE);
            }
        });


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
            }
        };


        mfRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                genderChecked = true;
                int maleId = group.getChildAt(0).getId();
                isNewUserMale = checkedId == maleId;
            }
        });


        goToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableDoubleClick(goToLoginButton, 1000);
                disableDoubleClick(registerButton, 1000);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                mStartActivity(intent);
            }
        });

        bmk();



    }

    private void CreateUser(final String email, final String password, final String username) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        loadingBar.setVisibility(View.VISIBLE);
                        currentUser = mAuth.getCurrentUser();
                        assert currentUser != null;
                        final String userID = currentUser.getUid();

                        user = new User(username, email, userID, score, isNewUserMale, null);

                        usersCollectionRef.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (Objects.requireNonNull(task.getResult()).exists()) {
                                            final String name = task.getResult().getString("username");
                                            mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                @Override
                                                public void onSuccess(AuthResult authResult) {
                                                    Toast.makeText(MainActivity.this, "signed in", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                            currentUser.updateProfile(changeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                                                    loadingBar.setVisibility(View.INVISIBLE);
                                                    mStartActivity(intent);
                                                    finish();
                                                }
                                            });

                                        } else {
                                            registerButton.setEnabled(true);
                                            goToLoginButton.setEnabled(true);
                                            Objects.requireNonNull(task.getException()).printStackTrace();
                                        }
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                registerButton.setEnabled(true);
                                goToLoginButton.setEnabled(true);
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        registerButton.setEnabled(true);
                        goToLoginButton.setEnabled(true);
                        Objects.requireNonNull(task.getException()).printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    registerButton.setEnabled(true);
                    goToLoginButton.setEnabled(true);
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(authStateListener);

        if (currentUser != null) {
            usersCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                    for (int i = 0; i < documentSnapshots.size(); i++) {
                        DocumentSnapshot documentSnapshot = documentSnapshots.get(i);
                        String names = Objects.requireNonNull(documentSnapshot.get("username")).toString();
                        assert currentUser != null;
                        autoLoginCheck = names.equals(currentUser.getDisplayName());

                        if (mAuth.getCurrentUser() != null && autoLoginCheck) {
                            user = documentSnapshot.toObject(User.class);
                            Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                            mStartActivity(intent);
                            finish();
                        } else if (!autoLoginCheck) {
                            //wholeLayoutRegister.animate().alpha(1).setDuration(3000);
                            //loadingBar.setVisibility(View.INVISIBLE);
                            //wholeLayoutRegister.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        } else {
            wholeLayoutRegister.animate().alpha(1).setDuration(1000);
            loadingBar.setVisibility(View.INVISIBLE);
            wholeLayoutRegister.setVisibility(View.VISIBLE);
        }
        super.onStart();
    }

    public boolean isUsernameTaken(final String username) {
        isTakenUsername = false;
        for (int i = 0; i < allUsernames.size(); i++) {
            if (allUsernames.get(i).equals(username)) {
                isTakenUsername = true;
            }
        }
        return isTakenUsername;
    }

    public static void disableDoubleClick(final View view, int waitTimeInMillis) {
        view.setEnabled(false);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, waitTimeInMillis);
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
        isNewIntent = false;
        if (sharedPreferences.getBoolean("musicEnabled", false) && !isMusicPlaying && !mediaPlayer.isPlaying()) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.background_music);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    public void bmk() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                disableDoubleClick(goToLoginButton, 3500);
//                disableDoubleClick(registerButton, 3500);
                registerButton.setEnabled(false);
                goToLoginButton.setEnabled(false);
                String username = usernameEditText.getText().toString();
                if (username.contains(" ")) {
                    isUsernameValid = false;
                } else if (!username.contains(" ")) {
                    isUsernameValid = true;
                }

                String password = passwordEditText.getText().toString();
                String repeatPassword = repeatPasswordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username) && !isUsernameTaken(username) && isUsernameValid && genderChecked) {
                    if (repeatPassword.equals(password)) {
                        CreateUser(email, password, username);
                    } else {
                        registerButton.setEnabled(true);
                        goToLoginButton.setEnabled(true);
                        Toast.makeText(MainActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                } else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
                    Toast.makeText(MainActivity.this, "Field(s) cannot be empty", Toast.LENGTH_SHORT).show();
                    registerButton.setEnabled(true);
                    goToLoginButton.setEnabled(true);
                } else if (isUsernameTaken(username)) {
                    Toast.makeText(MainActivity.this, "Username is taken", Toast.LENGTH_SHORT).show();
                    registerButton.setEnabled(true);
                    goToLoginButton.setEnabled(true);
                } else if (!genderChecked) {
                    Toast.makeText(MainActivity.this, "Odaberi spol", Toast.LENGTH_SHORT).show();
                    registerButton.setEnabled(true);
                    goToLoginButton.setEnabled(true);
                } else {
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    registerButton.setEnabled(true);
                    goToLoginButton.setEnabled(true);
                }
            }

        });

    }

}