package com.example.gcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        BottomNavigationView nav = findViewById(R.id.navUser);
        nav.setSelectedItemId(R.id.nav_user_search);
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_user_search) {
                return true;
            } else if (item.getItemId() == R.id.nav_user_home) {
                startActivity(new Intent(getApplicationContext(), UserHomeActivity.class));
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