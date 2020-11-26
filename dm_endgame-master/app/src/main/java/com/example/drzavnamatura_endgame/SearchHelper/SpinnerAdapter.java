package com.example.drzavnamatura_endgame.SearchHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drzavnamatura_endgame.R;

import java.util.ArrayList;
import java.util.List;


public class SpinnerAdapter extends ArrayAdapter {

    ArrayList<String> listOfOptions;

    public SpinnerAdapter(Context context, ArrayList<String> listOfOptions) {
        super(context, 0, listOfOptions);
        this.listOfOptions = listOfOptions;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return returnView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return returnView(position, convertView, parent);
    }

    private View returnView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_layout, parent, false);
        }
        ;
        TextView spinnerOption = convertView.findViewById(R.id.spinnerItemText);
        spinnerOption.setText(listOfOptions.get(position));
        return convertView;
    }
}


