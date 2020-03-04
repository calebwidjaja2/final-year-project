package com.example.selftourismapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DineFragment extends Fragment {

//    Button button;
//    Button button2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dine, container, false);



    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        button.findViewById(R.id.chineseFood);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getContext(),chineseList.class);
//                startActivity(i);
//            }
//        });
//        super.onCreate(savedInstanceState);
//    }
}
