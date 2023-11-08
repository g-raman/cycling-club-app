package com.example.gcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivityClubs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_clubs);

        BottomNavigationView nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.nav_clubs);
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_clubs) {
                return true;
            } else if (item.getItemId() == R.id.nav_users) {
                startActivity(new Intent(getApplicationContext(), AdminActivityUsers.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_settings) {
                startActivity(new Intent(getApplicationContext(), AdminActivitySettings.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            }
            return false;
        });
    }
}