package com.example.drzavnamatura_endgame.RecyclerViewHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drzavnamatura_endgame.Komentar;
import com.example.drzavnamatura_endgame.R;

import java.util.ArrayList;

public class RecyclerAdapterKomentara extends RecyclerView.Adapter<RecyclerAdapterKomentara.AdapterViewHolder> {

    ArrayList<Komentar> arrayListKomentara;

    public RecyclerAdapterKomentara(ArrayList<Komentar> arrayListKomentara) {
        this.arrayListKomentara = arrayListKomentara;
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        TextView autor, sadrzaj, timestamp;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            autor = itemView.findViewById(R.id.textViewAutor);
            sadrzaj = itemView.findViewById(R.id.textViewKomentar);
            timestamp = itemView.findViewById(R.id.timeStamp);

        }

    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rec_komentar, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        int realPosition = holder.getAdapterPosition();
        Komentar komentar = arrayListKomentara.get(realPosition);
        holder.autor.setText(komentar.getAutor());
        holder.sadrzaj.setText(komentar.getKomentar());
        int minutes = komentar.getSeconds()/60;
        int hours = minutes / 60;
        minutes %= 60;
        String minutesT = minutes + "";
        String hoursT = hours + "";
        if(minutes<10){
            minutesT = "0" + minutesT;
        }
        if (hours<10){
            hoursT = "0" + hoursT;
        }
        holder.timestamp.setText(hoursT + ":" + minutesT);
    }


    @Override
    public int getItemCount() {
        return arrayListKomentara.size();
    }

}
