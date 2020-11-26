package com.example.drzavnamatura_endgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class adminActivity extends AppCompatActivity {
    EditText pitanjeEt;
    EditText tocanOdgEt;
    EditText netocanOdgEt1;
    EditText netocanOdgEt2;
    EditText netocanOdgEt3;


    Button button;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference questionsCollectionRef = db.collection("Pitanja za kviz");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        pitanjeEt = findViewById(R.id.pitanjeEditText);
        tocanOdgEt = findViewById(R.id.tocanOdgEditText);
        netocanOdgEt1 = findViewById(R.id.netocanOdgET1);
        netocanOdgEt2 = findViewById(R.id.netocanOdgET2);
        netocanOdgEt3 = findViewById(R.id.netocanOdgET3);
        button = findViewById(R.id.spremiButtonPitanje);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                String pitanje = pitanjeEt.getText().toString();
                String tocanOdgovor = tocanOdgEt.getText().toString();
                ArrayList<String> netocniOdgovori = new ArrayList<>();
                netocniOdgovori.add(netocanOdgEt1.getText().toString());
                netocniOdgovori.add(netocanOdgEt2.getText().toString());
                netocniOdgovori.add(netocanOdgEt3.getText().toString());

                PitanjeKviz pitanjeKviz = new PitanjeKviz(pitanje, tocanOdgovor, netocniOdgovori);

                if (isValid(pitanje, tocanOdgovor, netocniOdgovori)){
                    questionsCollectionRef.add(pitanjeKviz).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            button.setEnabled(true);
                            Toast.makeText(adminActivity.this, "Uspjelo je!", Toast.LENGTH_SHORT).show();
                            deleteEditTexts();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            button.setEnabled(true);
                            e.printStackTrace();
                            Toast.makeText(adminActivity.this, "Nesto ni u redu\nprovjeri log pod tagom: greskaSpremanja ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    button.setEnabled(true);
                    Toast.makeText(adminActivity.this, "Polje prazno", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteEditTexts(){
        pitanjeEt.setText("");
        tocanOdgEt.setText("");
        netocanOdgEt1.setText("");
        netocanOdgEt2.setText("");
        netocanOdgEt3.setText("");
    }

    public boolean isValid(String pitanje, String tocanOdg, ArrayList<String> netocniOdg){
        boolean prazanElementNetocniOdg = false;
        for (String netocanOdg:netocniOdg){
            if (netocanOdg.equals("")){
                prazanElementNetocniOdg = true;
            }
        }
        if (pitanje.equals("") || tocanOdg.equals("") || prazanElementNetocniOdg){
            return false;
        }else{
            return true;
        }
    }
}