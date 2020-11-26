package com.example.drzavnamatura_endgame.MainFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.drzavnamatura_endgame.R;
import com.example.drzavnamatura_endgame.SearchHelper.PagerAdapter;
import com.example.drzavnamatura_endgame.SearchHelper.TabCJeline;
import com.example.drzavnamatura_endgame.SearchHelper.TabGradiva;
import com.example.drzavnamatura_endgame.SearchHelper.TabKorisnici;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import static com.example.drzavnamatura_endgame.SearchHelper.TabCJeline.adapterCjelina;
import static com.example.drzavnamatura_endgame.SearchHelper.TabGradiva.adapterGradiva;
import static com.example.drzavnamatura_endgame.SearchHelper.TabKorisnici.adapterKorisnika;


public class SearchFragment extends Fragment {

    public static SearchView searchView;
    public static ImageView CANCEL_BACK_BUTTON;

    PagerAdapter pagerAdapter;

    public static String searchText = "";
    public static Boolean isExed = false;
    public static Boolean isKeyboardUp = false;

    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

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
        closeButton.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.white));
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.requestFocus();
                searchView.setQuery(null, false);
                isExed = true;
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
                CANCEL_BACK_BUTTON.setVisibility(View.VISIBLE);
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CANCEL_BACK_BUTTON.setVisibility(View.VISIBLE);
                isKeyboardUp = true;
            }
        });

        CANCEL_BACK_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.clearFocus();
                isKeyboardUp = false;
                CANCEL_BACK_BUTTON.setVisibility(View.GONE);
                View view2 = Objects.requireNonNull(getActivity()).getCurrentFocus();
                if (view2 != null) {
                    InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                }
            }
        });

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
