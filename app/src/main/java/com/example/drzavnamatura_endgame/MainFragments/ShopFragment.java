package com.example.drzavnamatura_endgame.MainFragments;

import android.graphics.drawable.Icon;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.drzavnamatura_endgame.Icons;
import com.example.drzavnamatura_endgame.R;
import com.example.drzavnamatura_endgame.RecyclerViewHelper.RecyclerAdapterIcons;

import java.util.ArrayList;

public class ShopFragment extends Fragment implements RecyclerAdapterIcons.OnItemListener{

    RecyclerView recyclerView;
    RecyclerAdapterIcons recyclerAdapterIcons;
    ArrayList<Icons>arrayListIcons;

    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_dolazi_uskoro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*recyclerView = view.findViewById(R.id.rec_view_shop);
        arrayListIcons = new ArrayList<>();
        arrayListIcons.add(new Icons(200, "www.picexample.com", false));
        arrayListIcons.add(new Icons(200, "https://www.biography.com/.image/t_share/MTY2MzU3Nzk2OTM2MjMwNTkx/elon_musk_royal_society.jpg", false));
        arrayListIcons.add(new Icons(200, "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQJp_d6sVqa9G-oMH1ylbuFJ8zql40qvm2yNSNjGkCiTRmBlkgR&usqp=CAU", false));
        arrayListIcons.add(new Icons(200, "https://lh3.googleusercontent.com/proxy/0mqcwrOJzwqKXPecB2DyFG_DE9XcF6-SK4UTukUMUtGnf9UFD8DeBUh5QlqTP4MvUTnHlPQmlY7B_NeGOOg-1jwEwfdCQQZ2hgdcKI9_W9dZGtdu_Tl0SZGts1yuJg", false));
        arrayListIcons.add(new Icons(200, "https://i.pinimg.com/originals/7b/aa/25/7baa252dbdfeed669c152bedd2fa5feb.jpg", false));
        arrayListIcons.add(new Icons(200, "https://rickandmortyapi.com/api/character/avatar/2.jpeg", false));
        arrayListIcons.add(new Icons(200, "www.picexample.com", false));
        arrayListIcons.add(new Icons(200, "www.picexample.com", false));
        arrayListIcons.add(new Icons(200, "www.picexample.com", false));
        arrayListIcons.add(new Icons(200, "www.picexample.com", false));
        arrayListIcons.add(new Icons(200, "www.picexample.com", false));
        recyclerAdapterIcons = new RecyclerAdapterIcons(arrayListIcons, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setAdapter(recyclerAdapterIcons);
        recyclerView.setLayoutManager(gridLayoutManager);*/


    }

    @Override
    public void onItemClick(int position) {
        //Toast.makeText(getContext(), arrayListIcons.get(position).getIconPicUrl(), Toast.LENGTH_SHORT).show();
    }
}
