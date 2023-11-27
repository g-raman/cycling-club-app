package com.example.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClubOwnerActivityEvents extends AppCompatActivity {

    ClubOwner newClubOwner;
    ListView listViewEvents;
    DatabaseReference dbEvents;
    String UUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_owner_events);
        DatabaseReference keyGet = FirebaseDatabase.getInstance().getReference("clubs");
        keyGet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keysnapshot : snapshot.getChildren()) {
                    if ((keysnapshot.child("username").getValue().toString()).equals(newClubOwner.getUsername())){
                        UUID = keysnapshot.getKey().toString();
                        listViewEvents = findViewById(R.id.listEventsView);
                        List<Event> events = new ArrayList<>();
                        for (DataSnapshot postSnapshot : keysnapshot.child("events").getChildren()){
                            List<String> users = null;
                            for (DataSnapshot nestedSnapshot : postSnapshot.child("users").getChildren()) {
                                users.add(nestedSnapshot.getValue().toString());
                            }
                            String[] usersArr = users.toArray(new String[0]);
                            eventType evtype = postSnapshot.child("eventtype").getValue(eventType.class);
                            Event newEvent = new Event(postSnapshot.child("eventname").getValue().toString(),evtype,usersArr,postSnapshot.child("starttime").getValue().toString(),postSnapshot.child("location").getValue().toString(),Float.parseFloat(postSnapshot.child("pace").getValue().toString()),Integer.parseInt(postSnapshot.child("level").getValue().toString()));
                            events.add(newEvent);
                        }
                        EventList eventAdaptor = new EventList(ClubOwnerActivityEvents.this, events);
                        listViewEvents.setAdapter(eventAdaptor);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

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
                clubOwnerSettings.putExtra("UUID", UUID);
                startActivity(clubOwnerSettings);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            }
            return false;
        });

        Button addEvButton = findViewById(R.id.addEventBtn);

        addEvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEventDialog();
            }
        });


    }

    private void addEventDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_event, null);
        dialogBuilder.setView(dialogView);

        DatabaseReference evtype = FirebaseDatabase.getInstance().getReference("eventTypes");
        Spinner evTypeSpinner = dialogView.findViewById(R.id.SpinnerClubevType);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item);
        evtype.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> evTypeNames = new ArrayList<>();
                for (DataSnapshot newsnapshot : snapshot.getChildren()){
                    if (newsnapshot.child("status").getValue().toString().equals("true")) {
                        evTypeNames.add(newsnapshot.getKey().toString());
                    }
                }
                String[] evtypeArr = evTypeNames.toArray(new String[0]);
                typeAdapter.clear();
                typeAdapter.addAll(evtypeArr);
                typeAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        evTypeSpinner.setAdapter(typeAdapter);

        //this is the database reference youll be using setvalue to
        final Button addEvent = dialogView.findViewById(R.id.clubOwnerCreateEvent);
        EditText name = dialogView.findViewById(R.id.clubOwnerEditEventname);
        EditText startTime = dialogView.findViewById(R.id.clubOwnerEditStartTime);
        EditText location = dialogView.findViewById(R.id.clubOwnerEditLocation);
        EditText pace = dialogView.findViewById(R.id.clubOwnerEditPace);
        EditText level = dialogView.findViewById(R.id.clubOwnerEditLevel);

        dialogBuilder.setTitle("Add Event");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference evadder = FirebaseDatabase.getInstance().getReference("clubs").child(UUID).child("events");

                evadder.child("name").setValue(name.getText().toString());
                evadder.child("startTime").setValue(startTime.getText().toString());
                evadder.child("location").setValue(location.getText().toString());
                evadder.child("pace").setValue(pace.getText().toString());
                evadder.child("level").setValue(level.getText().toString());
                b.dismiss();
            }
        });
    }
}