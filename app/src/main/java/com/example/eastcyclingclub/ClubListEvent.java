package com.example.eastcyclingclub;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ClubListEvent extends ArrayAdapter<ClubHelperClassEvent> {
    private Activity context;
    List<ClubHelperClassEvent> clubHelperClassEvents;

    public ClubListEvent(Activity context, List<ClubHelperClassEvent> clubHelperClassEvents) {
        super(context, R.layout.club_list_event, clubHelperClassEvents);
        this.context = context;
        this.clubHelperClassEvents = clubHelperClassEvents;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.club_list_event, null, true);

        TextView textViewEventType = (TextView) listViewItem.findViewById(R.id.textViewEventType);
        TextView textViewEventName = (TextView) listViewItem.findViewById(R.id.textViewEventName);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.textViewDate);
        TextView textViewMaxParticipants = (TextView) listViewItem.findViewById(R.id.textViewMaxParticipants);

        ClubHelperClassEvent clubHelperClassEvent = clubHelperClassEvents.get(position);
        textViewEventType.setText("Event type: " + clubHelperClassEvent.getEventType());
        textViewEventName.setText("Event name: " + clubHelperClassEvent.getEventName());
        textViewDate.setText("Event date: " + clubHelperClassEvent.getEventDate());
        textViewMaxParticipants.setText("Max participants: " + clubHelperClassEvent.getMaxParticipants());

        return listViewItem;
    }
    public void searchDataList(ArrayList<ClubHelperClassEvent> searchList){
        clubHelperClassEvents = searchList;
        notifyDataSetChanged();
    }
}
