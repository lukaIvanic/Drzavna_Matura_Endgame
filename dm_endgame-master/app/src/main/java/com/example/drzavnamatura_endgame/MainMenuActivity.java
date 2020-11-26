package com.example.drzavnamatura_endgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.drzavnamatura_endgame.GameDirectory.GameActivity;
import com.example.drzavnamatura_endgame.MainFragments.CjelineFragment;
import com.example.drzavnamatura_endgame.MainFragments.CompeteFragment;
import com.example.drzavnamatura_endgame.MainFragments.GradivaFragment;
import com.example.drzavnamatura_endgame.MainFragments.HomeFragment;
import com.example.drzavnamatura_endgame.MainFragments.LeckijaFragment;
import com.example.drzavnamatura_endgame.MainFragments.ProfileFragment;
import com.example.drzavnamatura_endgame.MainFragments.SearchFragment;
import com.example.drzavnamatura_endgame.MainFragments.ShopFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.drzavnamatura_endgame.MainActivity.isNewIntent;
import static com.example.drzavnamatura_endgame.MainActivity.user;
import static com.example.drzavnamatura_endgame.MainFragments.SearchFragment.isKeyboardUp;
import static com.example.drzavnamatura_endgame.MainFragments.SearchFragment.searchView;


public class MainMenuActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    private CollectionReference usersCollectionRef = db.collection("users");

    Lekcija lekcijaSearch;
    public static ArrayList<Lekcija> listCjelina;
    public static ArrayList<Gradivo> listGradiva;

    public static String gradivoPosition;
    public static BottomNavigationView bottomNavigationView;

    String TAG = "Find me";

    boolean pressed;
    public static Fragment currentF;
    public static int kojiFragment;
    View decorView;

    public static String currentUserUsername = "";
    public static String currentUserEmail = "";

    public static MediaPlayer mediaPlayer;
    public static boolean musicEnabled;

    public static boolean isMusicPlaying = false;



    public static Boolean setHomeOnClick;
    boolean fragmentOpened = false;
    public static Boolean isHomeOpened = true;

    SharedPreferences sharedPreferences;

    public static String profilePicUrl = "";

    public static boolean isMale;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLekcije();
        listGradiva = new ArrayList<>();
        GradivaFragment.napuniListu(listGradiva, Util.ALL);

        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Window w = getWindow();
        w.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        setContentView(R.layout.activity_main_menu);

        setMarginTopAsStatusbar();

        usersCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                updateProfileFragment();
            }
        });

       /* decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });*/


        isNewIntent = false;


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        currentUserUsername = currentUser.getDisplayName();
        currentUserEmail = currentUser.getEmail();



        sharedPreferences = this.getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.background_music);
            mediaPlayer.setLooping(true);
        }

        if (!sharedPreferences.contains("musicEnabled")) {
            sharedPreferences.edit().putBoolean("musicEnabled", true).apply();
        }
        musicEnabled = sharedPreferences.getBoolean("musicEnabled", false);
        sharedPreferences.edit().putBoolean("isMusicPlaying", isMusicPlaying).apply();
        if (musicEnabled && !isMusicPlaying) {
            mediaPlayer.start();

        }

        try {
            final ArrayList<String> emails = new ArrayList<>();
            usersCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                    for (int i = 0; i < documentSnapshots.size(); i++) {
                        DocumentSnapshot documentSnapshot = documentSnapshots.get(i);
                        String objects = Objects.requireNonNull(documentSnapshot.get("email")).toString();
                        String names = Objects.requireNonNull(documentSnapshot.get("username")).toString();
                        emails.add(objects);
                        if (user == null && names.equals(currentUser.getDisplayName())) {
                            user = documentSnapshot.toObject(User.class);
                        }updateProfileFragment();
                    }
                }});
        }catch (Exception e) {
            e.printStackTrace();
        }


        //BOTTOM NAVIGATION BAR FRAGMENT PICKER
        bottomNavigationView.getBackground().setAlpha(220);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                pressed = false;
                if (item.getItemId() != R.id.navigation_home) {
                    Log.i("Not clicked on home", "");
                    setHomeOnClick = false;
                    isHomeOpened = false;
                }


                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        isHomeOpened =true;
                        fragmentOpened = false;
                        if (setHomeOnClick) {
                            sharedPreferences.edit().putInt("kojiFragment", 0).apply();
                        }
                        sjetiSeGdjeSiStao();
                        return true;
                    case R.id.navigation_compete:
                        currentF = new CompeteFragment();
                        openFragment(currentF);
                        return true;
                    case R.id.navigation_profile:
                        currentF = new ProfileFragment();
                        openFragment(currentF);
                        return true;
                    case R.id.navigation_search:
                        currentF = new SearchFragment();
                        openFragment(currentF);
                        return true;
                    case R.id.navigation_shop:
                        currentF = new ShopFragment();
                        openFragment(currentF);
                        return true;
                }

                return false;
            }
        });

        sjetiSeGdjeSiStao();
    }


    public void sjetiSeGdjeSiStao() {
        switch (sharedPreferences.getInt("kojiFragment", 0)) {
            case 0:
                pressed = false;  //ZA IZLAZ IZ APLIKACIJE
                currentF = new HomeFragment();
                break;
            case 1:
                currentF = new GradivaFragment();
                break;
            case 2:
                currentF = new CjelineFragment();
                break;
            case 3:
                currentF = new LeckijaFragment();
                break;
        }
        setHomeOnClick = true;
        openFragment(currentF);
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }


    public void openGradiva(View v) {
        fragmentOpened = true;
        isHomeOpened = false;
        if (v != null) {
            sharedPreferences.edit().putString("kojiPredmet", v.getTag().toString()).apply();
        }
        GradivaFragment gradivaFragment = new GradivaFragment();
        FragmentManager fragmentManager1 = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
        //pamtiGdjeSiStao();
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.remove(currentF);
        currentF = gradivaFragment;
        fragmentTransaction1.add(R.id.fragment_container, gradivaFragment, "gradiva").commit();

    }

    public void openCjeline() {
        CjelineFragment cjelineFragment = new CjelineFragment();
        FragmentManager fragmentManager1 = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
        ///  pamtiGdjeSiStao();
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.remove(currentF);
        currentF = cjelineFragment;
        fragmentTransaction1.add(R.id.fragment_container, cjelineFragment, "cjeline").commit();
    }


    public void openGame1v1(View view) {
        Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
        mStartActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (isKeyboardUp){
            searchView.clearFocus();
            isKeyboardUp = false;
            View view2 = this.getCurrentFocus();
            if (view2 != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
            }
        }
        /*
        else if (isHomeSubset) {
            switch (sharedPreferences.getInt("kojiFragment", 0)) {
                case 0:
                    if (!pressed) {
                        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
                        pressed = true;
                    } else {
                        finish();
                    }
                    break;
                case 1:
                    pressed = false;  //ZA IZLAZ IZ APLIKACIJE
                    currentF = new HomeFragment();
                    openFragment(currentF);
                    break;
                case 2:
                    openGradiva(null);
                    break;
                case 3:
                    openCjeline();
                    break;
            }
        }else{
            if (!pressed) {
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
                pressed = true;
            } else {
                finish();
            }
        }*/
        else if (isHomeOpened/* && nesto u search fragmentu nije otvvoreno*/){
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }else{
            switch (sharedPreferences.getInt("kojiFragment", 1)) {
                case 1:
                    currentF = new HomeFragment();
                    openFragment(currentF);
                    isHomeOpened =true;
                    break;
                case 2:
                    openGradiva(null);
                    break;
                case 3:
                    openCjeline();
                    break;
            }
        }

    }

    public void openSettings(View view) {
        Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
        mStartActivity(intent);
    }



    @Override
    protected void onStart() {
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
            }
        });

        super.onStart();
    }

    public void updateProfileFragment() {
            try {
                usersCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                        for (int i = 0; i < documentSnapshots.size(); i++) {
                            DocumentSnapshot documentSnapshot = documentSnapshots.get(i);
                            String score = Objects.requireNonNull(documentSnapshot.get("score")).toString();
                            String usernameCurrent = Objects.requireNonNull(documentSnapshot.get("username")).toString();
                            if (usernameCurrent.equals(currentUser.getDisplayName())) {
                                user.setScore(score);
                                try {
                                    profilePicUrl = Objects.requireNonNull(documentSnapshot.get("profilePicUrl")).toString();
                                }catch (Exception e){
                                    profilePicUrl = "";
                                }
                                break;
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

    public void mStartActivity(Intent intent){
        isNewIntent = true;
        startActivity(intent);
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
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.cancelAll();
        isNewIntent = false;
        if (sharedPreferences.getBoolean("musicEnabled", false)&&!isMusicPlaying&&!mediaPlayer.isPlaying()){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.background_music);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        super.onResume();
    }


//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            decorView.setSystemUiVisibility(hideSystemBars());
//        }
//    }
//
//    private int hideSystemBars() {
//        return View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
//    }

    public static boolean isForeground(Context ctx, String myPackage){
        ActivityManager manager = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > runningTaskInfo = manager.getRunningTasks(1);

        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        if(componentInfo.getPackageName().equals(myPackage)) {
            return true;
        }
        return false;
    }

    public void getLekcije() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = database.collection("lekcije");
        listCjelina = new ArrayList<>();
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot lekcijeGetter : queryDocumentSnapshots) {
                        //Checka sto lekcija sadrzi i daje informacije prikladnom konstruktoru
                        //To se radi zbog jednostavnosti u dodavanju lekcija
                        if (lekcijeGetter.getString("sadrzaj") != null && !lekcijeGetter.getString("sadrzaj").equals("")) {
                            if (lekcijeGetter.getString("imageURL") != null && !lekcijeGetter.getString("imageURL").equals("")) {
                                lekcijaSearch = new Lekcija(lekcijeGetter.getString("Naslov"), lekcijeGetter.getString("sadrzaj"), lekcijeGetter.getString("imageURL"), lekcijeGetter.getString("kojiPredmet"), lekcijeGetter.getString("kojeGradivo"), Integer.parseInt(lekcijeGetter.getString("razred")));
                            } else {
                                lekcijaSearch = new Lekcija(lekcijeGetter.getString("Naslov"), lekcijeGetter.getString("sadrzaj"), lekcijeGetter.getString("kojiPredmet"), lekcijeGetter.getString("kojeGradivo"), Integer.parseInt(lekcijeGetter.getString("razred")));
                            }
                        } else {
                            lekcijaSearch = new Lekcija(lekcijeGetter.getString("Naslov"), lekcijeGetter.getString("kojiPredmet"), lekcijeGetter.getString("kojeGradivo"), Integer.parseInt(lekcijeGetter.getString("razred")));
                        }
                        listCjelina.add(lekcijaSearch);    //tu se dodaje u listu
                    }
                } else {
                    Toast.makeText(MainMenuActivity.this, "Dokumenti u 'Lekcije' su prazni", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainMenuActivity.this, "OnFailureActivated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMarginTopAsStatusbar() {
        //SETTA TOP MARGIN FRAME LAYOUTA NA VRIJEDNOST OD STATUS BARA DA SENEOVERLAPPA
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        FrameLayout frameLayout = findViewById(R.id.fragment_container);

        if (frameLayout.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
            p.setMargins(0, result, 0, 0);
            frameLayout.requestLayout();
        }



    }




}

