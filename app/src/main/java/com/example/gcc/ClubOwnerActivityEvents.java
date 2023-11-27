package com.example.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClubOwnerActivityEvents extends AppCompatActivity {

    ClubOwner newClubOwner;
    ListView listViewEvents;
    DatabaseReference dbEvents;
    static String UUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_owner_events);
        DatabaseReference keyGet = FirebaseDatabase.getInstance().getReference("clubs");
        List<Event> events = new ArrayList<>();
        listViewEvents = findViewById(R.id.listEventsView);
        keyGet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keysnapshot : snapshot.getChildren()) {
                    if ((keysnapshot.child("username").getValue().toString()).equals(newClubOwner.getUsername())){
                        UUID = keysnapshot.getKey().toString();

                        DatabaseReference evRef = keyGet.child(UUID).child("events");
                        evRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot newsnapshot) {
                                events.clear();
                                for (DataSnapshot postSnapshot : newsnapshot.getChildren()){
                                    List<User> users = new ArrayList<>();

                                    for (DataSnapshot nestedSnapshot : postSnapshot.child("users").getChildren()) {
                                        users.add(nestedSnapshot.getValue(User.class));
                                    }
                                    if (postSnapshot.hasChild("eventname") && postSnapshot.hasChild("starttime") && postSnapshot.hasChild("location") && postSnapshot.hasChild("pace") && postSnapshot.hasChild("level") && postSnapshot.hasChild("eventtype")) {
                                        User[] usersArr = users.toArray(new User[0]);
                                        String newevname = postSnapshot.child("eventname").getValue().toString();
                                        String starttime = postSnapshot.child("starttime").getValue().toString();
                                        String loc = postSnapshot.child("location").getValue().toString();
                                        Float newpace = Float.parseFloat(postSnapshot.child("pace").getValue().toString());
                                        Integer level = Integer.parseInt(postSnapshot.child("level").getValue().toString());
                                        eventType evtype = postSnapshot.child("eventtype").getValue(eventType.class);
                                        Event newEvent = new Event(newevname,
                                                evtype,
                                                usersArr,
                                                starttime,
                                                loc,
                                                newpace,
                                                level,
                                                postSnapshot.getKey());
                                        events.add(newEvent);
                                    }
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

        listViewEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event newEvent = events.get(i);
                addEventDialog(false,newEvent);

                Button membersSelButton = listViewEvents.findViewById(R.id.clubOwnerListSelectMembers);

                membersSelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMembersList(newEvent);
                    }
                });
                return true;

            }
        });





        Button addEvButton = findViewById(R.id.addEventBtn);

        addEvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEventDialog(true, null);
            }
        });




    }

    private void addEventDialog(Boolean creating, Event updateEvent){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_event, null);
        dialogBuilder.setView(dialogView);

        Spinner evTypeSpinner = dialogView.findViewById(R.id.SpinnerClubevType);
        DatabaseReference evtype = FirebaseDatabase.getInstance().getReference("eventTypes");
        List<eventType> evTypeNames = new ArrayList<>();

        EventTypeAdapter typeAdapter = new EventTypeAdapter(this, android.R.layout.simple_spinner_dropdown_item, evTypeNames);
        evTypeSpinner.setAdapter(typeAdapter);

        evtype.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                evTypeNames.clear(); // Clear the list before populating it again
                for (DataSnapshot newsnapshot : snapshot.getChildren()){
                    if (newsnapshot.child("status").getValue().toString().equals("true")) {
                        eventType event = newsnapshot.getValue(eventType.class);
                        evTypeNames.add(event);
                    }
                }
                typeAdapter.notifyDataSetChanged(); // Notify the adapter of changes in the data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });

        //this is the database reference youll be using setvalue to
        final Button addEvent = dialogView.findViewById(R.id.clubOwnerCreateEvent);
        EditText name = dialogView.findViewById(R.id.clubOwnerEditEventname);
        EditText startTime = dialogView.findViewById(R.id.clubOwnerEditStartTime);
        EditText location = dialogView.findViewById(R.id.clubOwnerEditLocation);
        EditText pace = dialogView.findViewById(R.id.clubOwnerEditPace);
        EditText level = dialogView.findViewById(R.id.clubOwnerEditLevel);
        TextView typeText = dialogView.findViewById(R.id.textViewClubAddEvType);

        if (creating==false){
            name.setText(updateEvent.getName());
            startTime.setText(updateEvent.getStartTime());
            location.setText(updateEvent.getLocation());
            pace.setText(updateEvent.getPace().toString());
            level.setText((updateEvent.getLevel().toString()));
            for (int i = 0; i < evTypeNames.size(); i++) {
                if (evTypeNames.get(i).equals(updateEvent.getType())) {
                    evTypeSpinner.setSelection(i);
                    break;
                }
            }
        }

        typeText.setText("Text: ");

        dialogBuilder.setTitle("Add Event");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        evTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eventType selectedEventType = (eventType) parent.getItemAtPosition(position);
                pace.setHint("Pace (Min: "+selectedEventType.getPaceMin()+" / Max: "+selectedEventType.getPaceMax()+")");
                level.setHint("Level (0 - "+selectedEventType.getLevel()+")");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                eventType selectedEventType = (eventType) parent.getItemAtPosition(0);
            }


        });



        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference evadder = FirebaseDatabase.getInstance().getReference("clubs").child(UUID).child("events");
                String eventID="";
                if (creating==true) {
                    eventID= evadder.push().getKey();
                }
                else if (!creating){
                    eventID = updateEvent.getID();
                }


                eventType selectedEventType = (eventType) evTypeSpinner.getSelectedItem();
                Log.d("TAG",selectedEventType.getName());
                try {
                    if (Float.parseFloat(pace.getText().toString())>(selectedEventType.getPaceMin())
                        && (Float.parseFloat(pace.getText().toString())<(selectedEventType.getPaceMax()))
                        && (Integer.valueOf(level.getText().toString()))<=(selectedEventType.getLevel())) {
                        evadder.child(eventID).child("pace").setValue((pace.getText().toString()));
                        evadder.child(eventID).child("level").setValue(Integer.valueOf(level.getText().toString()));
                        evadder.child(eventID).child("eventname").setValue(name.getText().toString());
                        evadder.child(eventID).child("starttime").setValue(startTime.getText().toString());
                        evadder.child(eventID).child("location").setValue(location.getText().toString());
                        evadder.child(eventID).child("users").setValue(null);
                        evadder.child(eventID).child("eventtype").setValue(selectedEventType);
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "ensure values are within the correct bounds", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Log.d("TAG", "BAD");
                }

            }
        });


    }
    public static String getUUID(){ return UUID; }

    private void showMembersList(Event event){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this); // Use the context variable
        LayoutInflater inflater = getLayoutInflater(); // Obtain LayoutInflater from the context

        final View dialogView = inflater.inflate(R.layout.activity_club_owner_events_participants, null);
        dialogBuilder.setView(dialogView);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        List<String> Usernames = new ArrayList<>();
        ListView listView = dialogView.findViewById(R.id.listClubOwnerEventMembersView);

        for (User newUser : event.getUsers()){
            Usernames.add(newUser.getUsername());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Usernames);

        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("clubs").child(ClubOwnerActivityEvents.getUUID()).child("events").child(Usernames.get(position));
                ref.removeValue();
                return false;
            }
        });
    }
}