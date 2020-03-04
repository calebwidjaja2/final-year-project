package com.example.selftourismapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class destinationAdapter extends ArrayAdapter<DestinationItem> {

    // for the constructor adapter
    public destinationAdapter(Context context, ArrayList<DestinationItem> destinationList) {
        super(context, 0, destinationList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    // the method for drop down list
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }


    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) { // only if thew convertView is empty then only take and create
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.destination_row, parent, false
            );
        }

        // references to the destination image and the name of the destination
        ImageView destinationImageName = convertView.findViewById(R.id.destinationImage);
        TextView textViewName = convertView.findViewById(R.id.destinationName);

        DestinationItem destinationitem = getItem(position);

        if (destinationitem != null) {
            destinationImageName.setImageResource(destinationitem.getdestinationImage());
            textViewName.setText(destinationitem.getdestinationName());
        }

        return convertView;
    }
}
