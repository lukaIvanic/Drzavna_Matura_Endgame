package com.example.drzavnamatura_endgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static com.example.drzavnamatura_endgame.GameCompeteIntro.roomName;
import static com.example.drzavnamatura_endgame.MainActivity.isNewIntent;
import static com.example.drzavnamatura_endgame.MainMenuActivity.isMusicPlaying;
import static com.example.drzavnamatura_endgame.MainMenuActivity.mediaPlayer;

public class GameCompete2 extends AppCompatActivity {
    Button playButton;
    TextView user1TV;
    TextView user2TV;
    TextView messageTV;

    public static User player1;
    public static User player2;
    public static User notCurrentPlayer;
    public static String notCurrentPlayerUsername = "";

    boolean gameLaunch = false;
    boolean privremeno = false;
    boolean backPressed = false;
    boolean isOnPause = false;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference roomsCollectionReference = db.collection("rooms");
    DocumentReference roomDocumentReference;
    DocumentSnapshot roomDocumentSnapshot;
    EventListener<DocumentSnapshot> snapshotEventListener;
    CollectionReference userCollectionReference = db.collection("users");
    SharedPreferences sharedPreferences;

    Button startGame;


    @SuppressLint({"SetTextI18n", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));
        setContentView(R.layout.activity_game_compete2);

        startGame = findViewById(R.id.gameButton1);

        user1TV = findViewById(R.id.playerOneTV);
        user2TV = findViewById(R.id.playerTwoTV);
        playButton = findViewById(R.id.gameButton1);
        messageTV = findViewById(R.id.textView69);

