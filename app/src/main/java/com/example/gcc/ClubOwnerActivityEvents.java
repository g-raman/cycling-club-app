package com.example.gcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ClubOwnerActivityEvents extends AppCompatActivity {
    ClubOwner newClubOwner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_owner_events);
        Intent i = getIntent();
        newClubOwner = (ClubOwner)i.getSerializableExtra("USER");

        BottomNavigationView nav = findViewById(R.id.navClubOwner);
        nav.setSelectedItemId(R.id.nav_club_owner_events);
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_club_owner_events) {
                return true;
            } else if (item.getItemId() == R.id.nav_club_owner_settings) {
                Intent clubOwnerSettings = new Intent(ClubOwnerActivityEvents.this, ClubOwnerActivitySettings.class);
                clubOwnerSettings.putExtra("USER", newClubOwner);
                startActivity(clubOwnerSettings);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            }
            return false;
        });
    }
}