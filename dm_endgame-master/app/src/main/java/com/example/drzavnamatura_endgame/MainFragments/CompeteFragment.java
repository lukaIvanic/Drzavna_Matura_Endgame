package com.example.drzavnamatura_endgame.MainFragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.drzavnamatura_endgame.MainMenuActivity;
import com.example.drzavnamatura_endgame.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.example.drzavnamatura_endgame.MainMenuActivity.currentF;
import static com.example.drzavnamatura_endgame.MainMenuActivity.gradivoPosition;
import static com.example.drzavnamatura_endgame.MainMenuActivity.kojiFragment;


public class CompeteFragment extends Fragment {

    ConstraintLayout constraintLayout;
    FloatingActionButton button;
    Button igrajSaSkolomButton;


    public CompeteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compete, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        constraintLayout = view.findViewById(R.id.noice);
        igrajSaSkolomButton = view.findViewById(R.id.button5);
        button = view.findViewById(R.id.floatingActionButton);

        igrajSaSkolomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSnackbar("USKORO");
            }
        });

        go_to_leaderboards();

    }

    private void go_to_leaderboards() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeaderboardsFragment leaderboardsFragment = new LeaderboardsFragment();
                currentF = leaderboardsFragment;
                assert getFragmentManager() != null;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragment_container, leaderboardsFragment, "compete");
                fragmentTransaction.commit();

            }
        });
    }

    public void makeSnackbar(String text) {
        Snackbar snackbar = Snackbar.make(constraintLayout, text, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

}
