package com.example.drzavnamatura_endgame.GameDirectory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drzavnamatura_endgame.MainActivity;
import com.example.drzavnamatura_endgame.MainMenuActivity;
import com.example.drzavnamatura_endgame.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class GameActivity2 extends AppCompatActivity {
    SeekBar timerSeekbar;
    TextView usernameCurrentPlayerTextView;
    TextView userScoreTextView;
    TextView plusScoreTextView;
    GridLayout gridLayout;
    Button choice1Button;
    Button choice2Button;
    Button choice3Button;
    Button choice4Button;
    RelativeLayout currentLayout;
    RelativeLayout insideLayout;
    TextView timerTextView;
    TextView pitanjeTextView;

    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser;
    private CollectionReference usersCollectionRef = db.collection("users");
    private CollectionReference questionsCollectionRef = db.collection("Pitanja za kviz");

    String userScoreBeforeGame = "";
    CountDownTimer countDownTimer;
    ArrayList<String> questions;
    ArrayList<String> answers;
    Map<String, ArrayList<String>> questionAndAnswers;
    Map<String, String> questionAndCorrectAnswers;
    int numberOfQuestions = 5;
    int correctAnswer;
    int[] questionNumber;
    int questionCounter = 0;
    int playerPlusScore = 0;
    CountDownTimer questionTimer;
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

        setContentView(R.layout.activity_game2);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        timerSeekbar = findViewById(R.id.timerSeekbar);
        usernameCurrentPlayerTextView = findViewById(R.id.usernameTextViewGame);
        userScoreTextView = findViewById(R.id.currentUserScoreTextView);
        plusScoreTextView = findViewById(R.id.plusScoreTV);
        gridLayout = findViewById(R.id.gridLayout);
        choice1Button = findViewById(R.id.gridButton1);
        choice2Button = findViewById(R.id.gridButton2);
        choice3Button = findViewById(R.id.gridButton3);
        choice4Button = findViewById(R.id.gridButton4);
        currentLayout = findViewById(R.id.Layout2);
        insideLayout = findViewById(R.id.insideRelLay);
        timerTextView = findViewById(R.id.timerTV);
        pitanjeTextView = findViewById(R.id.pitanjeTextView);

        questions = new ArrayList<>();
        answers = new ArrayList<>();
        questionAndAnswers = new HashMap<>();
        questionAndCorrectAnswers = new HashMap<>();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //POSTAVLJA TEXT VIEWOVE
        assert currentUser != null;
        usernameCurrentPlayerTextView.setText(currentUser.getDisplayName());
        setScore();
        //

        timerSeekbar.setEnabled(false);
        timerSeekbar.setMax(10);
        timerSeekbar.setMin(0);
        timerSeekbar.setProgress(10);
        currentLayout.setVisibility(View.GONE);
        generateQuestions();

        countDownTimer = new CountDownTimer(3100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(String.valueOf(millisUntilFinished / 1000));
                timerTextView.setAlpha(1);
                timerTextView.animate().alpha(0.3f).setDuration(850);
            }

            @Override
            public void onFinish() {
                insideLayout.setVisibility(View.GONE);
                timerTextView.setVisibility(View.INVISIBLE);
                currentLayout.setVisibility(View.VISIBLE);
                currentLayout.setClickable(true);
                currentLayout.setEnabled(true);

                questionNumber = new Random().ints(0, questions.size()).distinct().limit(numberOfQuestions).toArray();
                newQuestion(questionNumber[questionCounter]);

                runQuestionTimer();

            }
        }.start();
    }

    public void setScore() {
        try {
            usersCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                    for (int i = 0; i < documentSnapshots.size(); i++) {
                        DocumentSnapshot documentSnapshot = documentSnapshots.get(i);
                        String score = Objects.requireNonNull(documentSnapshot.get("score")).toString();
                        String usernameCurrent = documentSnapshot.get("username").toString();
                        if (usernameCurrent.equals(currentUser.getDisplayName())) {
                            userScoreBeforeGame = score;
                            userScoreTextView.setText(userScoreBeforeGame);
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GameActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void newQuestion(int questionNumber) {
        String question = questions.get(questionNumber);
        pitanjeTextView.setText(question);
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(choice1Button);
        buttons.add(choice2Button);
        buttons.add(choice3Button);
        buttons.add(choice4Button);

        final int[] colorRandoms = new Random().ints(0, 4).distinct().limit(4).toArray();
        buttons.get(colorRandoms[0]).setBackgroundColor(Color.GREEN);
        buttons.get(colorRandoms[1]).setBackgroundColor(Color.RED);
        buttons.get(colorRandoms[2]).setBackgroundColor(Color.YELLOW);
        buttons.get(colorRandoms[3]).setBackgroundColor(Color.BLUE);

        final int[] ints = new Random().ints(0, 4).distinct().limit(4).toArray();
        final int[] ints2 = new Random().ints(0, 8).distinct().limit(4).toArray();
        buttons.get(ints[0]).setText(questionAndCorrectAnswers.get(question));
        correctAnswer = ints[0];
        for (int i = 1; i < ints.length; i++) {
            buttons.get(ints[i]).setText(questionAndAnswers.get(question).get(ints2[i - 1]));
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

    public void checkAnswer(View view) {
        MainActivity.disableDoubleClick(choice1Button, 1500);
        MainActivity.disableDoubleClick(choice2Button, 1500);
        MainActivity.disableDoubleClick(choice3Button, 1500);
        MainActivity.disableDoubleClick(choice4Button, 1500);

        int timeLeft = timerSeekbar.getProgress();

        if (timeLeft > 0) {
            if (Integer.parseInt(view.getTag().toString()) == correctAnswer) {
                plusScoreTextView.setVisibility(View.VISIBLE);

                if (timeLeft > 8) {
                    plusScoreTextView.setText("+5");
                    playerPlusScore += 5;
                } else if (timeLeft > 6) {
                    plusScoreTextView.setText("+4");
                    playerPlusScore += 4;
                } else if (timeLeft > 4) {
                    plusScoreTextView.setText("+3");
                    playerPlusScore += 3;
                } else {
                    plusScoreTextView.setText("+1");
                    playerPlusScore += 1;
                }

            } else {
                plusScoreTextView.setVisibility(View.VISIBLE);
                plusScoreTextView.setText("INCORRECT");
            }
        } else {
            plusScoreTextView.setVisibility(View.VISIBLE);
            plusScoreTextView.setText("TOO SLOW");
        }


        questionTimer.cancel();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                questionCounter++;
                if (questionCounter == numberOfQuestions) {
                    timerTextView.setVisibility(View.VISIBLE);
                    insideLayout.setVisibility(View.VISIBLE);
                    currentLayout.setVisibility(View.GONE);
                    timerTextView.setAlpha(1);
                    timerTextView.setText("Final Score: " + playerPlusScore);
                    updateUserScore();
                } else {
                    runQuestionTimer();
                }

            }
        }, 1000);
    }

    public void updateUserScore() {
        try {
            usersCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                    for (int i = 0; i < documentSnapshots.size(); i++) {
                        DocumentSnapshot documentSnapshot = documentSnapshots.get(i);
                        String usernameCurrent = documentSnapshot.get("username").toString();
                        if (usernameCurrent.equals(currentUser.getDisplayName())) {
                            final DocumentReference mDocumentReference = usersCollectionRef.document(documentSnapshot.getId());
                            usersCollectionRef.document(documentSnapshot.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    Map<String, Object> newScore = new HashMap<>();
                                    newScore.put("score", String.valueOf(Integer.parseInt(userScoreBeforeGame) + playerPlusScore));
                                    mDocumentReference.update(newScore).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent intent = new Intent(GameActivity2.this, MainMenuActivity.class);
                                            startActivity(intent);
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
                    Toast.makeText(GameActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runQuestionTimer() {
        newQuestion(questionNumber[questionCounter]);
        plusScoreTextView.setVisibility(View.INVISIBLE);
        questionTimer = new CountDownTimer(10100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerSeekbar.setProgress((int) millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                checkAnswer(null);
            }
        }.start();
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
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
