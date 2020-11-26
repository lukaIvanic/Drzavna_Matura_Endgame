package com.example.drzavnamatura_endgame.RecyclerViewHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drzavnamatura_endgame.InviteActvitiy;
import com.example.drzavnamatura_endgame.R;
import com.example.drzavnamatura_endgame.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static com.example.drzavnamatura_endgame.MainFragments.SearchFragment.searchText;

public class InviteRecyclerAdapter extends RecyclerView.Adapter<InviteRecyclerAdapter.InviteHolderAdapter> {


    private List<User> listUserHelper;
    private ArrayList<User> arrayListUser;
    private ArrayList<Boolean> arrayListUserChecked;
    //private OnItemListener onItemListener;


    public InviteRecyclerAdapter(ArrayList<User> list) {
        this.listUserHelper = list;
        this.arrayListUser = new ArrayList<>();
        arrayListUserChecked = new ArrayList<>();
        this.arrayListUser.addAll(list);
        for (User user : arrayListUser) {
            arrayListUserChecked.add(false);
        }
        //this.onItemListener = onItemListener;

    }

    public static class InviteHolderAdapter extends RecyclerView.ViewHolder{

        TextView user_ime, user_score, user_score_trophy;
        ImageView user_profilna, inviteButton;
        //OnItemListener onItemListener;

        InviteHolderAdapter(@NonNull View itemView) {
            super(itemView);
            user_ime = itemView.findViewById(R.id.userInviteName);
            user_score = itemView.findViewById(R.id.userInviteScore);
            inviteButton = itemView.findViewById(R.id.inviteImageView);

            //this.onItemListener = onItemListener;
            //itemView.setOnClickListener(this);


        }

        /*@Override
        public void onClick(View v) {
            onItemListener.onInviteClick(getAdapterPosition(), inviteButton);
        }*/
    }

    /*public interface OnItemListener {
        void onInviteClick(int position, ImageView inviteButton);
    }*/


    @NonNull
    @Override
    public InviteHolderAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View layout;
        layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_people_invite, viewGroup, false);
        return new InviteHolderAdapter(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteHolderAdapter holder, int position) {
        User user = listUserHelper.get(position);
        holder.user_ime.setText(user.getUsername());
        holder.user_score.setText(user.getScore());
        try {
            if (arrayListUserChecked.get(holder.getAdapterPosition())) {
                holder.inviteButton.setImageResource(R.drawable.ic_user_invited_icon);
            } else {
                holder.inviteButton.setImageResource(R.drawable.ic_invite_user_icon);
            }
        }catch (Exception e){

        }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InviteActvitiy.enableButton();
                    InviteActvitiy.invitedArrayList.set(holder.getAdapterPosition(), true);
                    arrayListUserChecked.set(holder.getAdapterPosition(), true);
                    holder.inviteButton.setImageResource(R.drawable.ic_user_invited_icon);
                }
            });


        int profilePic;
        if (user.isMale()) {
            profilePic = R.drawable.avatar_male;
        } else {
            profilePic = R.drawable.avatar_female;
        }

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
