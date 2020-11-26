package com.example.drzavnamatura_endgame.MainFragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drzavnamatura_endgame.Komentar;
import com.example.drzavnamatura_endgame.MainMenuActivity;
import com.example.drzavnamatura_endgame.R;
import com.example.drzavnamatura_endgame.RecyclerViewHelper.RecyclerAdapterKomentara;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static android.content.Context.MODE_PRIVATE;
import static com.example.drzavnamatura_endgame.MainMenuActivity.currentUserUsername;

public class HomeFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference commentsCollectionReference = db.collection("Comments");
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    TextView welcomeTV;
    ImageButton imageButton;
    public static  RecyclerAdapterKomentara recyclerAdapterKomentara;
    public static RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    ArrayList<Komentar> komentariArrayList;
    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        imageButton = view.findViewById(R.id.menu_button);
        welcomeTV = view.findViewById(R.id.textView2);
        welcomeTV.setText("Dobrodošao,\n" + currentUserUsername);
        SharedPreferences sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("com.example.drzavnamatura_endgame", MODE_PRIVATE);
        sharedPreferences.edit().remove("kojiFragment").apply();
        sharedPreferences.edit().putInt("kojiFragment: ", 0).apply();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                popupMenu.setOnMenuItemClickListener(HomeFragment.this);
                popupMenu.inflate(R.menu.main_menu);
                popupMenu.show();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.buttonMatura420:
                final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
                dialog.setContentView(R.layout.dialog_rasprava);
                dialog.setTitle("Rasprava");

                recyclerView = dialog.findViewById(R.id.rec_view_komentara);

                final EditText editTextKomentarDesc = dialog.findViewById(R.id.edit_text_komentar_desc);
                final Button buttonPostComment = dialog.findViewById(R.id.buttonPostComment);
                komentariArrayList = new ArrayList<>();
                komentariArrayList = MainMenuActivity.komentari;
                recyclerAdapterKomentara = new RecyclerAdapterKomentara(komentariArrayList);
                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                        DividerItemDecoration.HORIZONTAL));
                linearLayoutManager = new LinearLayoutManager(getContext()){
                    @Override
                    public boolean supportsPredictiveItemAnimations() {
                        return false;
                    }
                };
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(recyclerAdapterKomentara);


                editTextKomentarDesc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if(actionId == EditorInfo.IME_ACTION_SEND){
                            buttonPostComment.performClick();
                            return true;
                        }
                        return false;
                    }
                });

                recyclerView.smoothScrollToPosition(komentariArrayList.size());

                buttonPostComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String komentar = editTextKomentarDesc.getText().toString().trim();
                        if (!komentar.isEmpty()) {
                            ZonedDateTime nowZoned = ZonedDateTime.now();
                            Instant midnight = nowZoned.toLocalDate().atStartOfDay(nowZoned.getZone()).toInstant();
                            Duration duration = Duration.between(midnight, Instant.now());
                            int seconds = (int) duration.getSeconds();
                            Map<String, Object> more = new HashMap<>();
                            more.put("autor", currentUser.getDisplayName());
                            more.put("komentar", komentar);
                            more.put("seconds", seconds);
                            /*more.put("hour", Timestamp.now().toDate().getHours());
                            more.put("minute", Timestamp.now().toDate().getMinutes());
                            more.put("seconds", (int)Timestamp.now().getSeconds());*/

                            commentsCollectionReference.add(more).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    editTextKomentarDesc.getText().clear();
                                    recyclerAdapterKomentara.notifyDataSetChanged();
                                    recyclerView.smoothScrollToPosition(komentariArrayList.size());
                                }
                            });

                        }
                    }
                });
                dialog.show();
                return true;
            default:
                return false;
        }

    }
}