package com.example.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClubOwnerActivityEvents extends AppCompatActivity {
    ClubOwner newClubOwner;
    ListView listViewEvents;
    DatabaseReference dbEvents;
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

        dbEvents = FirebaseDatabase.getInstance().getReference("clubs").child(newClubOwner.getUsername()).child("events");
        DatabaseReference dbEventTypes = FirebaseDatabase.getInstance().getReference("eventTypes");
        listViewEvents = findViewById(R.id.listEventsView);
        List<Event> events = new ArrayList<>();
        dbEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    List<String> users = null;
                    for (DataSnapshot nestedSnapshot : postSnapshot.child("users").getChildren()) {
                        users.add(nestedSnapshot.getValue().toString());
                    }
                    String[] usersArr = users.toArray(new String[0]);
                    eventType evtype = postSnapshot.child("eventtype").getValue(eventType.class);
                    Event newEvent = new Event(postSnapshot.getKey().toString(),evtype,usersArr,postSnapshot.child("starttime").getValue().toString(),postSnapshot.child("location").getValue().toString(),Float.parseFloat(postSnapshot.child("pace").getValue().toString()),Integer.parseInt(postSnapshot.child("level").getValue().toString()));
                    events.add(newEvent);
                }
                EventList eventAdaptor = new EventList(ClubOwnerActivityEvents.this, events);
                listViewEvents.setAdapter(eventAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}