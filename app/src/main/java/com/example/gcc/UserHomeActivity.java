package com.example.gcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        BottomNavigationView nav = findViewById(R.id.navUser);
        nav.setSelectedItemId(R.id.nav_user_home);
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_user_home) {
                return true;
            } else if (item.getItemId() == R.id.nav_user_search) {
                startActivity(new Intent(getApplicationContext(), UserSearchActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_user_settings) {
                startActivity(new Intent(getApplicationContext(), UserSettingsActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            }
            return false;
        });
    }
}