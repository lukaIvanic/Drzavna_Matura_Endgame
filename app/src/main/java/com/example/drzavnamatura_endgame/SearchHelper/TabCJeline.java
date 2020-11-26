package com.example.drzavnamatura_endgame.SearchHelper;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drzavnamatura_endgame.MainFragments.LeckijaFragment;
import com.example.drzavnamatura_endgame.MainMenuActivity;
import com.example.drzavnamatura_endgame.R;
import com.example.drzavnamatura_endgame.RecyclerViewHelper.RecyclerAdapterGIC;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.example.drzavnamatura_endgame.MainFragments.CjelineFragment.koji_url;
import static com.example.drzavnamatura_endgame.MainFragments.CjelineFragment.lekcija;
import static com.example.drzavnamatura_endgame.MainMenuActivity.bottomNavigationView;
import static com.example.drzavnamatura_endgame.MainMenuActivity.currentF;
import static com.example.drzavnamatura_endgame.MainMenuActivity.listCjelina;


public class TabCJeline extends Fragment implements RecyclerAdapterGIC.OnItemListener {

    public static RecyclerAdapterGIC adapterCjelina;
    RecyclerView recyclerView;


    public TabCJeline() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_cjeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewTabCjeline);
        adapterCjelina = new RecyclerAdapterGIC( listCjelina, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterCjelina);

    }

    @Override
    public void onItemClick(final int position) {
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
                Log.i("KOJI URL", koji_url);
                openCjelinu(position);
                dialog.dismiss();
            }
        });
        izbor_edutorij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                koji_url = "https://edutorij.e-skole.hr/share/page/home-page";
                Log.i("KOJI URL", koji_url);
                openCjelinu(position);
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    private void openCjelinu(int position) {
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        MainMenuActivity.isHomeOpened = false;
        SharedPreferences sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        sharedPreferences.edit().putString("kojiPredmet", listCjelina.get(position).getVisiOdjelPredmet()).apply();
        sharedPreferences.edit().putString("kojeGradivo", listCjelina.get(position).getVisiOdjelGradivo()).apply();
        lekcija = listCjelina.get(position);
        LeckijaFragment lekcijaFragment = new LeckijaFragment();
        FragmentManager fragmentManager1 = currentF.getFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
        fragmentTransaction1.setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_top);
        ///  pamtiGdjeSiStao();
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.remove(currentF);
        currentF = lekcijaFragment;
        fragmentTransaction1.add(R.id.fragment_container, lekcijaFragment, "cjeline").commit();
    }
}
