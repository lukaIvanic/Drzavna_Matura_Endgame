package com.example.drzavnamatura_endgame.RecyclerViewHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drzavnamatura_endgame.R;
import com.example.drzavnamatura_endgame.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static com.example.drzavnamatura_endgame.MainFragments.SearchFragment.searchText;

public class RecyclerAdapterUsers extends RecyclerView.Adapter<RecyclerAdapterUsers.AdapterViewHolder> {

    private List<User> listUserHelper;
    private ArrayList<User> arrayListUser;
    private OnItemListener onItemListener;


    public RecyclerAdapterUsers(ArrayList<User> list, OnItemListener onItemListener) {
        this.listUserHelper = list;
        this.arrayListUser = new ArrayList<>();
        this.arrayListUser.addAll(list);
        this.onItemListener = onItemListener;

    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView user_ime, user_score, user_score_trophy;
        ImageView user_profilna;
        OnItemListener onItemListener;

        AdapterViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            user_ime = itemView.findViewById(R.id.item_user_ime);
            user_profilna = itemView.findViewById(R.id.item_user_profilna);
            user_score = itemView.findViewById(R.id.item_user_score);
            user_score_trophy = itemView.findViewById(R.id.item_user_score_trophy);

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
        layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rec_user, viewGroup, false);
        return new AdapterViewHolder(layout, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        User user = listUserHelper.get(position);
        holder.user_ime.setText(user.getUsername());
        holder.user_score.setText(user.getScore());
        holder.user_score_trophy.setText("\uD83C\uDFC6");


        int profilePic;
        if (user.isMale()) {
            profilePic = R.drawable.avatar_male;
        } else {
            profilePic = R.drawable.avatar_female;
        }

        Picasso.get()
                .load(arrayListUser.get(position).getProfilePicUrl())
                .transform(new CropCircleTransformation())
                .placeholder(profilePic)
                .fit()
                .into(holder.user_profilna);

}

    @Override
    public int getItemCount() {
        return listUserHelper.size();
    }


    // Filter Class
    public void filterByName() {
        searchText = searchText.toLowerCase(Locale.getDefault());
        Log.i("Users filtered: ", "Char Length: " + searchText.length());
        listUserHelper.clear();
        if (searchText.length() == 0) {
            listUserHelper.addAll(arrayListUser);
        } else {
            for (User wp : arrayListUser) {
                if (wp.getUsername().toLowerCase(Locale.getDefault()).contains(searchText)) {
                    listUserHelper.add(wp);
                }
            }
        }

        notifyDataSetChanged();
    }

}


