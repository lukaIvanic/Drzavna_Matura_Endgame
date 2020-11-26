package com.example.drzavnamatura_endgame.MainFragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drzavnamatura_endgame.Lekcija;
import com.example.drzavnamatura_endgame.R;
import com.example.drzavnamatura_endgame.RecyclerViewHelper.RecyclerAdapterGIC;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.example.drzavnamatura_endgame.MainMenuActivity.currentF;
import static com.example.drzavnamatura_endgame.MainMenuActivity.kojiFragment;


public class CjelineFragment extends Fragment implements RecyclerAdapterGIC.OnItemListener {

    ArrayList<Lekcija> listCjelina = new ArrayList<>();
    ImageView backArrowButtonCjeline;
    TextView predmetNaslov, gradivoNaslov;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    RecyclerAdapterGIC adapterCjelinaFragment;
    public static String koji_url;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = database.collection("lekcije");

    public static int layout_lekcije;
    public static Lekcija lekcija;

    public CjelineFragment() {
        kojiFragment = 2;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sve_cjeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        topElementsBeDo(view);

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot lekcijeGetter : queryDocumentSnapshots) {
                        if (lekcijeGetter.getString("kojeGradivo").equals(sharedPreferences.getString("kojeGradivo", ""))) {
                            //Checka sto lekcija sadrzi i daje informacije prikladnom konstruktoru
                            //To se radi zbog jednostavnosti u dodavanju lekcija
                            if (lekcijeGetter.getString("sadrzaj") != null && !lekcijeGetter.getString("sadrzaj").equals("")) {
                                if (lekcijeGetter.getString("imageURL") != null && !lekcijeGetter.getString("imageURL").equals("")) {
                                    lekcija = new Lekcija(lekcijeGetter.getString("Naslov"), lekcijeGetter.getString("sadrzaj"), lekcijeGetter.getString("imageURL"), lekcijeGetter.getString("kojiPredmet"), lekcijeGetter.getString("kojeGradivo"), Integer.parseInt(lekcijeGetter.getString("razred")));
                                } else {
                                    lekcija = new Lekcija(lekcijeGetter.getString("Naslov"), lekcijeGetter.getString("sadrzaj"), lekcijeGetter.getString("kojiPredmet"), lekcijeGetter.getString("kojeGradivo"), Integer.parseInt(lekcijeGetter.getString("razred")));
                                }
                            } else {
                                lekcija = new Lekcija(lekcijeGetter.getString("Naslov"), lekcijeGetter.getString("kojiPredmet"), lekcijeGetter.getString("kojeGradivo"), Integer.parseInt(lekcijeGetter.getString("razred")));
                            }
                            listCjelina.add(lekcija);    //tu se dodaje u listu
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Dokumenti u 'Lekcije' su prazni", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "OnFailureActivated", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                recyclerView = view.findViewById(R.id.recyclerViewCjelina);
                adapterCjelinaFragment = new RecyclerAdapterGIC(listCjelina, CjelineFragment.this);
                recyclerView.setAdapter(adapterCjelinaFragment);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });
    }


    public void onItemClickCjeline(final int position) {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_biraj_link);
        TextView naslov_lekcije, izbor_toni, izbor_edutorij;
        naslov_lekcije = dialog.findViewById(R.id.selected_lekcija_name_textview);
        izbor_toni = dialog.findViewById(R.id.izbor_toni_milun);
        izbor_edutorij = dialog.findViewById(R.id.izbor_edutorij_textview);

        naslov_lekcije.setText(listCjelina.get(position).getNaslov());

        izbor_toni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                koji_url = "https://www.tonimilun.hr/";
                go_to_view_lekcija();
                dialog.dismiss();
            }
        });

        izbor_edutorij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                koji_url = "https://edutorij.e-skole.hr/share/page/home-page";
                go_to_view_lekcija();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void go_to_view_lekcija() {
        LeckijaFragment lekcijaFragment = new LeckijaFragment();
        currentF = lekcijaFragment;
        assert getFragmentManager() != null;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_top);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container, lekcijaFragment, "cjeline");
        fragmentTransaction.commit();
        layout_lekcije = R.layout.leckija_2tekst_1slika;
    }

    private void topElementsBeDo(View view) {
     /*   backArrowButtonCjeline = view.findViewById(R.id.backArrowsvecjeline);
        backArrowButtonCjeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });*/

        predmetNaslov = view.findViewById(R.id.cjelineFragmentPredmetNaslov);
        gradivoNaslov = view.findViewById(R.id.cjelineFragmentGradivoNaslov);


        sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        sharedPreferences.edit().remove("kojiFragment").apply();
        sharedPreferences.edit().putInt("kojiFragment", 2).apply();

        gradivoNaslov.setText(sharedPreferences.getString("kojeGradivo", ""));
        predmetNaslov.setText(sharedPreferences.getString("kojiPredmet", ""));
    }

    @Override
    public void onItemClick(int position) {
        onItemClickCjeline(position);
    }
}
