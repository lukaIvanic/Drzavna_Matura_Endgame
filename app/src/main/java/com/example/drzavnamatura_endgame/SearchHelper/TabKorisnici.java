package com.example.drzavnamatura_endgame.SearchHelper;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.drzavnamatura_endgame.R;
import com.example.drzavnamatura_endgame.RecyclerViewHelper.RecyclerAdapterUsers;
import com.example.drzavnamatura_endgame.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static com.example.drzavnamatura_endgame.MainActivity.user;

public class TabKorisnici extends Fragment implements RecyclerAdapterUsers.OnItemListener {

    public TabKorisnici() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    ArrayList<User> arrayListUsers;
    public static RecyclerAdapterUsers adapterKorisnika;
    SwipeRefreshLayout swipeRefresh;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersCollectionRef = db.collection("users");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_korisnici, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewTabKorisnici);
        swipeRefresh = view.findViewById(R.id.swipeRefreshTabKorisnici);
        arrayListUsers = new ArrayList<>();


        loadUsers();


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUsers();
            }
        });
    }

    private void loadUsers() {
        usersCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                arrayListUsers.clear();
                for (QueryDocumentSnapshot user : queryDocumentSnapshots) {
                    arrayListUsers.add(user.toObject(User.class));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Couldn't load users, try again.", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                adapterKorisnika = new RecyclerAdapterUsers(arrayListUsers, TabKorisnici.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapterKorisnika);
                swipeRefresh.setRefreshing(false);
                swipeRefresh.setRefreshing(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                }, 400);
                adapterKorisnika.filterByName();
            }
        });
    }

    @Override
    public void onItemClick(int position) {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_fragment_detalji_korisnika);
        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        TextView username = (TextView) dialog.findViewById(R.id.dialogUsername);
        username.setText(arrayListUsers.get(position).getUsername());
        TextView score = dialog.findViewById(R.id.dialogScore);
        score.setText(arrayListUsers.get(position).getScore());

        //dialog.dismiss();
        ImageView profilna = dialog.findViewById(R.id.dialogProfilna);
        Picasso.get()
                .load(arrayListUsers.get(position).getProfilePicUrl())
                .transform(new CropCircleTransformation())
                .fit()
                .into(profilna);

        dialog.show();

    }
}
