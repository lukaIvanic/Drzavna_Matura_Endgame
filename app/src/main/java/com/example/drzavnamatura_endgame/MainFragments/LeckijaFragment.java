package com.example.drzavnamatura_endgame.MainFragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.drzavnamatura_endgame.Lekcija;
import com.example.drzavnamatura_endgame.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.example.drzavnamatura_endgame.MainFragments.CjelineFragment.koji_url;
import static com.example.drzavnamatura_endgame.MainFragments.CjelineFragment.lekcija;

public class LeckijaFragment extends Fragment {

    WebView webView;

    public LeckijaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //AKO SE DOLAZI IZ CJELINA FRAGMENT ONDA SAVEA LEKCIJU, A U BILO KOJEM SLUCAJU LOADIRA LEKCIJU IZ SHARED PREFSA
        SharedPreferences sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);

        //SAVEANJE
        if (koji_url != null) {
            sharedPreferences.edit().remove("lekcija_url").apply();
            sharedPreferences.edit().putString("lekcija_url", koji_url).apply();
        }

        return inflater.inflate(R.layout.lekcija_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView= view.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        ;webSettings.getAllowFileAccess();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(koji_url);
        SharedPreferences sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        sharedPreferences.edit().remove("kojiFragment").apply();
        sharedPreferences.edit().putInt("kojiFragment", 3).apply();


    }
}
