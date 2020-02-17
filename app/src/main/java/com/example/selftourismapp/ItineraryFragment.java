package com.example.selftourismapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ItineraryFragment extends AppCompatActivity {

    EditText editText;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_fragment);

        editText = findViewById(R.id.editText2);
        button = findViewById(R.id.buttonEnter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().equals("3")){
                    Intent i = new Intent(getApplicationContext(), itinerarysugesstion.class);
                    startActivity(i);
                    Log.v("tag01", "ta09g87");
                }
            }
        });
    }
}
