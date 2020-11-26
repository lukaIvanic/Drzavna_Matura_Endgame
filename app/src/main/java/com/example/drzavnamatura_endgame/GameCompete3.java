package com.example.drzavnamatura_endgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import static com.example.drzavnamatura_endgame.GameCompeteIntro.roomName;
import static com.example.drzavnamatura_endgame.GameCompete2.player1;
import static com.example.drzavnamatura_endgame.GameCompete2.player2;
import static com.example.drzavnamatura_endgame.MainActivity.isNewIntent;
import static com.example.drzavnamatura_endgame.MainMenuActivity.isMusicPlaying;
import static com.example.drzavnamatura_endgame.MainMenuActivity.mediaPlayer;

public class GameCompete3 extends AppCompatActivity {
    Button button1;
    Button button2;
    Button button3;
    Button button4;

    TextView pitanjeTextView;
    TextView user1TV;
    TextView user2TV;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference roomsCollectionReference = db.collection("rooms");
    DocumentReference roomDocumentReference;
    DocumentSnapshot roomDocumentSnapshot;
    CollectionReference userCollectionReference = db.collection("users");
    private CollectionReference questionsCollectionRef = db.collection("Pitanja za kviz");
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    ArrayList<String> questions;
    ArrayList<String> answers;
    Map<String, ArrayList<String>> questionAndAnswers;
    Map<String, String> questionAndCorrectAnswers;
    int numberOfQuestions = 10;
    int correctAnswer;
    int numberOfCorrectAnswers = 0;
    int questionCounter = 0;
    int playerPlusScore = 0;
    int timeInSeconds = 150;

