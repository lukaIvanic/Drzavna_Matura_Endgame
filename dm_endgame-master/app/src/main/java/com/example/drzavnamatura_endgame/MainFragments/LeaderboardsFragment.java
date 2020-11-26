package com.example.drzavnamatura_endgame.MainFragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.drzavnamatura_endgame.R;
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
import static com.example.drzavnamatura_endgame.MainMenuActivity.currentF;

public class LeaderboardsFragment extends Fragment implements RecyclerAdapterUsers.OnItemListener {
    ImageView back_button;

    ArrayList<User> arrayListUsersLeaderboard;

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


        back_button = view.findViewById(R.id.back_button_leaderboards);
        recyclerView = view.findViewById(R.id.recyclerViewLeaderboards);
        swipeRefreshLeaderboards = view.findViewById(R.id.swipeRefreshLeaderboard);
        arrayListUsersLeaderboard = new ArrayList<>();

        loadLeaderboard();

        swipeRefreshLeaderboards.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayListUsersLeaderboard.clear();
                loadLeaderboard();
            }
        });

        return_to_compete_on_button_click();
    }

    private void return_to_compete_on_button_click() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompeteFragment competeFragment = new CompeteFragment();
                currentF = competeFragment;
                assert getFragmentManager() != null;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragment_container, competeFragment, "compete");
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getContext(), arrayListUsersLeaderboard.get(position).getUsername(), Toast.LENGTH_SHORT).show();
    }

    private void loadLeaderboard() {
        usersCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot userGetter : queryDocumentSnapshots.getDocuments()) {
                    arrayListUsersLeaderboard.add(userGetter.toObject(User.class));
                }
                Boolean not_sorted = true;
                while (not_sorted) {
                    not_sorted = false;
                    for (int i = 1; i < arrayListUsersLeaderboard.size(); i++) {
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
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                adapterUsersLeadeboard = new RecyclerAdapterUsers(arrayListUsersLeaderboard, LeaderboardsFragment.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapterUsersLeadeboard);
                swipeRefreshLeaderboards.setRefreshing(false);

                swipeRefreshLeaderboards.setRefreshing(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLeaderboards.setRefreshing(false);
                    }
                }, 400);
            }
        });
    }
}
