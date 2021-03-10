package com.example.drzavnamatura_endgame.RecyclerViewHelper;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drzavnamatura_endgame.Pitanje;
import com.example.drzavnamatura_endgame.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.drzavnamatura_endgame.IspitActivity.allAnswersChecked;
import static com.example.drzavnamatura_endgame.IspitActivity.correct_answers;

public class RecyclerAdapterPitanja extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Pitanje> arrayListPitanja;
    Context context;
    private static int OPTIONS_TYPE = 1, INPUT_TYPE = 2;
    onImageListener onImageListener;

    public RecyclerAdapterPitanja(ArrayList<Pitanje> arrayListPitanja, Context context, onImageListener onImageListener) {
        this.arrayListPitanja = arrayListPitanja;
        this.context = context;
        this.onImageListener = onImageListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayListPitanja.get(position).getTocanOdgovorString().equals("nope")) {
            return OPTIONS_TYPE;
        } else {
            return INPUT_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == OPTIONS_TYPE) {
            return new AdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pitanje_slika_izbor, parent, false), onImageListener);
        } else {
            return new InputAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pitanje_user_input, parent, false), onImageListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder_, int position) {
        if (arrayListPitanja.get(position).getTocanOdgovorString().equals("nope")) {
            AdapterViewHolder holder = (AdapterViewHolder) holder_;
            holder.pitanje.setText(arrayListPitanja.get(holder.getAdapterPosition()).getPitanje());
            holder.odg1.setText(arrayListPitanja.get(holder.getAdapterPosition()).getOdg1() + " ");
            holder.odg2.setText(arrayListPitanja.get(holder.getAdapterPosition()).getOdg2() + " ");
            holder.odg3.setText(arrayListPitanja.get(holder.getAdapterPosition()).getOdg3() + " ");
            holder.odg4.setText(arrayListPitanja.get(holder.getAdapterPosition()).getOdg4() + " ");
            if(arrayListPitanja.get(holder.getAdapterPosition()).getUrl()!=null && !arrayListPitanja.get(holder.getAdapterPosition()).getUrl().equals("")) {
                Picasso.get().load(arrayListPitanja.get(holder.getAdapterPosition()).getUrl()).into(holder.slika_pitanja);
            }

            holder.koje_pitanje_redni_broj.setText((holder.getAdapterPosition() + 1) + ". ");

            holder.setOdgovora.check(arrayListPitanja.get(holder.getAdapterPosition()).getKojiOdgovor());

            holder.setOdgovora.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    arrayListPitanja.get(holder.getAdapterPosition()).setKojiOdgovor(checkedId);
                    int koji_odgovor = 0;
                    if (checkedId == holder.odg1.getId()) {
                        koji_odgovor = 1;
                    } else if (checkedId == holder.odg2.getId()) {
                        koji_odgovor = 2;
                    } else if (checkedId == holder.odg3.getId()) {
                        koji_odgovor = 3;
                    } else if (checkedId == holder.odg4.getId()) {
                        koji_odgovor = 4;
                    }
                    if (koji_odgovor == arrayListPitanja.get(holder.getAdapterPosition()).getTocanOdgovor()) {
                        arrayListPitanja.get(holder.getAdapterPosition()).setIsTocno(true);
                    } else {
                        arrayListPitanja.get(holder.getAdapterPosition()).setIsTocno(false);
                    }
                    arrayListPitanja.get(holder.getAdapterPosition()).setKojiOdgovor(checkedId);
                }
            });

            if (allAnswersChecked) {

                holder.solution.setVisibility(View.VISIBLE);


//                    isAnswerChecked.set(position, true);
                holder.odg1.setBackgroundColor(Color.WHITE);
                holder.odg2.setBackgroundColor(Color.WHITE);
                holder.odg3.setBackgroundColor(Color.WHITE);
                holder.odg4.setBackgroundColor(Color.WHITE);
                if (arrayListPitanja.get(position).getKojiOdgovor() == holder.odg1.getId()) {
                    holder.odg1.setBackgroundResource(R.drawable.ic_background_wrong);
                } else if (arrayListPitanja.get(position).getKojiOdgovor() == holder.odg2.getId()) {
                    holder.odg2.setBackgroundResource(R.drawable.ic_background_wrong);
                } else if (arrayListPitanja.get(position).getKojiOdgovor() == holder.odg3.getId()) {
                    holder.odg3.setBackgroundResource(R.drawable.ic_background_wrong);
                } else if (arrayListPitanja.get(position).getKojiOdgovor() == holder.odg4.getId()) {
                    holder.odg4.setBackgroundResource(R.drawable.ic_background_wrong);
                } else {
                    holder.pitanje.setText(holder.pitanje.getText() + "\n(Nije odgovoreno)");
                }

                switch (arrayListPitanja.get(position).getTocanOdgovor()) {
                    case 1:
                        holder.odg1.setBackgroundResource(R.drawable.ic_background_right);
                        break;
                    case 2:
                        holder.odg2.setBackgroundResource(R.drawable.ic_background_right);
                        break;
                    case 3:
                        holder.odg3.setBackgroundResource(R.drawable.ic_background_right);
                        break;
                    case 4:
                        holder.odg4.setBackgroundResource(R.drawable.ic_background_right);
                        break;
                }
                holder.odg1.setEnabled(false);
                holder.odg2.setEnabled(false);
                holder.odg3.setEnabled(false);
                holder.odg4.setEnabled(false);

            }
        } else {
            InputAdapterViewHolder holder = (InputAdapterViewHolder) holder_;

            holder.user_input.setText(arrayListPitanja.get(holder.getAdapterPosition()).getUser_odgovor());
            holder.slika_pitanja.setTag(arrayListPitanja.get(holder.getAdapterPosition()).getUrl());

            holder.pitanje.setText(arrayListPitanja.get(holder.getAdapterPosition()).getPitanje());
            holder.koje_pitanje_redni_broj.setText((position + 1) + ". ");

            if (!arrayListPitanja.get(holder.getAdapterPosition()).getUrl().equals("") && arrayListPitanja.get(holder.getAdapterPosition()).getUrl() != null) {
                Log.i("'Path adapter'", holder.getAdapterPosition() + ". " + arrayListPitanja.get(holder.getAdapterPosition()).getUrl());
                Picasso.get().load(holder.slika_pitanja.getTag().toString()).into(holder.slika_pitanja);
            }else{
                holder.slika_pitanja.setImageResource(0);
                holder.slika_pitanja.setEnabled(false);
            }

            if (allAnswersChecked) {
                holder.solution.setVisibility(View.VISIBLE);
                holder.tocanOdgTxt.setVisibility(View.VISIBLE);
                holder.tocanOdgTxt.setText("*Tocan odgovor je: " + arrayListPitanja.get(holder.getAdapterPosition()).getTocanOdgovorString());
                if (holder.user_input.getText().toString().equals("")) {
                    holder.user_input.setHint("Nije odgovoreno");
                }
                arrayListPitanja.get(holder.getAdapterPosition()).setUser_odgovor(holder.user_input.getText().toString().trim());
                if (arrayListPitanja.get(holder.getAdapterPosition()).getUser_odgovor().equals(arrayListPitanja.get(holder.getAdapterPosition()).getTocanOdgovorString())) {
                    holder.user_input.setBackgroundResource(R.drawable.ic_background_right);
                } else {
                    holder.user_input.setBackgroundResource(R.drawable.ic_background_wrong);
                }
                holder.user_input.setEnabled(false);
                holder.solution.setVisibility(View.VISIBLE);
            }
        }

    }

    public int get_correct_count() {
        correct_answers = 0;
        for (Pitanje pitanje : arrayListPitanja) {
            if (pitanje.getIsTocno()) {
                correct_answers++;
            }
        }
        return correct_answers;
    }

    public int get_postotak() {
        float postotak = ((float) get_correct_count() / (float) getItemCount());
        return (int) (postotak * 100);
    }

    public String get_ocjena() {
        int posto = get_postotak();
        if (posto < 37) {
            return "Nedovoljan (1)";
        } else if (posto < 53) {
            return "Dovoljan (2)";
        } else if (posto < 70) {
            return "Dobar (3)";
        } else if (posto < 82) {
            return "Vrlo Dobar (4)";
        } else {
            return "Odličan (5)";
        }
    }

    @Override
    public int getItemCount() {
        return arrayListPitanja.size();
    }


    public static class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView pitanje, koje_pitanje_redni_broj;
        RadioGroup setOdgovora;
        RadioButton odg1, odg2, odg3, odg4;
        ImageView slika_pitanja;
        Button solution;
        onImageListener onImageListener;

        public AdapterViewHolder(@NonNull View itemView, onImageListener onImageListener) {
            super(itemView);
            pitanje = itemView.findViewById(R.id.pitanje_textview);
            koje_pitanje_redni_broj = itemView.findViewById(R.id.koje_pitanje_broj_textview);
            setOdgovora = itemView.findViewById(R.id.radioGroupOdgovora);
            odg1 = itemView.findViewById(R.id.radio_button_odg1);
            odg2 = itemView.findViewById(R.id.radio_button_odg2);
            odg3 = itemView.findViewById(R.id.radio_button_odg3);
            odg4 = itemView.findViewById(R.id.radio_button_odg4);
            slika_pitanja = itemView.findViewById(R.id.pitanje_slika);
            solution = itemView.findViewById(R.id.button_solution);
            this.onImageListener = onImageListener;
            slika_pitanja.setOnClickListener(this);
            solution.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getClass().toString().equals("class androidx.appcompat.widget.AppCompatImageView")) {
                onImageListener.onImageClick(getAdapterPosition());
            } else {
                onImageListener.onSolutionClick(getAdapterPosition());
            }
        }


    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Log.i("CREATED", " YES");
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder.getItemViewType() == INPUT_TYPE) {
            if (holder.getAdapterPosition() >= 0 && holder.getAdapterPosition() < arrayListPitanja.size()) {
                arrayListPitanja.get(holder.getAdapterPosition()).setUser_odgovor(((InputAdapterViewHolder) holder).user_input.getText().toString());
                arrayListPitanja.get(holder.getAdapterPosition()).setUrl(((InputAdapterViewHolder)holder).slika_pitanja.getTag().toString());
            }
        }
    }

    public static class InputAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView pitanje, koje_pitanje_redni_broj, tocanOdgTxt;
        EditText user_input;
        ImageView slika_pitanja;
        Button solution;
        onImageListener onImageListener;

        public InputAdapterViewHolder(@NonNull View itemView, onImageListener onImageListener) {
            super(itemView);
            pitanje = itemView.findViewById(R.id.pitanje_textview);
            koje_pitanje_redni_broj = itemView.findViewById(R.id.koje_pitanje_broj_textview);
            slika_pitanja = itemView.findViewById(R.id.pitanje_slika);
            solution = itemView.findViewById(R.id.button_solution);
            user_input = itemView.findViewById(R.id.user_input);
            tocanOdgTxt = itemView.findViewById(R.id.tocanOdgTextView);
            this.onImageListener = onImageListener;
            slika_pitanja.setOnClickListener(this);
            solution.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getClass().toString().equals("class androidx.appcompat.widget.AppCompatImageView")) {
                onImageListener.onImageClick(getAdapterPosition());
            } else {
                onImageListener.onSolutionClick(getAdapterPosition());
            }
        }
    }

    public interface onImageListener {
        void onImageClick(int position);

        void onSolutionClick(int position);
    }
}
