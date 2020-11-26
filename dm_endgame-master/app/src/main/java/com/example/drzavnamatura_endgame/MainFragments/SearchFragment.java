package com.example.drzavnamatura_endgame.MainFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.drzavnamatura_endgame.Gradivo;
import com.example.drzavnamatura_endgame.R;
import com.example.drzavnamatura_endgame.SearchHelper.PagerAdapter;
import com.example.drzavnamatura_endgame.SearchHelper.SpinnerAdapter;
import com.example.drzavnamatura_endgame.SearchHelper.TabCJeline;
import com.example.drzavnamatura_endgame.SearchHelper.TabGradiva;
import com.example.drzavnamatura_endgame.SearchHelper.TabKorisnici;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.example.drzavnamatura_endgame.MainActivity.allUsers;
import static com.example.drzavnamatura_endgame.MainMenuActivity.bottomNavigationView;
import static com.example.drzavnamatura_endgame.MainMenuActivity.currentF;
import static com.example.drzavnamatura_endgame.MainMenuActivity.listGradiva;
import static com.example.drzavnamatura_endgame.MainMenuActivity.listCjelina;
import static com.example.drzavnamatura_endgame.SearchHelper.TabCJeline.adapterCjelina;
import static com.example.drzavnamatura_endgame.SearchHelper.TabGradiva.adapterGradiva;
import static com.example.drzavnamatura_endgame.SearchHelper.TabKorisnici.adapterKorisnika;


public class SearchFragment extends Fragment {

    ArrayList<String> optionsList;

    SpinnerAdapter sortOptionsAdapter;
    public static SearchView searchView;
    public static Spinner spinner;
    public static ImageView CANCEL_BACK_BUTTON;

    PagerAdapter pagerAdapter;

    public static String searchText = "";

    public static Boolean isKeyboardUp = false;

    ViewPager viewPager;
    TabLayout tabLayout;


    public SearchFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


        CANCEL_BACK_BUTTON = view.findViewById(R.id.cancelBackButton);

        searchView = (SearchView) view.findViewById(R.id.searchSearch);

        allSearchViewThings();


        /*optionsList = new ArrayList<>();
        optionsList.add("Normal");
        optionsList.add("Reverse");
        optionsList.add("A - Z");
        optionsList.add("Z - A");
        spinner = (Spinner) view.findViewById(R.id.sortSpinnerSearch);
        sortOptionsAdapter = new SpinnerAdapter(Objects.requireNonNull(getContext()), optionsList);
        spinner.setAdapter(sortOptionsAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              *//*  switch (position) {
                    case 0:
                        sortDefault();
                        break;
                    case 1:
                        sortByReverse();
                        break;
                    case 2:
                        sortByAToZ();
                        break;
                    case 3:
                        sortbyZToA();
                        break;
                }*//*
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
    }

    private void setupViewPager(ViewPager viewPager) {
        pagerAdapter = new PagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragment(new TabGradiva(), "Gradiva");
        pagerAdapter.addFragment(new TabCJeline(), "Cjeline");
        pagerAdapter.addFragment(new TabKorisnici(), "Korisnici");
        viewPager.setAdapter(pagerAdapter);

    }



    private void allSearchViewThings() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;
                //spinner.setSelection(0);
               /* if (newText.length() > 0) {
                    spinner.setEnabled(false);
                } else {
                    spinner.setEnabled(true);
                }*/
                adapterGradiva.filterByNaslov(newText);
                adapterCjelina.filterByNaslov(newText);
                if (adapterKorisnika != null) {
                    adapterKorisnika.filterByName();
                }
                return false;
            }
        });

        int searchCloseButtonId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = searchView.findViewById(searchCloseButtonId);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.requestFocus();
                searchView.setQuery(null, false);
            }
        });

        //DESIGN LOL
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);
        if (searchPlate != null) {
            searchPlate.setBackgroundColor(Color.TRANSPARENT);
        }

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //spinner.setVisibility(View.VISIBLE);
                CANCEL_BACK_BUTTON.setVisibility(View.VISIBLE);
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //spinner.setVisibility(View.VISIBLE);
                CANCEL_BACK_BUTTON.setVisibility(View.VISIBLE);
                isKeyboardUp = true;
            }
        });

        CANCEL_BACK_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.clearFocus();
                isKeyboardUp = false;
                //spinner.setVisibility(View.GONE);
                CANCEL_BACK_BUTTON.setVisibility(View.GONE);
                View view2 = getActivity().getCurrentFocus();
                if (view2 != null) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                }
            }
        });

    }

    /*private void sortByReverse() {
        sortDefault();
        Collections.reverse(listGradiva);
    }

    private void sortByAToZ() {
        sortDefault();
        Collections.sort(listGradiva, new Comparator<Gradivo>() {
            public int compare(Gradivo obj1, Gradivo obj2) {
                // ## Ascending order
                return obj1.getNaslov().compareToIgnoreCase(obj2.getNaslov()); // To compare string values
                // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
            }
        });
    }

    private void sortbyZToA() {
        sortByAToZ();
        Collections.reverse(listGradiva);
    }

    private void sortDefault() {
        GradivaFragment.napuniListu(listGradiva, "all");
    }

*/
    private void openCjeline(int position) {
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        SharedPreferences sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        sharedPreferences.edit().putString("kojiPredmet", listGradiva.get(position).getVisiOdjelPredmet()).apply();
        CjelineFragment cjelineFragment = new CjelineFragment();
        FragmentManager fragmentManager1 = getFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
        ///  pamtiGdjeSiStao();
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.remove(currentF);
        currentF = cjelineFragment;
        fragmentTransaction1.add(R.id.fragment_container, cjelineFragment, "cjeline").commit();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        adapterGradiva.resetLista(0);
        adapterCjelina.resetLista(1);
        //OVO RESETIRA KORISNIKE
        searchText = "";

    }


}
