package com.example.drzavnamatura_endgame.RecyclerViewHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drzavnamatura_endgame.Gradivo;
import com.example.drzavnamatura_endgame.Lekcija;
import com.example.drzavnamatura_endgame.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

public class RecyclerAdapterGIC extends RecyclerView.Adapter<RecyclerAdapterGIC.AdapterViewHolder> {

    ArrayList<Lekcija> arrayListLekcija;
    List<Lekcija> listLekcijaHelper;
    ArrayList<Gradivo> arrayListGradiva;
    List<Gradivo> listGradivaHelper;
    Boolean isGradivo = false;
    OnItemListener onItemListener;

    public RecyclerAdapterGIC(ArrayList<Lekcija> arrayListLekcija, OnItemListener onItemListener) {
        this.arrayListLekcija = arrayListLekcija;
        listLekcijaHelper = new ArrayList<>();
        listLekcijaHelper.addAll(arrayListLekcija);
        this.onItemListener = onItemListener;
    }

    public RecyclerAdapterGIC(ArrayList<Gradivo> arrayListGradiva, Boolean isGradivo, OnItemListener onItemListener) {
        this.arrayListGradiva = arrayListGradiva;
        listGradivaHelper = new ArrayList<>();
        listGradivaHelper.addAll(arrayListGradiva);
        this.isGradivo = isGradivo;
        this.onItemListener = onItemListener;

    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {


        TextView naslovCjeline, predmetCjeline, gradivoCjeline, razredCjeline;
        TextView naslovGradiva, predmetGradiva, razredGradiva;
        View lilDot;
        OnItemListener onItemListener;

        AdapterViewHolder(@Nonnull View itemview, OnItemListener onItemListener) {
            super(itemview);
            try {
                naslovCjeline = itemview.findViewById(R.id.item_cjelina_naslov);
                predmetCjeline = itemview.findViewById(R.id.item_cjelina_predmet);
                gradivoCjeline = itemview.findViewById(R.id.item_cjelina_gradivo);
                razredCjeline = itemview.findViewById(R.id.item_cjelina_razred);
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                naslovGradiva = itemview.findViewById(R.id.item_gradivo_naslov);
                predmetGradiva = itemview.findViewById(R.id.item_gradivo_predmet);
                razredGradiva = itemview.findViewById(R.id.item_gradivo_razred);
                lilDot = itemview.findViewById(R.id.lilDot);
            }catch (Exception e){
                e.printStackTrace();
            }
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }
    public interface OnItemListener{
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout;
        if (isGradivo){
            layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rec_gradivo, parent, false);
        }else {
            layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rec_cjelina, parent, false);
        }
        return new AdapterViewHolder(layout, onItemListener);
    }


    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        if (isGradivo) {
            holder.naslovGradiva.setText(arrayListGradiva.get(position).getNaslov());
            holder.razredGradiva.setText(arrayListGradiva.get(position).getRazred());
            holder.predmetGradiva.setText(arrayListGradiva.get(position).getVisiOdjelPredmet());
            if(arrayListGradiva.get(position).getNaslov().contains("Skripta")){
                holder.lilDot.setVisibility(View.INVISIBLE);
            }
        }else{
            holder.naslovCjeline.setText(arrayListLekcija.get(position).getNaslov());
            holder.gradivoCjeline.setText(arrayListLekcija.get(position).getVisiOdjelGradivo());
            holder.predmetCjeline.setText(arrayListLekcija.get(position).getVisiOdjelPredmet());
            holder.razredCjeline.setText(arrayListLekcija.get(position).getRazred());
        }
    }

    @Override
    public int getItemCount() {
        if (isGradivo){
            return  arrayListGradiva.size();
        }else{
            return arrayListLekcija.size();
        }

    }

    public void filterByNaslov(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        if (isGradivo) {
            arrayListGradiva.clear();
            if (charText.length() == 0) {
                arrayListGradiva.addAll(listGradivaHelper);
            } else {
                for (Gradivo gradivo : listGradivaHelper) {
                    if (gradivo.getNaslov().toLowerCase(Locale.getDefault()).contains(charText)) {
                        arrayListGradiva.add(gradivo);
                    }
                }
            }

        }else{
            arrayListLekcija.clear();
            if (charText.equals("")) {
                arrayListLekcija.addAll(listLekcijaHelper);
            } else {
                for (Lekcija lekcija : listLekcijaHelper) {
                    if (lekcija.getNaslov().toLowerCase(Locale.getDefault()).contains(charText)) {
                        arrayListLekcija.add(lekcija);
                    }
                }
            }
        }

        notifyDataSetChanged();

    }

    public void resetLista(int x){
        if(x == 0){
            arrayListGradiva.clear();
            arrayListGradiva.addAll(listGradivaHelper);
        }else{
            arrayListLekcija.clear();
            arrayListLekcija.addAll(listLekcijaHelper);
        }

    }
}
