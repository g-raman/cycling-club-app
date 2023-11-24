package com.example.gcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ClubOwnerActivitySettings extends AppCompatActivity {
    ClubOwner newClubOwner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_owner_settings);

        Intent i = getIntent();
        newClubOwner = (ClubOwner)i.getSerializableExtra("USER");
        Log.d("TAG", newClubOwner.getUsername());
        BottomNavigationView nav = findViewById(R.id.navClubOwner);
        nav.setSelectedItemId(R.id.nav_club_owner_settings);
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_club_owner_settings) {
                return true;
            } else if (item.getItemId() == R.id.nav_club_owner_events) {
                Intent clubOwnerEvents = new Intent(ClubOwnerActivitySettings.this, ClubOwnerActivityEvents.class);
                clubOwnerEvents.putExtra("USER", newClubOwner);
                startActivity(clubOwnerEvents);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            }
            return false;
        });
    }
}