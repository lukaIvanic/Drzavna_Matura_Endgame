package com.example.drzavnamatura_endgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.drzavnamatura_endgame.RecyclerViewHelper.RecyclerAdapterIspita;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class ListIspitaActivity extends AppCompatActivity implements RecyclerAdapterIspita.onIspitListener {

    private RecyclerView rec_view_ispiti_mature, rec_view_ispiti_gradiva;
    private RecyclerAdapterIspita recyclerAdapterIspita, recyclerGradivaAdapterIspita;
    private ArrayList<Ispit> ispitArrayList, ispitGradivaArrayList;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ispitiCollectionRef = db.collection("ispiti");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));
        setContentView(R.layout.activity_lista_ispita);

        rec_view_ispiti_mature = findViewById(R.id.rec_view_matura_ispiti);
        ispitArrayList = new ArrayList<>();

        getIspiti();

        recyclerAdapterIspita = new RecyclerAdapterIspita(ispitArrayList, this, Util.ISPITI_MATURE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        rec_view_ispiti_mature.setLayoutManager(gridLayoutManager);
        rec_view_ispiti_mature.setAdapter(recyclerAdapterIspita);
/*
        rec_view_ispiti_gradiva = findViewById(R.id.rec_view_gradiva_ispiti);
        ispitGradivaArrayList = new ArrayList<>();
        ispitGradivaArrayList.add(new Ispit("Niža razina", "19./20.",64, Util.VRSTA_HRVATSKI, 1001));
        recyclerGradivaAdapterIspita = new RecyclerAdapterIspita(ispitGradivaArrayList, this, Util.ISPITI_GRADIVA);
        rec_view_ispiti_gradiva.setLayoutManager(new LinearLayoutManager(this));
        rec_view_ispiti_gradiva.setAdapter(recyclerGradivaAdapterIspita);*/
    }

    private void getIspiti() {
        ispitiCollectionRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            String dbIspit;
            Log.i("TAG", "Snapped");
            for (int i = 0; i < 7; i++) {
                String razina = "Niža razina";
                int predmet = Util.VRSTA_HRVATSKI;

                dbIspit = "ispit" + (i + 1);
                Log.i("position", i + ".");
                String[] ispit_info = Objects.requireNonNull(Objects.requireNonNull(queryDocumentSnapshots.getDocuments().get(0).get(dbIspit)).toString().split(" "));

                if (ispit_info[1].equals("A")) razina = "Viša razina";
                if (ispit_info[0].equals("Mat")) predmet = Util.VRSTA_MATEMATIKA;

                ispitArrayList.add(new Ispit(razina, ispit_info[2] + " " + ispit_info[3] + ".", 0, predmet, Integer.parseInt(ispit_info[4])));
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ListIspitaActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                Log.i("TAG", "Fail");
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                recyclerAdapterIspita.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onIspitClick(int position, int vrstaIspita) {
        int ID_Ispita;
        if (vrstaIspita == Util.ISPITI_MATURE) {
            ID_Ispita = ispitArrayList.get(position).getId();
        } else {
            ID_Ispita = ispitGradivaArrayList.get(position).getId();
        }
        Intent intent = new Intent(ListIspitaActivity.this, IspitActivity.class);
        intent.putExtra("ID_Ispita", ID_Ispita);
        intent.putExtra("razina_ispita", ispitArrayList.get(position).getRazina());
        intent.putExtra("naslov_ispita", ispitArrayList.get(position).getPredmet() + "");
        intent.putExtra("datum_ispita", ispitArrayList.get(position).getDatum());
        startActivity(intent);
        finish();
    }
}
