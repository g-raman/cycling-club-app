package com.example.gcc;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class EventList extends ArrayAdapter<Event> {

    private Activity context;
    List<Event> events;
    public EventList(Activity context, List<Event> events) {
        super(context, R.layout.layout_event_list, events);
        this.context = context;
        this.events = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_event_list, null, true);

        TextView textViewEventName = (TextView) listViewItem.findViewById(R.id.textViewLayoutEventListName);
        TextView textViewEventType = (TextView) listViewItem.findViewById(R.id.textViewLayoutEventListType);
        TextView textViewStartTime = (TextView) listViewItem.findViewById(R.id.textViewLayoutEventListStartTime);
        TextView textViewEventLocation = (TextView) listViewItem.findViewById(R.id.textViewLayoutEventListLocation);
        TextView textViewPace = (TextView) listViewItem.findViewById(R.id.textViewLayoutEventListPace);
        TextView textViewLevel = (TextView) listViewItem.findViewById(R.id.textViewLayoutEventListLevel);

        //Button membersSelButton = (Button) listViewItem.findViewById(R.id.clubOwnerListSelectMembers);

        Event newEvent = events.get(position);
        textViewEventName.setText(newEvent.getName());
        textViewEventType.setText(newEvent.getType().getName());
        textViewStartTime.setText(newEvent.getStartTime());
        textViewEventLocation.setText(String.format(newEvent.getLocation()));
        textViewPace.setText(String.format(newEvent.getPace().toString()));
        textViewLevel.setText(String.format(newEvent.getLevel().toString()));

        return listViewItem;
    }
}
