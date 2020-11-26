package com.example.drzavnamatura_endgame.RecyclerViewHelper;

import android.graphics.drawable.Icon;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drzavnamatura_endgame.Icons;
import com.example.drzavnamatura_endgame.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class RecyclerAdapterIcons extends RecyclerView.Adapter<RecyclerAdapterIcons.AdapterViewHolder> {


    private ArrayList<Icons> arrayListIcons;
    private OnItemListener onItemListener;


    public RecyclerAdapterIcons(ArrayList<Icons> list, OnItemListener onItemListener) {
        arrayListIcons = list;
        this.onItemListener = onItemListener;

    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView icon_price;
        ImageView icon_image;
        OnItemListener onItemListener;

        AdapterViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            icon_image = itemView.findViewById(R.id.iconImageShop);
            icon_price = itemView.findViewById(R.id.iconPriceShop);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }


    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View layout;
        layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rec_icon, viewGroup, false);
        return new AdapterViewHolder(layout, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        holder.icon_price.setText(arrayListIcons.get(position).getPrice() + "");

        if (!arrayListIcons.get(position).getIconPicUrl().equals("www.picexample.com")) {

            Picasso.get()
                    .load(arrayListIcons.get(position).getIconPicUrl())
                    .transform(new CropCircleTransformation())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.icon_image);
        } else {
            Picasso.get()
                    .load(R.drawable.ic_launcher_foreground)
                    .transform(new CropCircleTransformation())
                    .into(holder.icon_image);
        }
    }

    @Override
    public int getItemCount() {
        return arrayListIcons.size();
    }


}


