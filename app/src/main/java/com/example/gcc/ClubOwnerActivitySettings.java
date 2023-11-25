package com.example.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClubOwnerActivitySettings extends AppCompatActivity {
    ClubOwner newClubOwner;
    String UUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_owner_settings);

        Intent i = getIntent();
        newClubOwner = (ClubOwner)i.getSerializableExtra("USER");
        UUID = (String)i.getSerializableExtra("UUID");
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
        EditText editTextClubName = findViewById(R.id.clubName);
        EditText editTextClubDesc = findViewById(R.id.clubDescription);
        EditText editTextclubNumber = findViewById(R.id.clubPhoneNumber);
        EditText editTextclubEmail = findViewById(R.id.clubEmailAddress);
        DatabaseReference dbClub = FirebaseDatabase.getInstance().getReference("clubs").child(UUID);

        dbClub.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("clubname").exists()) {
                    editTextClubName.setText(snapshot.child("clubname").getValue().toString());
                }
                if (snapshot.child("clubdesc").exists()) {
                    editTextClubDesc.setText(snapshot.child("clubdesc").getValue().toString());
                }
                if (snapshot.child("clubnumber").exists()) {
                    editTextclubNumber.setText(snapshot.child("clubnumber").getValue().toString());
                }
                if (snapshot.child("clubemail").exists()) {
                    editTextclubEmail.setText(snapshot.child("clubemail").getValue().toString());
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Adding a TextWatcher to the EditText
        editTextClubName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                dbClub.child("clubname").setValue(editable.toString());
            }
        });
        editTextClubDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                dbClub.child("clubdesc").setValue(editable.toString());
            }
        });
        editTextclubNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                dbClub.child("clubnumber").setValue(editable.toString());
            }
        });
        editTextclubEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                dbClub.child("clubemail").setValue(editable.toString());
            }
        });
    }
}