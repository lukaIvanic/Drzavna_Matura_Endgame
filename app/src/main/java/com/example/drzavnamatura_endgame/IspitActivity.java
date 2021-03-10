package com.example.drzavnamatura_endgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drzavnamatura_endgame.RecyclerViewHelper.RecyclerAdapterPitanja;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IspitActivity extends AppCompatActivity implements RecyclerAdapterPitanja.onImageListener {

    RecyclerView recyclerView;
    RecyclerAdapterPitanja recyclerAdapterPitanja;
    ArrayList<Pitanje> arrayList;
    Button godButton;
    Boolean isRezultati;
    TextView naslov_ispita;
    public static Boolean allAnswersChecked;
    public static int correct_answers;
    private String razinaIspita = "";
    int ID_Ispita;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference pitanjaCollectionReference = db.collection("ispiti");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));
        setContentView(R.layout.activity_ispit);
        Intent intent = getIntent();
        ID_Ispita = intent.getIntExtra("ID_Ispita", 0);
        Log.i("ID", ID_Ispita + "!");

        recyclerView = findViewById(R.id.rec_view_ispit_indIspit);
        godButton = findViewById(R.id.god_button);
        naslov_ispita = findViewById(R.id.ispit_ispit_naslov);

        if (intent.getStringExtra("razina_ispita").equals("Viša razina")) {
            razinaIspita = "A";
        } else {
            razinaIspita = "B";
        }

        allAnswersChecked = false;
        correct_answers = 0;
        isRezultati = false;

        introDialog();

        arrayList = new ArrayList<>();
        String predmet = "";
        String razina = "";
        switch (intent.getIntExtra("naslov_ispita", Util.VRSTA_MATEMATIKA)) {
            case Util.VRSTA_MATEMATIKA:
                predmet = "Mat";
                break;
            case Util.VRSTA_HRVATSKI:
                predmet = "Hrv";
                break;
        }

        switch (intent.getStringExtra("razina_ispita")) {
            case "Viša razina":
                razina = "A";
                break;
            case "Niža razina":
                razina = "B";
                break;
        }

        naslov_ispita.setText(predmet + " " + razina + " " + intent.getStringExtra("datum_ispita"));

        ucitajPitanja();

        recyclerAdapterPitanja = new RecyclerAdapterPitanja(arrayList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapterPitanja);

        godButton_onclick();
    }

    private void ucitajPitanja() {
        String colPath = "ispit" + ID_Ispita + "_pitanja/";
        pitanjaCollectionReference = db.collection("ispiti/lista_pitanja/" + colPath);
        pitanjaCollectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Log.i("Pos Pitanje", Objects.requireNonNull(documentSnapshot.get("pitanje")).toString());
                    if (documentSnapshot.get("tocanOdgovorString").equals("nope")) {
                       /* if (documentSnapshot.get("url") == null) {
                            arrayList.add(new Pitanje(documentSnapshot.get("pitanje").toString(),
                                    documentSnapshot.get("odg1").toString(),
                                    documentSnapshot.get("odg2").toString(),
                                    documentSnapshot.get("odg3").toString(),
                                    documentSnapshot.get("odg4").toString(),
                                    Integer.parseInt(documentSnapshot.get("tocanOdgovor").toString()),
                                    0, false,
                                    documentSnapshot.get("metadata").toString()));
                        } else {*/
                        String url = "";
                        try {
                            url  = documentSnapshot.get("url").toString() + "";
                            
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Log.i("URL", documentSnapshot.get("pitanje").toString());
                            arrayList.add(new Pitanje(documentSnapshot.get("pitanje").toString(),
                                    documentSnapshot.get("odg1").toString(),
                                    documentSnapshot.get("odg2").toString(),
                                    documentSnapshot.get("odg3").toString(),
                                    documentSnapshot.get("odg4").toString(),
                                    Integer.parseInt(documentSnapshot.get("tocanOdgovor").toString()),
                                    url,
                                    0, false,
                                    documentSnapshot.get("metadata").toString()));

                    } else {
                        String url = "";
                        if (documentSnapshot.get("url") != null)url = documentSnapshot.get("url").toString();
                        arrayList.add(new Pitanje(documentSnapshot.get("pitanje").toString(),
                                documentSnapshot.get("user_odgovor").toString(),
                                documentSnapshot.get("tocanOdgovorString").toString(),
                                url));
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(IspitActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                recyclerAdapterPitanja.notifyDataSetChanged();
            }
        });

       /* for (int i = 0; i<arrayList.size(); i++) {
            String docPath;
            if((i+1)>9) {
                docPath = "pitanje" + (i + 1);
            }else{
                docPath = "pitanje0" + (i + 1);
            }
            pitanjaCollectionReference.document(docPath).set(arrayList.get(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(IspitActivity.this, "Smrt", Toast.LENGTH_SHORT).show();
                }
            });
        }*/
    }

    private void introDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(IspitActivity.this);
        builder.setTitle(Html.fromHtml("<font color='#FF0050'>BITNO!</font>"));
        builder.setMessage("Ako izađeš iz aplikacije moglo bi doći do problema s ispravljanjem ispita! \nVrijeme je neograničeno i možeš predati u bilo kojem trenutku. Rezultati prikazuju korisnikove točne odgovore od sveukupnih bodova.");
        builder.setPositiveButton(Html.fromHtml("<font color='#FF0050'>OK</font>"), null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void godButton_onclick() {


        godButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRezultati) {
                    allAnswersChecked = true;
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).getKojiOdgovor() == 0) {
                            allAnswersChecked = false;
                        }
                    }
                    if (allAnswersChecked) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(IspitActivity.this);
                        builder.setTitle(Html.fromHtml("<font color='#FF0050'>Ispravi ispit?</font>"));
                        builder.setMessage("Ako nastaviš dalje više nečeš moći mijenjati odgovore.");
                        builder.setPositiveButton(Html.fromHtml("<font color='#FF0050'>Ispravi</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Objects.requireNonNull(recyclerView.getLayoutManager()).scrollToPosition(0);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerAdapterPitanja.notifyDataSetChanged();
                                        godButton.setText("REZULTATI");
                                        isRezultati = true;
                                    }
                                }, 500);
                            }
                        });
                        builder.setNegativeButton(Html.fromHtml("<font color='#FF0050'>Ne još</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(IspitActivity.this);
                        builder.setTitle("Nisi ispunio sva polja!");
                        builder.setMessage("Ako ispraviš više nećeš moći mijenjati odgovore");
                        builder.setPositiveButton(Html.fromHtml("<font color='#FF0050'>Ispravi svejedno</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                recyclerView.smoothScrollToPosition(0);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        allAnswersChecked = true;
                                        recyclerAdapterPitanja.notifyDataSetChanged();
                                        godButton.setText("REZULTATI");
                                        isRezultati = true;
                                    }
                                }, 500);

                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                } else {
                    otvori_rezultate();
                }
            }
        });
    }

    private void otvori_rezultate() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_otvori_rezultate);
        TextView bodovi, postotak, ocjena;
        bodovi = dialog.findViewById(R.id.dialog_rezultat_bodovi_textview);
        postotak = dialog.findViewById(R.id.dialog_rezultat_postotak_textview);
        ocjena = dialog.findViewById(R.id.dialog_rezultat_ocjena_textview);
        bodovi.setText("Bodovi: " + recyclerAdapterPitanja.get_correct_count() + "/" + recyclerAdapterPitanja.getItemCount());
        postotak.setText("Postotak: " + recyclerAdapterPitanja.get_postotak() + "%");
        ocjena.setText("Ocjena: " + recyclerAdapterPitanja.get_ocjena());
        //dialog.dismiss();
        dialog.show();
    }

    @Override
    public void onImageClick(int position) {
        if (arrayList.get(position).getUrl() != null) {
            final Dialog dialog = new Dialog(IspitActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.setContentView(R.layout.dialog_slika);

            //dialog.dismiss();
            ImageView slika_pitanja = dialog.findViewById(R.id.dialog_slika_imageview);
            Log.i("'Path click'", position + ". " + arrayList.get(position).getUrl());
            Picasso.get()
                    .load(arrayList.get(position).getUrl())
                    .into(slika_pitanja);
            dialog.show();
        }


    }

    String GRADIVO_TEXT;
    int KOJA_STRANICA;

    @Override
    public void onSolutionClick(int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        try {
            //ALGORITAM ZA STRINGOVE
            final String[] array = arrayList.get(position).getMetadata().split(" ");
            GRADIVO_TEXT = "";
            for (int i = 2; i < array.length; i++) {
                GRADIVO_TEXT += array[i] + " ";
            }
            KOJA_STRANICA = Integer.parseInt(array[0]);
            builder.setTitle(Html.fromHtml("<font color='#FF0050'>Otvori skriptu?</font>"));
            builder.setMessage("Stranica: " + KOJA_STRANICA + "\n" + GRADIVO_TEXT);
            builder.setPositiveButton(Html.fromHtml("<font color='#FF0050'>OK</font>"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(IspitActivity.this, PDFActivity.class);
                    intent.putExtra("koja_skripta", Integer.parseInt(array[1]));
                    intent.putExtra("stranicaPDF", KOJA_STRANICA);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(Html.fromHtml("<font color='#FF0050'>CANCEL</font>"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            builder.setTitle(Html.fromHtml("<font color='#FF0050'>Objašnjenje</font>"));
            builder.setMessage(arrayList.get(position).getMetadata());
            if (arrayList.get(position).getMetadata().equals("")) {
                builder.setMessage("Nope");
            }
            builder.setPositiveButton(Html.fromHtml("<font color='#FF0050'>OK</font>"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(IspitActivity.this);
        builder.setTitle(Html.fromHtml("<font color='#FF0050'>Odustaješ od ispita?</font>"));
        builder.setMessage("Ako otiđeš tvoj ispit će se izbrisati.");
        builder.setPositiveButton(Html.fromHtml("<font color='#FF0050'>ODLAZAK</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(IspitActivity.this, ListIspitaActivity.class));
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
