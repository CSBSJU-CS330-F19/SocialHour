package com.example.socialhour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.DataTypes.SocialHourEvent;
import com.example.services.DBConnection;

import java.util.ArrayList;

public class SingleEventPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event_page);
        DBConnection dbc = DBConnection.getInstance();
        SocialHourEvent s = dbc.getEvent(EventsPage.selectedEvent);
        TextView eventName = findViewById(R.id.EventName);
        TextView dateTime = findViewById(R.id.DateTime);
        TextView members = findViewById(R.id.Members);
        //ScrollView scroll = findViewById(R.id.)
        eventName.setText(s.getEventName());
        dateTime.setText("Date : " + s.getStartTime().toString().replace("00.00", "").replace(":0-06:00", "").replace("T", "\nTime:"));
        ArrayList<String> membersList = s.getAttendees();
        members.setText("Members Attending: " + membersList.toString());


    }
}
