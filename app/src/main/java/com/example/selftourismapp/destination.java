package com.example.selftourismapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class destination extends AppCompatActivity {
    private ArrayList<DestinationItem> mDestinationList;
    private destinationAdapter mAdapter;
    private TextView description;
    DatabaseReference reff;
    List<DestinationItem> ContentList;
    String desc;
    Button button1;

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("ons", "onstart act");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination);

        ContentList = new ArrayList<>();
        description = (TextView) findViewById(R.id.destinationDescription);

        initList();

        Spinner spinnerDestination = findViewById(R.id.spinner_destination);
        mAdapter = new destinationAdapter(this, mDestinationList);
        spinnerDestination.setAdapter(mAdapter);

        button1 = (Button) findViewById(R.id.goMap);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(destination.this, MapsActivity.class);
                startActivity(i);
            }
        });

        // taking data from the firebase
        reff = FirebaseDatabase.getInstance().getReference("Contents");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DestinationItem list = snapshot.getValue(DestinationItem.class);
                    DestinationItem obj = new DestinationItem(snapshot.getKey(), 0, list.getmDestinationDesc());
                    String desc = obj.getdestinationName();
                    Log.v("descName", obj.getdestinationName());
                    ContentList.add(obj);
                    Log.v("ObjKey", obj.getdestinationName());
                    Log.v("ObjKey", obj.getmDestinationDesc());
                    Log.v("ObjKey", ContentList.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Log.v("ObjKeyxdcf", ContentList.toString());
        // *Log.v("ObjKey", ContentList.get(0).getmDestinationDesc());


        // press spinner to allow drop down list
        spinnerDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final DestinationItem clickeditem = (DestinationItem) adapterView.getItemAtPosition(i);
                reff = FirebaseDatabase.getInstance().getReference("Contents").child(clickeditem.getdestinationName());
                Log.v("TagX", clickeditem.getdestinationName());
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DestinationItem dsitem = dataSnapshot.getValue(DestinationItem.class);
                        Log.v("TagX", dsitem.getmDestinationDesc());
                        String clickedDestinationName = dsitem.getdestinationName();
                        String clickedDestinationDesc = dsitem.getmDestinationDesc();
                        Toast.makeText(destination.this, clickedDestinationDesc + " selected", Toast.LENGTH_SHORT).show();
                        description.setText(clickedDestinationDesc);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


//                DestinationItem clickeditem = (DestinationItem)adapterView.getItemAtPosition(i);
//                String clickedDestinationName = reff.child(clickeditem.getdestinationName()).toString();
//                String clickedDestinationDesc = reff.child(clickeditem.getdestinationName()).child("mDestinationDesc").toString();
//                Toast.makeText(destination.this, clickedDestinationName + " selected", Toast.LENGTH_SHORT).show();
//                description.setText(clickedDestinationDesc);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //array list for the destination
    private void initList() {
        mDestinationList = new ArrayList<>();
        mDestinationList.add(new DestinationItem("CHIJMES", R.drawable.chijmes, "Now home to an array of shops, bars and restaurants, CHIJMES used to be a Catholic convent school way back in the 1800s. The compound houses several buildings of varying architectural styles and set up during different points in history, such as an orphanage, a dormitory and a Gothic Chapel.\n" +
                "\n" +
                "The complex was renamed CHIJMES in 1990, a reference to the convent’s acronym and the sound of its tower bells. After redevelopment work, CHIJMES has grown into the well-loved dining and nightlife enclave that it is today.\n" +
                "\n" +
                "CHIJMES. 30 Victoria Street, Singapore 18796.\n" +
                "Daily 9.30am-6.30pm. Opening hours vary for individual businesses."));
        mDestinationList.add(new DestinationItem("MERLION", R.drawable.merlion, " Merlion, a mythical creature that’s half-fish and half-lion. The Merlion combines two elements of Singapore’s identity—its body symbolises the fishing villages of Singapore’s past, while its lion head is a symbol of Singapura (“lion city” in Sanskrit).\n" +
                "\n" +
                "The Merlion, built by local craftsman Lim Nang Seng, was unveiled on 15 September 1972 by then-Prime Minister Lee Kuan Yew. The icon was originally positioned at the mouth of the Singapore River, but was later moved to its current spot overlooking the bay at the Merlion Park.\n" +
                "\n" +
                "Merlion Park. One Fullerton, Singapore 049213.\n" +
                "Daily 24 hours.\n" +
                "\n"));
        mDestinationList.add(new DestinationItem("HELIX BRIDGE", R.drawable.helix, "The Helix Bridge is a pedestrian bridge, considered to be the world’s first curved bridge, which links Marina Centre with Marina South in the Marina Bay area, Singapore. Together with Promenade, a walkway along marina bay, it forms a 3.5-kilometer pedestrian loop around the whole bay. The 280-metre bridge, the longest in Singapore, has five viewing platforms sited at strategic locations, which provide stunning views of the Singapore skyline and events taking place within Marina Bay. It is a place that links cultural, recreational and entertainment facilities for the community.\n"
        ));
        mDestinationList.add(new DestinationItem("MARINA BAY SANDS", R.drawable.marina, ""));

    }
}
