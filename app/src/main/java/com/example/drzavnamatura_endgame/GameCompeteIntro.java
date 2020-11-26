package com.example.drzavnamatura_endgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static com.example.drzavnamatura_endgame.MainActivity.isNewIntent;
import static com.example.drzavnamatura_endgame.MainMenuActivity.isMusicPlaying;
import static com.example.drzavnamatura_endgame.MainMenuActivity.mediaPlayer;

public class GameCompeteIntro extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference roomsCollectionReference = db.collection("rooms");
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    public static Room room;
    public static String roomName;
    boolean privremeno = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));
        setContentView(R.layout.activity_game_compete_intro);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        sharedPreferences = this.getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
    }

    public void openRooms(View view) {
        mStartActivity(new Intent(GameCompeteIntro.this, GameCompete.class));
    }

    public void matchRandom(View view) {
        if (!privremeno) {
            roomsCollectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if ((queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0)) {
                        int roomNumber = new Random().nextInt(queryDocumentSnapshots.size());
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(roomNumber);

                        if (Objects.equals(documentSnapshot.get("full"), Boolean.FALSE) && Integer.parseInt(documentSnapshot.get("currentPlayers").toString()) < 2) {
                            Intent intent = new Intent(GameCompeteIntro.this, GameCompete2.class);
                            intent.putExtra("fromRandom", true);
                            mStartActivity(intent);
                            roomName = documentSnapshot.get("roomName").toString();

                            Map<String, Object> more = new HashMap<>();
                            more.put("full", true);
                            more.put("currentPlayers", 2);
                            more.put("Player1", roomName);
                            more.put("Player2", currentUser.getDisplayName());
                            documentSnapshot.getReference().update(more);
                        } else {
                            matchRandom(null);
                        }
                    } else {
                        privremeno = true;
                        room = new Room(currentUser.getDisplayName(), 2, 1, false);
                        room.setFromRandom(true);
                        roomsCollectionReference.add(room).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Intent intent = new Intent(GameCompeteIntro.this, GameCompete2.class);
                                intent.putExtra("fromRandom", true);
                                mStartActivity(intent);
                                roomName = room.getRoomName();
                            }
                        });
                    }
                }
            });
        }

    }

    public void openInvite(View view){
        mStartActivity(new Intent(GameCompeteIntro.this, InviteActvitiy.class));
    }

    @Override
    protected void onPause() {
        if (!isNewIntent) {
            mediaPlayer.stop();
            sharedPreferences.edit().putBoolean("isMusicPlaying", false).apply();
            isMusicPlaying = false;
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        isNewIntent = false;
        privremeno = false;
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

    @Override
    public void onBackPressed() {
        isNewIntent = true;
        super.onBackPressed();
    }
}
