package com.example.gcc;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class UserEventList extends ArrayAdapter<Event> {

    private Activity context;
    List<Event> events;

    User user;

    UserEventList(Activity context, List<Event> events, User user){
        super(context, R.layout.layout_user_event_search, events);
        this.context = context;
        this.events = events;
        this.user = user;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View clubListItem = inflater.inflate(R.layout.layout_user_event_search, null, true);

        TextView evName = (TextView) clubListItem.findViewById(R.id.textViewEventName);
        TextView idealPace = (TextView) clubListItem.findViewById(R.id.textViewIdealPace);
        TextView evType = (TextView) clubListItem.findViewById(R.id.textViewEventType);
        TextView idealLevel = (TextView) clubListItem.findViewById(R.id.textViewIdealLevel);


        Event newEvent = events.get(position);

        evName.setText(newEvent.getName());
        idealPace.setText(newEvent.getPace().toString());
        idealLevel.setText(newEvent.getLevel().toString());
        evType.setText(newEvent.getType().toString().toString());

        Button joinEvBtn = (Button) clubListItem.findViewById(R.id.joinEventBtn);

        joinEvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                userJoinEvent(newEvent);
            }
        });

        return  clubListItem;
    }

    private void userJoinEvent(Event evJoin) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUsername()).child("joinedevents").child(evJoin.getID());
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUsername());
        DatabaseReference dbClub = FirebaseDatabase.getInstance().getReference("clubs");
        dbRef.child("eventname").setValue(evJoin.getName());
        dbRef.child("eventtype").setValue(evJoin.getType());
        dbRef.child("level").setValue(evJoin.getLevel());
        dbRef.child("pace").setValue(evJoin.getPace());
        dbRef.child("location").setValue(evJoin.getLocation());
        dbRef.child("starttime").setValue(evJoin.getStartTime());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (evJoin.getPace() > Float.parseFloat((String) snapshot.child("idealpace").getValue().toString())) {
                    Toast.makeText(context, "You do not meet pace requirements", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (evJoin.getLevel() > Integer.parseInt((String) snapshot.child("userLevel").getValue().toString())) {
                    Toast.makeText(context, "You do not meet level requirements", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbClub.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot newSnap : snapshot.getChildren()) {
                            if (newSnap.child("events").hasChildren()) {
                                for (DataSnapshot events : newSnap.child("events").getChildren()) {
                                    if (events.getKey().toString().equals(evJoin.getID())) {
                                        dbClub.child(newSnap.getKey()).child("events").child(evJoin.getID()).child("users").child(user.getUsername()).setValue(user);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
