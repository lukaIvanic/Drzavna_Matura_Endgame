package com.example.drzavnamatura_endgame.MainFragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import com.example.drzavnamatura_endgame.Gradivo;
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
    ImageView backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sva_gradiva, container, false);


    }

    public GradivaFragment() {
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {




        backButton = view.findViewById(R.id.back_button_sva_gradiva);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

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
            case Util.matA:
                lista.add(new Gradivo("Brojevi", Util.PRVI_RAZRED,  chooserPredmeta));
                break;
            case Util.matB:
                lista.add(new Gradivo("Množenje", Util.PRVI_RAZRED,  chooserPredmeta));
                break;
            case Util.hrvB:
                lista.add(new Gradivo("Rocket Science", Util.CETVRTI_RAZRED,  chooserPredmeta));
                break;
            case Util.hrvA:
                lista.add(new Gradivo("Predikati", Util.DRUGI_RAZRED,  chooserPredmeta));
                break;
            case Util.engB:
                lista.add(new Gradivo("Kreten si kako ne znas engleski", Util.CETVRTI_RAZRED,  chooserPredmeta));
                break;
            case Util.engA:
                lista.add(new Gradivo("Ok ovo vec more proc",Util.TRECI_RAZRED,  chooserPredmeta));
                break;
            case Util.ALL:
                lista.add(new Gradivo("Brojevi", Util.PRVI_RAZRED,  Util.matA));
                lista.add(new Gradivo("Množenje", Util.PRVI_RAZRED,  Util.matB));
                lista.add(new Gradivo("Rocket Science", Util.CETVRTI_RAZRED,  Util.hrvB));
                lista.add(new Gradivo("Predikati", Util.DRUGI_RAZRED,  Util.hrvA));
                lista.add(new Gradivo("Kreten si kako ne znas engleski", Util.CETVRTI_RAZRED,  Util.engB));
                lista.add(new Gradivo("Ok ovo vec more proc",Util.TRECI_RAZRED,  Util.engA));
                break;
        }
    }


    @Override
    public void onItemClick(int position) {
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        sharedPreferences.edit().remove("kojeGradivo").apply();
        sharedPreferences.edit().putString("kojeGradivo", listGradiva.get(position).getNaslov()).apply();
        gradivoPosition = listGradiva.get(position).getNaslov();
        CjelineFragment cjelineFragment = new CjelineFragment();
        currentF = cjelineFragment;
        assert getFragmentManager() != null;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container, cjelineFragment);
        fragmentTransaction.commit();
    }
}
