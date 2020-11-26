package com.example.drzavnamatura_endgame.MainFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.drzavnamatura_endgame.GameCompeteIntro;
import com.example.drzavnamatura_endgame.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import static com.example.drzavnamatura_endgame.MainMenuActivity.currentF;
import static com.example.drzavnamatura_endgame.MainActivity.isNewIntent;


public class CompeteFragment extends Fragment {

    ConstraintLayout constraintLayout;
    ImageView button;
    ImageView kvizCard;


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
        kvizCard = view.findViewById(R.id.imageViewKviz);
        button = view.findViewById(R.id.floatingActionButton);


        kvizCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNewIntent = true;
                startActivity(new Intent(Objects.requireNonNull(getContext()).getApplicationContext(), GameCompeteIntro.class));
            }
        });
        go_to_leaderboards_listener();
    }

    private void go_to_leaderboards_listener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeaderboardsFragment leaderboardsFragment = new LeaderboardsFragment();
                currentF = leaderboardsFragment;
                assert getFragmentManager() != null;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_top);
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
