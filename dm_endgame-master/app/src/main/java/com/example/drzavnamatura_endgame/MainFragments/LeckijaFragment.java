package com.example.drzavnamatura_endgame.MainFragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.drzavnamatura_endgame.Lekcija;
import com.example.drzavnamatura_endgame.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.example.drzavnamatura_endgame.MainFragments.CjelineFragment.lekcija;

public class LeckijaFragment extends Fragment {

    public LeckijaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //AKO SE DOLAZI IZ CJELINA FRAGMENT ONDA SAVEA LEKCIJU, A U BILO KOJEM SLUCAJU LOADIRA LEKCIJU IZ SHARED PREFSA
        SharedPreferences sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        Gson gson = new Gson();

        //SAVEANJE
        if (lekcija != null) {
            String json = gson.toJson(lekcija);
            sharedPreferences.edit().remove("lekcija").apply();
            sharedPreferences.edit().putString("lekcija", json).apply();
        }

        //GETTANJE
        String json2 = sharedPreferences.getString("lekcija", null);
        lekcija = gson.fromJson(json2, Lekcija.class);
        try {
            switch (lekcija.getTIP_LEKCIJE()) {
                case "1":
                    return inflater.inflate(R.layout.lekcija_naslov_sadrzaj, container, false);

                case "2":
                    return inflater.inflate(R.layout.leckija_2tekst_1slika, container, false);
                default:
                    return inflater.inflate(R.layout.leckija_samo_naslov, container, false);
            }
        } catch (Exception e) {
            return inflater.inflate(R.layout.leckija_samo_naslov, container, false);
        }

    }

    TextView naslov, sadrzaj;
    ImageView slika;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        sharedPreferences.edit().remove("kojiFragment").apply();
        sharedPreferences.edit().putInt("kojiFragment", 3).apply();


            switch (lekcija.getTIP_LEKCIJE()) {
                case Lekcija.SAMO_NASLOV:
                    naslov = view.findViewById(R.id.naslovLekcijeULekciji);
                    naslov.setText(lekcija.getNaslov());

                    break;

                case Lekcija.NASLOV_SADRZAJ:
                    naslov = view.findViewById(R.id.naslovLekcijeULekciji);
                    naslov.setText(lekcija.getNaslov());

                    sadrzaj = view.findViewById(R.id.sadrzajLekcije);
                    sadrzaj.setText(lekcija.getSadrzaj());

                    break;

                case Lekcija.NASLOV_SADRZAJ_SLIKA:
                    naslov = view.findViewById(R.id.naslovLekcijeULekciji2t1s);
                    naslov.setText(lekcija.getNaslov());

                    sadrzaj = view.findViewById(R.id.sadrzajLekcije2t1s);
                    sadrzaj.setText(lekcija.getSadrzaj());

                    slika = view.findViewById(R.id.slikaLekcije2t1s);
                    Picasso.get()
                            .load(lekcija.getSlikaURL())
                            .placeholder(R.drawable.logo_lunar_studios)
                            .fit()
                            .into(slika);

                    break;


            }


    }
}
