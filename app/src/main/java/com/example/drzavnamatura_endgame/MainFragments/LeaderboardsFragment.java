package com.example.drzavnamatura_endgame.MainFragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.drzavnamatura_endgame.R;
import com.example.drzavnamatura_endgame.RecyclerViewHelper.MyGridLayoutManager;
import com.example.drzavnamatura_endgame.RecyclerViewHelper.RecyclerAdapterUsers;
import com.example.drzavnamatura_endgame.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.drzavnamatura_endgame.MainMenuActivity.isLeaderboardsOpened;

public class LeaderboardsFragment extends Fragment implements RecyclerAdapterUsers.OnItemListener {

    ArrayList<User> arrayListUsersLeaderboard;
    private int listSize=0;
    private int usersInLeaderboards = 30;

    RecyclerView recyclerView;
    RecyclerAdapterUsers adapterUsersLeadeboard;

    SwipeRefreshLayout swipeRefreshLeaderboards;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersCollectionRef = db.collection("users");

    public LeaderboardsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isLeaderboardsOpened = true;

        recyclerView = view.findViewById(R.id.recyclerViewLeaderboards);
        swipeRefreshLeaderboards = view.findViewById(R.id.swipeRefreshLeaderboard);
        arrayListUsersLeaderboard = new ArrayList<>();

        loadLeaderboard();

        swipeRefreshLeaderboards.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    arrayListUsersLeaderboard = new ArrayList<>();
                    loadLeaderboard();
                }catch (Exception ignored){
                };
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        User user = arrayListUsersLeaderboard.get(position);
        String titleText = user.getUsername();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#FF0050"));
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);
        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                titleText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        builder.setTitle(ssBuilder);
        builder.setMessage((position+1) + ". mjesto\n" + user.getScore() + " bodova");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void loadLeaderboard() {
        usersCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot userGetter : queryDocumentSnapshots.getDocuments()) {
                    arrayListUsersLeaderboard.add(userGetter.toObject(User.class));
                }
                listSize = arrayListUsersLeaderboard.size();
                Boolean not_sorted = true;
                while (not_sorted) {
                    not_sorted = false;
                    for (int i = 1; i < listSize; i++) {
                        if (Integer.parseInt(arrayListUsersLeaderboard.get(i).getScore()) > Integer.parseInt(arrayListUsersLeaderboard.get(i - 1).getScore())) {
                            User temp_user = arrayListUsersLeaderboard.get(i);
                            arrayListUsersLeaderboard.set(i, arrayListUsersLeaderboard.get(i - 1));
                            arrayListUsersLeaderboard.set(i - 1, temp_user);
                            not_sorted = true;
                        }
                    }
                    if (!not_sorted) {
                        break;
                    }
                }
                if(listSize>usersInLeaderboards){
                    for(int i = listSize; i > usersInLeaderboards; i--){
                        arrayListUsersLeaderboard.remove(i-1);
                    }
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                try {
                    adapterUsersLeadeboard = new RecyclerAdapterUsers(arrayListUsersLeaderboard, LeaderboardsFragment.this);
                    recyclerView.setLayoutManager(new MyGridLayoutManager(getContext(), 1));
                    recyclerView.getRecycledViewPool().clear();
                    recyclerView.swapAdapter(adapterUsersLeadeboard, true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLeaderboards.setRefreshing(false);
                        }
                    }, 400);
                }catch (Exception ignored){};
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        isLeaderboardsOpened = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isLeaderboardsOpened = false;
    }
}
