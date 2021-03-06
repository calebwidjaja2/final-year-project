package com.example.selftourismapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_Drawer_open,R.string.navigation_Drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    //navigation drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_restaurant:
                Intent i = new Intent(getApplicationContext(), restaurant.class);
                startActivity(i);
                break;
        }
        switch (item.getItemId()){
            case R.id.nav_place:
                Intent i = new Intent (getApplicationContext(), destination.class);
                startActivity(i);
                break;
        }
        switch (item.getItemId()){
            case R.id.nav_map:
                Intent i = new Intent(this, MapsActivity.class);
                startActivity(i);
                break;
        }
        switch (item.getItemId()){
            case R.id.nav_chat:
                Intent i = new Intent (this, chat_service.class);
                startActivity(i);
                break;
        }
        switch (item.getItemId()){
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent (this, LoginPage.class);
                startActivity(i);
                finish();
        }
        switch (item.getItemId()){
            case R.id.nav_itinerary:
                Intent i = new Intent(this, ItineraryFragment.class);
                startActivity(i);
                break;
        }
        return true;
    }

    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }




}
