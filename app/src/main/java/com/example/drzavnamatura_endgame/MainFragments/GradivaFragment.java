package com.example.drzavnamatura_endgame.MainFragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drzavnamatura_endgame.Gradivo;
import com.example.drzavnamatura_endgame.PDFActivity;
import com.example.drzavnamatura_endgame.R;
import com.example.drzavnamatura_endgame.RecyclerViewHelper.RecyclerAdapterGIC;
import com.example.drzavnamatura_endgame.Util;


import static com.example.drzavnamatura_endgame.MainMenuActivity.currentF;
import static com.example.drzavnamatura_endgame.MainMenuActivity.gradivoPosition;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class GradivaFragment extends Fragment implements RecyclerAdapterGIC.OnItemListener{

    private ArrayList<Gradivo> listGradiva;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerViewGradiva;
    RecyclerAdapterGIC adapterGradivaFragment;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sva_gradiva, container, false);


    }

    public GradivaFragment() {
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        sharedPreferences.edit().remove("kojiFragment").apply();
        sharedPreferences.edit().putInt("kojiFragment", 1).apply();


        listGradiva = new ArrayList<>();

        napuniListu(listGradiva, sharedPreferences.getString("kojiPredmet", ""));

        textView = view.findViewById(R.id.gradivaFragmentPredmetNaslov);

        textView.setText(sharedPreferences.getString("kojiPredmet", ""));

        recyclerViewGradiva = view.findViewById(R.id.recyclerViewGradiva);

        adapterGradivaFragment = new RecyclerAdapterGIC(listGradiva, true, this);

        recyclerViewGradiva.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewGradiva.setAdapter(adapterGradivaFragment);


    }

    public static void napuniListu(ArrayList<Gradivo> lista, String chooserPredmeta) {
        lista.clear();
        switch (chooserPredmeta) {
            /*case Util.matA:
                break;
            case Util.matB:
                break;
            case Util.engB:
                break;
            case Util.engA:
                break;*/
            case Util.hrvB:
                lista.add(new Gradivo("Skripta Hrvatski", "Skripta",  Util.hrvB, 3));
                lista.add(new Gradivo("Skripta Hrvatski 2", "Skripta",  Util.hrvB, 4));
                lista.add(new Gradivo("Hrvatski Jezik Kratko", "Skripta",  Util.hrvB, 5));
                lista.add(new Gradivo("Hrvatski Književnost Jezik", "Skripta",  Util.hrvB, 6));
                lista.add(new Gradivo("Hrvatski Memo", "Skripta",  Util.hrvB, 7));
                break;
            case Util.hrvA:
                lista.add(new Gradivo("Hrvatski Jezik Viša Razina", "Skripta",  Util.hrvA, 1));
                lista.add(new Gradivo("Hrvatski Viša Razina 2013.", "Skripta",  Util.hrvA, 2));
                break;
            case Util.ALL:
                lista.add(new Gradivo("Hrvatski Viša Razina 2013.", "Skripta",  Util.hrvA, 2));
                lista.add(new Gradivo("Hrvatski Jezik Kratko", "Skripta",  Util.hrvB, 5));
                lista.add(new Gradivo("Skripta Hrvatski", "Skripta",  Util.hrvB, 3));
                lista.add(new Gradivo("Skripta Hrvatski 2", "Skripta",  Util.hrvB, 4));
                lista.add(new Gradivo("Hrvatski Jezik Viša Razina", "Skripta",  Util.hrvA, 1));
                lista.add(new Gradivo("Hrvatski Književnost Jezik", "Skripta",  Util.hrvB, 6));
                lista.add(new Gradivo("Hrvatski Memo", "Skripta",  Util.hrvB, 7));
                break;
        }
    }


    @Override
    public void onItemClick(int position) {
        if(!listGradiva.get(0).getRazred().equals("Skripta")) {
            SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
            sharedPreferences.edit().remove("kojeGradivo").apply();
            sharedPreferences.edit().putString("kojeGradivo", listGradiva.get(position).getNaslov()).apply();
            gradivoPosition = listGradiva.get(position).getNaslov();
            CjelineFragment cjelineFragment = new CjelineFragment();
            currentF = cjelineFragment;
            assert getFragmentManager() != null;
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_top);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.fragment_container, cjelineFragment);
            fragmentTransaction.commit();
        }else{
            Intent intent = new Intent(getActivity(), PDFActivity.class);
            intent.putExtra("koja_skripta", listGradiva.get(position).getVrsta());
            intent.putExtra("naziv_skripte", listGradiva.get(position).getNaslov());
            startActivity(intent);
        }
    }
}