    CountDownTimer questionTimer;
    ArrayList<Button> buttons;
    TextView timerTV;
    CountDownTimer countDownTimer;
    View wholeView;
    ArrayList<String> pitanjaRandom;
    ProgressBar timerSeekbar;
    boolean chosen = false;
    boolean updated = false;
    Random random;
    boolean done = false;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));
        setContentView(R.layout.activity_game_compete3);

        generateQuestions();

        pitanjeTextView = findViewById(R.id.pitanjeGameTextViewGame);
        button1 = findViewById(R.id.gameButton1);
        button2 = findViewById(R.id.gameButton2);
        button3 = findViewById(R.id.gameButton3);
        button4 = findViewById(R.id.gameButton4);
        user1TV = findViewById(R.id.playerOneTV3);
        user2TV = findViewById(R.id.playerTwoTV3);
        wholeView = findViewById(R.id.wholeView);
        timerTV = findViewById(R.id.timerTV420);
        timerSeekbar = findViewById(R.id.timerSeekbar420);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        sharedPreferences = this.getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);

        user1TV.setText(player1.getUsername());
        user2TV.setText(player2.getUsername());

        if (!Objects.equals(currentUser.getDisplayName(), player2.getUsername()) && !Objects.equals(currentUser.getDisplayName(), player1.getUsername())) {
            mStartActivity(new Intent(GameCompete3.this, MainMenuActivity.class));
            Toast.makeText(this, "Someone joined the room before you", Toast.LENGTH_SHORT).show();
        } else {
            questions = new ArrayList<>();
            answers = new ArrayList<>();
            questionAndAnswers = new HashMap<>();
            questionAndCorrectAnswers = new HashMap<>();

            pitanjaRandom = new ArrayList<>();

            buttons = new ArrayList<>();
            buttons.add(button1);
            buttons.add(button2);
            buttons.add(button3);
            buttons.add(button4);

            timerSeekbar.setEnabled(false);
            timerSeekbar.setMax(timeInSeconds);
            timerSeekbar.setMin(0);
            timerSeekbar.setProgress(timeInSeconds);

            final EventListener<DocumentSnapshot> eventListener = new EventListener<DocumentSnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    assert documentSnapshot != null;
                    if (Objects.equals(documentSnapshot.get("finished" + GameCompete2.notCurrentPlayer.getUsername()), Boolean.TRUE) && !done) {
                        done = true;
                        random = new Random();
                        playerPlusScore = random.nextInt(-10 - -20) - 20;
                        wholeView.setVisibility(View.VISIBLE);
                        timerTV.setVisibility(View.VISIBLE);
                        timerTV.setTextColor(Color.BLACK);
                        timerTV.setText("You Lost!\n" + playerPlusScore);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                roomDocumentReference.update("finished" + currentUser.getDisplayName(), Boolean.TRUE).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        updateUsersScores();
                                    }
                                });
                            }
                        }, 1000);
                    }
                }
            };


            countDownTimer = new CountDownTimer(3100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerTV.setText(String.valueOf(millisUntilFinished / 1000));
                    timerTV.setAlpha(1);
                    timerTV.animate().alpha(1.0f).setDuration(850);
                }

                @Override
                public void onFinish() {
                    roomsCollectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                if (Objects.requireNonNull(documentSnapshot.get("roomName")).toString().equals(roomName)) {
                                    roomDocumentSnapshot = documentSnapshot;
                                    roomDocumentReference = roomDocumentSnapshot.getReference();

                                    roomDocumentReference.addSnapshotListener(eventListener);

                                    timerTV.setAlpha(1);
                                    pitanjaRandom = (ArrayList<String>) documentSnapshot.get("randoms");
                                    wholeView.setVisibility(View.GONE);
                                    timerTV.setVisibility(View.INVISIBLE);
                                    runQuestionTimer();
                                    break;
                                }
                            }
                        }
                    });
                }
            }.start();
        }


    }

    public void newQuestion(int questionNumber) {
        String question = questions.get(questionNumber);
        pitanjeTextView.setText(question);
        timerTV.setVisibility(View.INVISIBLE);

        final int[] colorRandoms = new Random().ints(0, 4).distinct().limit(4).toArray();
        buttons.get(colorRandoms[0]).setBackgroundResource(R.drawable.green_compete_button);
        buttons.get(colorRandoms[1]).setBackgroundResource(R.drawable.blue_compete_button);
        buttons.get(colorRandoms[2]).setBackgroundResource(R.drawable.purple_compete_button);
        buttons.get(colorRandoms[3]).setBackgroundResource(R.drawable.yellow_compete_button);

        final int[] ints = new Random().ints(0, 4).distinct().limit(4).toArray();
        final int[] ints2 = new Random().ints(0, 3).distinct().limit(3).toArray();
        buttons.get(ints[0]).setText(questionAndCorrectAnswers.get(question));
        correctAnswer = ints[0];
        for (int i = 1; i < ints.length; i++) {
            buttons.get(ints[i]).setText(Objects.requireNonNull(questionAndAnswers.get(question)).get(ints2[i-1]));
        }
    }

    public void generateQuestions() {
        questionsCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot ds : documentSnapshots) {
                    String pitanje = ds.get("pitanje").toString();
                    ArrayList<String> netocniOdgovori = (ArrayList<String>) ds.get("netocniOdgovori");
                    String tocanOdgovor = ds.get("tocanOdgovor").toString();

                    questions.add(pitanje);
                    answers = netocniOdgovori;
                    questionAndAnswers.put(pitanje, answers);
                    questionAndCorrectAnswers.put(pitanje, tocanOdgovor);
                    answers = new ArrayList<>();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void checkAnswer1v1(final View view) {
        final int time = timerSeekbar.getProgress();
        chosen = true;
        MainActivity.disableDoubleClick(button1, 1200);
        MainActivity.disableDoubleClick(button2, 1200);
        MainActivity.disableDoubleClick(button3, 1200);
        MainActivity.disableDoubleClick(button4, 1200);

        if (view != null) {
            timerTV.setTextColor(Color.WHITE);
            if (Integer.parseInt(view.getTag().toString()) == correctAnswer) {
                numberOfCorrectAnswers++;
                timerTV.setVisibility(View.VISIBLE);
                timerTV.setText("GREAT!");
                if (numberOfCorrectAnswers == 5) {
                    roomDocumentReference.update("finished" + currentUser.getDisplayName(), Boolean.TRUE);
                }
            } else {
                timerTV.setVisibility(View.VISIBLE);
                timerTV.setText("False!");
            }
        }
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                if (Objects.equals(roomDocumentSnapshot.get("finished" + currentUser.getDisplayName()), Boolean.FALSE)) {
                    if (questionCounter < numberOfQuestions && time >= 1) {
                        timerTV.setVisibility(View.GONE);
                        questionCounter++;
                        if (numberOfCorrectAnswers == 5) {
                            questionTimer.cancel();
                            wholeView.setVisibility(View.VISIBLE);
                            timerTV.setTextColor(Color.BLACK);
                            timerTV.setVisibility(View.VISIBLE);
                            random = new Random();
                            playerPlusScore = random.nextInt(40 - 20) + 20;
                            timerTV.setText("You won!\n" + playerPlusScore);
                            updateUsersScores();
                        } else {
                            timerTV.setVisibility(View.VISIBLE);
                            newQuestion(Integer.parseInt(pitanjaRandom.get(questionCounter)));
                        }
                    } else {
                        random = new Random();
                        playerPlusScore = random.nextInt(-10 - -20) - 20;
                        wholeView.setVisibility(View.VISIBLE);
                        timerTV.setTextColor(Color.BLACK);
                        timerTV.setVisibility(View.VISIBLE);
                        timerTV.setText(questionCounter >= numberOfQuestions ?
                                "Potrosio si sva pitanja\n" + playerPlusScore :
                                "Ode vrijeme.\n" + playerPlusScore);

                        updateUsersScores();
                    }
                }
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    public void runQuestionTimer() {
        newQuestion(Integer.parseInt(pitanjaRandom.get(questionCounter)));
        timerTV.setVisibility(View.GONE);
        questionTimer = new CountDownTimer(timeInSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerSeekbar.setProgress((int) millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                checkAnswer1v1(null);
            }
        }.start();
    }

    public void updateUsersScores() {
        if (!updated) {
            updated = true;
            userCollectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        if (documentSnapshot.get("username").toString().equals(currentUser.getDisplayName())) {
                            String score = documentSnapshot.get("score").toString();
                            DocumentReference documentReference = documentSnapshot.getReference();
                            Map<String, Object> newScore = new HashMap<>();
                            newScore.put("score", String.valueOf(Integer.parseInt(score) + playerPlusScore));
                            documentReference.update(newScore).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    roomDocumentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent intent = new Intent(GameCompete3.this, MainMenuActivity.class);
                                            mStartActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            });
        }
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
}
