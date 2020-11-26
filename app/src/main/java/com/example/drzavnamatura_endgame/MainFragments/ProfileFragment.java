package com.example.drzavnamatura_endgame.MainFragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.drzavnamatura_endgame.MainMenuActivity;
import com.example.drzavnamatura_endgame.R;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static com.example.drzavnamatura_endgame.MainActivity.user;


public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ImageView profilePictureImageView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    private TextView username_profileFragment;
    private TextView email_profileFragment;

    private Button settingsButton;


    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        username_profileFragment = view.findViewById(R.id.username_textView_profile);
        email_profileFragment = view.findViewById(R.id.email_textView_profile);
        settingsButton = view.findViewById(R.id.profle_settings_button);
        profilePictureImageView = view.findViewById(R.id.slikaProfila);

        int male_or_female_pic;
        if(user.isMale()){
            male_or_female_pic = R.drawable.avatar_male;
        }else {
            male_or_female_pic = R.drawable.avatar_female;
        }


        Picasso.get()
                .load(user.getProfilePicUrl())
                .transform(new CropCircleTransformation())
                .placeholder(male_or_female_pic)
                .fit()
                .into(profilePictureImageView);

        username_profileFragment.setText(user.getUsername() + "\n" + user.getScore() + "\uD83C\uDFC6");


        email_profileFragment.setText(MainMenuActivity.currentUserEmail);


        super.onViewCreated(view, savedInstanceState);
    }


}
