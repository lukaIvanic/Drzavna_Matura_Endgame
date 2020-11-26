package com.example.drzavnamatura_endgame.SearchHelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drzavnamatura_endgame.MainFragments.CjelineFragment;
import com.example.drzavnamatura_endgame.MainFragments.GradivaFragment;
import com.example.drzavnamatura_endgame.MainMenuActivity;
import com.example.drzavnamatura_endgame.R;
import com.example.drzavnamatura_endgame.RecyclerViewHelper.RecyclerAdapterGIC;
import com.example.drzavnamatura_endgame.Util;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.example.drzavnamatura_endgame.MainMenuActivity.bottomNavigationView;
import static com.example.drzavnamatura_endgame.MainMenuActivity.currentF;
import static com.example.drzavnamatura_endgame.MainMenuActivity.listGradiva;


public class TabGradiva extends Fragment implements RecyclerAdapterGIC.OnItemListener {

    public TabGradiva() {
        // Required empty public constructor
    }

    public static RecyclerAdapterGIC adapterGradiva;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_gradiva, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(getActivity(), "TabGradiva", Toast.LENGTH_SHORT).show();
        recyclerView = view.findViewById(R.id.recyclerViewTabGradiva);
        GradivaFragment.napuniListu(listGradiva, Util.ALL);
        adapterGradiva = new RecyclerAdapterGIC(listGradiva, true, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterGradiva);
    }

    @Override
    public void onItemClick(int position) {
        openGradivo(position);
    }

    public void openGradivo(int position) {
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        MainMenuActivity.isHomeOpened = false;
        SharedPreferences sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        sharedPreferences.edit().putString("kojiPredmet", listGradiva.get(position).getVisiOdjelPredmet()).apply();
        sharedPreferences.edit().putString("kojeGradivo", listGradiva.get(position).getNaslov()).apply();
        Log.i("PREDMET ", sharedPreferences.getString("kojiPredmet", ""));
        Log.i("GRADIVO", sharedPreferences.getString("kojeGradivo", ""));
        CjelineFragment cjelineFragment = new CjelineFragment();
        FragmentManager fragmentManager1 = currentF.getFragmentManager();
        assert fragmentManager1 != null;
        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
        //pamtiGdjeSiStao();
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.remove(currentF);
        currentF = cjelineFragment;
        fragmentTransaction1.add(R.id.fragment_container, cjelineFragment, "gradiva").commit();
    }
}