        notCurrentPlayer = new User();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        privremeno = false;
        sharedPreferences = this.getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);

        if (getIntent().getBooleanExtra("fromRandom", false)) {
            messageTV.setText("Waiting for another player");
            user1TV.setVisibility(View.INVISIBLE);
            user2TV.setVisibility(View.INVISIBLE);
            playButton.setVisibility(View.INVISIBLE);
        } else {
            playButton.setVisibility(!Objects.equals(currentUser.getDisplayName(), roomName) ? View.INVISIBLE : View.VISIBLE);
            messageTV.setText(!Objects.equals(currentUser.getDisplayName(), roomName) ? "Waiting for host to start the game" : "Start the game when player 2 joins");
            user1TV.setText(roomName);
        }
        snapshotEventListener = (documentSnapshot, e) -> {
            if (!privremeno && !getIntent().getBooleanExtra("fromRandom", false)) {
                if (documentSnapshot != null && !backPressed) {
                    try {
                        String player2Username = documentSnapshot.get("Player2").toString();
                        user2TV.setText(player2Username);
                        getPlayers1and2(roomName, player2Username);
                        if (!isOnPause && !Objects.equals(currentUser.getDisplayName(), user2TV.getText().toString()) && !Objects.equals(currentUser.getDisplayName(), roomName)) {
                            mStartActivity(new Intent(GameCompete2.this, MainMenuActivity.class));
                            Toast.makeText(getApplicationContext(), "Someone joined the room before you", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            if (documentSnapshot.get("playClicked").equals(Boolean.TRUE)) {
                                gameLaunch = true;
                                privremeno = true;
                                notCurrentPlayer = currentUser.getDisplayName().equals(player1.getUsername()) ? player2 : player1;
                                notCurrentPlayerUsername = notCurrentPlayer.getUsername();
                                roomDocumentReference.update("playClicked", Boolean.FALSE).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        mStartActivity(new Intent(getApplicationContext(), GameCompete3.class));
                                    }
                                });
                            } else if (getIntent().getBooleanExtra("fromRandom", false)) {
                                gameLaunch = true;
                                notCurrentPlayer = currentUser.getDisplayName().equals(player1.getUsername()) ? player2 : player1;
                                notCurrentPlayerUsername = notCurrentPlayer.getUsername();
                                mStartActivity(new Intent(getApplicationContext(), GameCompete3.class));
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (roomDocumentSnapshot == null && !roomName.equals(currentUser.getDisplayName())) {
                    mStartActivity(new Intent(GameCompete2.this, GameCompete.class));
                    Toast.makeText(GameCompete2.this, "Host closed room", Toast.LENGTH_SHORT).show();
                }
            } else if (getIntent().getBooleanExtra("fromRandom", false)) {
                try {
                    getPlayers1and2(roomName, documentSnapshot.get("Player2").toString());
                    if (documentSnapshot.get("Player1").toString().equals(roomName) && !privremeno) {
                        privremeno = true;
                        gameLaunch = true;
                        startGamePlease(null);
                        notCurrentPlayer = currentUser.getDisplayName().equals(player1.getUsername()) ? player2 : player1;
                        notCurrentPlayerUsername = notCurrentPlayer.getUsername();
                        mStartActivity(new Intent(getApplicationContext(), GameCompete3.class));
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    privremeno = false;
                }
            }
        };


        roomsCollectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                    if (Objects.requireNonNull(documentSnapshot.get("roomName")).toString().equals(roomName)) {
                        roomDocumentSnapshot = documentSnapshot;
                        roomDocumentReference = roomDocumentSnapshot.getReference();
                        roomDocumentReference.addSnapshotListener(GameCompete2.this, snapshotEventListener);
                        if (getIntent().getBooleanExtra("fromRandom", false) && !Objects.equals(currentUser.getDisplayName(), roomName)){
                            roomDocumentReference.update("Go", true);
                        }
                    }
                }
            }
        });
    }

    public void getPlayers1and2(final String p1, final String p2) {
        userCollectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                    if (documentSnapshot.get("username").toString().equals(p1)) {
                        player1 = documentSnapshot.toObject(User.class);
                    } else if (documentSnapshot.get("username").toString().equals(p2)) {
                        player2 = documentSnapshot.toObject(User.class);
                    }
                }
            }
        });
    }

    public void startGamePlease(View view) {
        if (player2 != null && player1 != null) {
            final int[] ints = new Random().ints(0, SplashScreenActivity.questionDatabaseNumber).distinct().limit(10).toArray();
            ArrayList<String> temp = new ArrayList<>();
            for (int i : ints) {
                temp.add(String.valueOf(i));
            }
            Map<String, Object> more = new HashMap<>();
            more.put("playClicked", true);
            more.put("randoms", temp);
            more.put("finished" + player1.getUsername(), false);
            more.put("finished" + player2.getUsername(), false);

            if (temp.size() == 10) {
                roomDocumentReference.update(more).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    @Override
    protected void onPause() {
        isOnPause = true;
        if (!Objects.equals(currentUser.getDisplayName(), roomName) && !gameLaunch) {
            player2 = null;
            Map<String, Object> more = new HashMap<>();
            more.put("Player2", "Player 2");
            more.put("full", false);
            more.put("currentPlayers", Integer.parseInt(Objects.requireNonNull(roomDocumentSnapshot.get("currentPlayers")).toString()) - 1);
            roomDocumentReference.update(more);
        }
        if (!gameLaunch && roomName.equals(currentUser.getDisplayName())) {
            roomDocumentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    roomDocumentSnapshot = null;
                }
            });
        }
        if (!isNewIntent) {
            mediaPlayer.stop();
            sharedPreferences.edit().putBoolean("isMusicPlaying", false).apply();
            isMusicPlaying = false;
        }
        finish();
        super.onPause();
    }

    @Override
    protected void onResume() {
        gameLaunch = false;
        backPressed = false;
        isNewIntent = false;
        isOnPause = false;
        if (sharedPreferences.getBoolean("musicEnabled", false) && !sharedPreferences.getBoolean("isMusicPlaying", false) && !mediaPlayer.isPlaying()) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.background_music);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        super.onResume();
    }

    public void mStartActivity(Intent intent) {
        isNewIntent = true;
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBackPressed() {
        backPressed = true;
        if (roomDocumentSnapshot != null && !Objects.equals(currentUser.getDisplayName(), roomName)) {
            player2 = null;
            Map<String, Object> more = new HashMap<>();
            more.put("Player2", "Player 2");
            more.put("full", false);
            more.put("currentPlayers", Integer.parseInt(Objects.requireNonNull(roomDocumentSnapshot.get("currentPlayers")).toString()) - 1);
            roomDocumentReference.update(more);
        }
        isNewIntent = true;
        super.onBackPressed();
    }

}
