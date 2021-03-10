package com.example.drzavnamatura_endgame.RecyclerViewHelper;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drzavnamatura_endgame.Ispit;
import com.example.drzavnamatura_endgame.R;
import com.example.drzavnamatura_endgame.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapterIspita extends RecyclerView.Adapter<RecyclerAdapterIspita.AdapterViewHolder> {

    ArrayList<Ispit> ispitArrayList;
    private onIspitListener onIspitListener;
    int vrstaIspita;

    public RecyclerAdapterIspita(ArrayList<Ispit> ispitArrayList, onIspitListener onIspitListener, int vrstaIspita) {
        this.ispitArrayList = ispitArrayList;
        this.onIspitListener = onIspitListener;
        this.vrstaIspita = vrstaIspita;
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView razina, datum;
        ImageView imageViewIconIspita;
        onIspitListener onIspitListener;
        int vrstaIspita;

        public AdapterViewHolder(@NonNull View itemView, onIspitListener onIspitListener, int vrstaIspita) {
            super(itemView);
            razina = itemView.findViewById(R.id.item_ispit_razina);
            datum = itemView.findViewById(R.id.item_ispit_datum);
            imageViewIconIspita = itemView.findViewById(R.id.item_ispit_icon);
            this.onIspitListener = onIspitListener;
            itemView.setOnClickListener(this);
            this.vrstaIspita = vrstaIspita;

        }

        @Override
        public void onClick(View v) {
            onIspitListener.onIspitClick(getAdapterPosition(), vrstaIspita);
        }
    }
    public interface onIspitListener {
        void onIspitClick(int position, int vrstaIspita);
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rec_ispit, parent, false);
        return new AdapterViewHolder(view, onIspitListener, vrstaIspita);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Ispit ispit = ispitArrayList.get(position);
        holder.datum.setText(ispit.getDatum());
        holder.razina.setText(ispit.getRazina() + " ");
        int picture = 0;
        switch (ispitArrayList.get(position).getPredmet()){
            case Util.VRSTA_HRVATSKI:
                picture = R.drawable.ic_hrv_ispit;
                break;
            case Util.VRSTA_MATEMATIKA:
                picture = R.drawable.ic_mat_ispit;
                break;
        }
        holder.imageViewIconIspita.setBackgroundResource(picture);
    }


    @Override
    public int getItemCount() {
        return ispitArrayList.size();
    }


}

