package com.example.gcc;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

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

    private void userJoinEvent(Event evJoin){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUsername()).child("joinedevents").child(evJoin.getID());
        DatabaseReference dbClub = FirebaseDatabase.getInstance().getReference("clubs");
        dbRef.setValue(evJoin);

        dbClub.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot newSnap : snapshot.getChildren()){
                    if (snapshot.child("events").hasChildren()) {
                        for (DataSnapshot events : snapshot.child("events").getChildren()) {
                            if (events.getKey().toString().equals(evJoin.getID())){
                                dbClub.child(newSnap.getKey()).child("events").child("users").child(user.getUsername()).setValue(user);
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

    }
}
