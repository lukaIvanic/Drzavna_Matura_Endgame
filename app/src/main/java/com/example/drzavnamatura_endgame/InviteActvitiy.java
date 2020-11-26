package com.example.drzavnamatura_endgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.drzavnamatura_endgame.RecyclerViewHelper.InviteRecyclerAdapter;
import com.example.drzavnamatura_endgame.RecyclerViewHelper.RecyclerAdapterUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class InviteActvitiy extends AppCompatActivity{

    private SearchView searchViewInvite;
    private RecyclerView recyclerViewInvite;
    private InviteRecyclerAdapter inviteRecyclerAdapter;
    private ArrayList<User> userArrayList;
    static Button buttonContinue;
    public static ArrayList<Boolean> invitedArrayList;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersInvCollectionRef = db.collection("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_actvitiy);

        searchViewInvite = findViewById(R.id.searchViewInvite);
        recyclerViewInvite = findViewById(R.id.recViewInvite);
        buttonContinue = findViewById(R.id.buttonContinue);

        invitedArrayList = new ArrayList<>();
        userArrayList = new ArrayList<>();
        getUsers();

        buttonContinue.setEnabled(false);

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < invitedArrayList.size(); i++){
                    if(invitedArrayList.get(i)){
                        Log.i("INVITED", userArrayList.get(i).getUsername() + "\n");
                    }
                }
            }
        });
    }

    private void getUsers(){
        usersInvCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()){
                    userArrayList.add(documentSnapshot.toObject(User.class));
                    invitedArrayList.add(false);
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                inviteRecyclerAdapter = new InviteRecyclerAdapter(userArrayList);
                recyclerViewInvite.setLayoutManager(new LinearLayoutManager(InviteActvitiy.this));
                recyclerViewInvite.setAdapter(inviteRecyclerAdapter);
            }
        });
    }

    public static void enableButton(){
        buttonContinue.setEnabled(true);
    }

}