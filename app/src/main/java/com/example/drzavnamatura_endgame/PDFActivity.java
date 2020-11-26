package com.example.drzavnamatura_endgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

public class PDFActivity extends AppCompatActivity {

    PDFView pdfView;
    Boolean nightmode = false;
    ImageView imageView;
    TextView textView;
    String koja_skripta = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));
        setContentView(R.layout.activity_p_d_f__reader);

        textView = findViewById(R.id.skripta_name);

        Intent intent = getIntent();
        textView.setText(intent.getStringExtra("naziv_skripte"));

        switch (intent.getIntExtra("koja_skripta", 0)){
            case 1:
                koja_skripta = "ahrvatski_viša_razina_2013";
                break;
            case 2:
                koja_skripta = "hrv_skripta_visa_2013_knjizevnost";
                break;
            case 3:
                koja_skripta = "skripta_hrvatski";
                break;
            case 4:
                koja_skripta = "skripta_hrvatski_2";
                break;
            case 5:
                koja_skripta = "skripta_hrvatski_jezik";
                break;
            case 6:
                koja_skripta = "skripta_hrvatski_književnost";
                break;
            case 7:
                koja_skripta = "skripta_hrvatski_sareno";
                break;

        }
        int stranica = intent.getIntExtra("stranicaPDF", 0);

        imageView = findViewById(R.id.night_mode);
        pdfView = findViewById(R.id.pdfView);
        pdfView.fromAsset(koja_skripta)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(stranica)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .nightMode(false)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                .spacing(0)
                .load();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nightmode) {
                    pdfView.setNightMode(true);
                    pdfView.scrollTo(pdfView.getScrollX(), pdfView.getScrollY() + 10);
                    nightmode = true;
                } else {
                    pdfView.setNightMode(false);
                    nightmode = false;
                    pdfView.scrollTo(pdfView.getScrollX(), pdfView.getScrollY() + 10);
                }
            }
        });
    }
}