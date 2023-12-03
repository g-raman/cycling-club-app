package com.example.gcc;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class joinedEventsList extends ArrayAdapter<Event> {

    private Activity context;
    List<Event> events;

    User user;

    joinedEventsList(Activity context, List<Event> events, User user){
        super(context, R.layout.layout_user_event_search, events);
        this.context = context;
        this.events = events;
        this.user = user;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View clubListItem = inflater.inflate(R.layout.layout_user_joined_event, null, true);

        TextView EvName = (TextView) clubListItem.findViewById(R.id.textViewLayoutEventListNameJoined);
        TextView EvType = (TextView) clubListItem.findViewById(R.id.textViewLayoutEventListTypeJoined);
        TextView EvDate = (TextView) clubListItem.findViewById(R.id.textViewLayoutEventListStartDateJoined);
        TextView EvLoc = (TextView) clubListItem.findViewById(R.id.textViewLayoutEventListLocationJoined);
        TextView EvPace = (TextView) clubListItem.findViewById(R.id.textViewLayoutEventListPaceJoined);
        TextView EvLevel = (TextView) clubListItem.findViewById(R.id.textViewLayoutEventListLevelJoined);

        Event newEv = events.get(position);

        EvName.setText(newEv.getName());
        EvType.setText(newEv.getType().getName());
        EvDate.setText(newEv.getStartTime());
        EvLoc.setText(newEv.getLocation());
        EvPace.setText(newEv.getPace().toString());
        EvLevel.setText(newEv.getLevel().toString());

        return  clubListItem;


    }
}
