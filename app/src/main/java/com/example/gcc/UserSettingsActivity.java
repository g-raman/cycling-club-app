package com.example.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gcc.utils.Helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.rpc.Help;

public class UserSettingsActivity extends AppCompatActivity {
    User newUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        Intent i = getIntent();
        newUser = (User) i.getSerializableExtra("USER");

        DatabaseReference userSettings = FirebaseDatabase.getInstance().getReference("users").child(newUser.getUsername());

        BottomNavigationView nav = findViewById(R.id.navUser);
        nav.setSelectedItemId(R.id.nav_user_settings);
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_user_settings) {
                return true;
            } else if (item.getItemId() == R.id.nav_user_home) {
                Intent userSearch = new Intent(UserSettingsActivity.this, UserHomeActivity.class);
                userSearch.putExtra("USER", newUser);
                startActivity(userSearch);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_user_search) {
                Intent userSearch = new Intent(UserSettingsActivity.this, UserSearchActivity.class);
                userSearch.putExtra("USER", newUser);
                startActivity(userSearch);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_user_logout) {
                Intent userSearch = new Intent(UserSettingsActivity.this, LoginActivity.class);
                startActivity(userSearch);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            }
            return false;
        });

        Integer[] Levels = new Integer[]{1, 2, 3, 4, 5};
        ArrayAdapter<Integer> levelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Levels);
        Spinner level = findViewById(R.id.spinnerUserLevel);
        level.setAdapter(levelAdapter);

        EditText userPwd = findViewById(R.id.userPassword);
        EditText userPace = findViewById(R.id.userPace);
        EditText userAge = findViewById(R.id.userAge);
        Menu menu = nav.getMenu();

        MenuItem menuItem = menu.findItem(R.id.nav_user_search);
        MenuItem menuItem2 = menu.findItem(R.id.nav_user_home);

        userSettings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("password").exists()){
                    userPwd.setText(snapshot.child("password").getValue().toString());
                }
                else {
                    menuItem.setVisible(false);
                    menuItem2.setVisible(false);
                }
                if (snapshot.child("idealpace").exists()){
                    userPace.setText(snapshot.child("idealpace").getValue().toString());
                }
                else {
                    menuItem.setVisible(false);
                    menuItem2.setVisible(false);
                }
                if (snapshot.child("userAge").exists()){
                    userAge.setText(snapshot.child("userAge").getValue().toString());
                }
                else {
                    menuItem.setVisible(false);
                    menuItem2.setVisible(false);
                }
                if (snapshot.child("userLevel").exists()){
                    level.setSelection(Integer.valueOf(snapshot.child("userLevel").getValue().toString())-1);
                }
                else {
                    menuItem.setVisible(false);
                    menuItem2.setVisible(false);
                }
                if ((snapshot.child("password").exists() && snapshot.child("idealpace").exists() && snapshot.child("userAge").exists()) && snapshot.child("userLevel").exists()){
                    menuItem.setVisible(true);
                    menuItem2.setVisible(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button chngeSettings = findViewById(R.id.changeUserSettingsBtn);

        chngeSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper helper = new Helper();
                String password = userPwd.getText().toString();
                float pace = 0;
                int age = 0;

                try {
                    pace = Float.parseFloat(userPace.getText().toString());
                } catch (Exception ignored) {
                    Toast.makeText(UserSettingsActivity.this, "Pace must be a decimal number", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    age = Integer.parseInt(userAge.getText().toString());
                } catch (Exception ignored) {
                    Toast.makeText(UserSettingsActivity.this, "Age must be an integer", Toast.LENGTH_SHORT).show();
                    return;
                }

                int idealLevel = level.getSelectedItemPosition() + 1;

                boolean validPassword = helper.validatePassword(password);
                boolean validPace = helper.validatePace(pace);
                boolean validAge = helper.validateAge(age);
                boolean validLevel = helper.validateLevel(idealLevel);

                if (!validPassword) {
                    Toast.makeText(UserSettingsActivity.this, "Make sure password meets all requirements", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!validPace) {
                    Toast.makeText(UserSettingsActivity.this, "Pace must be greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!validAge) {
                    Toast.makeText(UserSettingsActivity.this, "Age must be greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!validLevel) {
                    Toast.makeText(UserSettingsActivity.this, "Level must be between 1 - 5", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(UserSettingsActivity.this, "Success", Toast.LENGTH_SHORT).show();

                newUser.setPassword(password);
                newUser.setPreferredPace(pace);
                newUser.setAge(age);
                newUser.setPreferredLevel(idealLevel);

                userSettings.child("password").setValue(password);
                userSettings.child("idealpace").setValue(pace);
                userSettings.child("userAge").setValue(age);
                userSettings.child("userLevel").setValue(idealLevel);
            }
        });


    }
}