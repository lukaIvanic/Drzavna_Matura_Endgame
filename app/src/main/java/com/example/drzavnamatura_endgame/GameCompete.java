package com.example.drzavnamatura_endgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

import static com.example.drzavnamatura_endgame.GameCompeteIntro.room;
import static com.example.drzavnamatura_endgame.GameCompeteIntro.roomName;
import static com.example.drzavnamatura_endgame.MainActivity.isNewIntent;
import static com.example.drzavnamatura_endgame.MainActivity.user;
import static com.example.drzavnamatura_endgame.MainMenuActivity.isMusicPlaying;
import static com.example.drzavnamatura_endgame.MainMenuActivity.mediaPlayer;

public class GameCompete extends AppCompatActivity {
    ListView roomsListView;
    public static ArrayList<String> rooms;
    ArrayAdapter<String> adapter;
    Button createRoomButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference roomsCollectionReference = db.collection("rooms");
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    Random random;
    ConstraintLayout constraintLayout;
    boolean mrzinAndroidStudio = false;
    boolean stisnutCreateIliJoin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));
        setContentView(R.layout.activity_game_compete);

        roomsListView = findViewById(R.id.playersListView);
        rooms = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_listview, R.id.text1, rooms);
        roomsListView.setAdapter(adapter);
        createRoomButton = findViewById(R.id.createRoomButton);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        progressBar = findViewById(R.id.progress_bar_game_compete1);
        progressBar.setVisibility(View.INVISIBLE);
        random = new Random();
        sharedPreferences = this.getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        constraintLayout = findViewById(R.id.layout69);

        roomsCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                rooms.clear();
                if (e == null){
                    assert queryDocumentSnapshots != null;
                    List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.size() > 0 ?
                            queryDocumentSnapshots.getDocuments() : null;

                    if (documentSnapshots != null){
                        for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                            if (Objects.equals(documentSnapshot.get("fromRandom"), Boolean.FALSE)){
                                if (!rooms.contains(documentSnapshot.get("roomName").toString())) {
                                    rooms.add(documentSnapshot.get("roomName").toString());
                                }if (Objects.equals(documentSnapshot.get("full"), Boolean.TRUE)){
                                    rooms.remove(documentSnapshot.get("roomName").toString());
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (rooms.contains(currentUser.getDisplayName())){
                        createRoomButton.setEnabled(false);
                    }else{
                        createRoomButton.setEnabled(true);
                    }
                }
            }
        });

        createRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!stisnutCreateIliJoin){
                    stisnutCreateIliJoin = true;
                    createRoomButton.setEnabled(false);
                    roomsListView.setClickable(false);
                    room = new Room(currentUser.getDisplayName(), 2, 1, false);
                    room.setFromRandom(false);
                    roomsCollectionReference.add(room).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(GameCompete.this, "Room created", Toast.LENGTH_SHORT).show();
                            mStartActivity(new Intent(GameCompete.this, GameCompete2.class));
                            roomName = room.getRoomName();
                        }
                    });
                }

            }
        });
        roomsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!stisnutCreateIliJoin){
                    stisnutCreateIliJoin=true;
                    createRoomButton.setEnabled(false);
                    roomsListView.setClickable(false);
                    mrzinAndroidStudio = false;
                    roomName = rooms.get(position);
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            roomsCollectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (final DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                        int playersNumber = Integer.parseInt(Objects.requireNonNull(documentSnapshot.get("currentPlayers")).toString());
                                        if (Objects.requireNonNull(documentSnapshot.get("roomName")).toString().equals(roomName) &&
                                                Objects.equals(documentSnapshot.get("full"), Boolean.FALSE) && playersNumber<2){
                                            Map<String, Object> more = new HashMap<>();
                                            more.put("full", true);
                                            more.put("currentPlayers", ++playersNumber);
                                            more.put("Player1", roomName);
                                            more.put("Player2", currentUser.getDisplayName());
                                            documentSnapshot.getReference().update(more).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    if (Objects.requireNonNull(documentSnapshot.get("currentPlayers")).toString().contains("1")){
                                                        Intent intent = new Intent(GameCompete.this, GameCompete2.class);
                                                        mStartActivity(intent);
                                                    }else{
                                                        Toast.makeText(GameCompete.this, "Room is full or something went wrong", Toast.LENGTH_SHORT).show();
                                                        createRoomButton.setEnabled(true);
                                                        roomsListView.setClickable(true);
                                                        stisnutCreateIliJoin=false;
                                                    }
                                                }
                                            });
                                        }else{
                                            Toast.makeText(GameCompete.this, "Room is full or something went wrong", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                            createRoomButton.setEnabled(true);
                                            roomsListView.setClickable(true);
                                            stisnutCreateIliJoin=false;
                                        }
                                        break;
                                    }
                                }
                            });
                        }
                    },new Random().nextInt(1800 - 500) + 500);
                }

            }
        });
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
        stisnutCreateIliJoin = false;
        createRoomButton.setEnabled(true);
        roomsListView.setClickable(true);
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
        super.onBackPressed();
    }
}
