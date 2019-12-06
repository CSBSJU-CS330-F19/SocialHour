package com.example.socialhour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.DataTypes.SocialHourEvent;
import com.example.DataTypes.User;
import com.example.services.DBConnection;
import com.google.api.client.util.DateTime;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class EventsPage extends AppCompatActivity {
    Button createEvent;
    static String selectedEvent;
    static DataSnapshot eventSnap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBConnection dbc = DBConnection.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_page);
        User currentUser = dbc.getCurrentUser();

        createEvent = findViewById(R.id.createEvent);

        eventSnap = dbc.getEventDataSnapshot();

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), InputGenerateMeetingTimes.class));
            }
        });

        LinearLayout linearLayout = findViewById(R.id.linLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        final ArrayList<String> events = dbc.getGroupsEvents(GroupsPage.getSelectedGroup().getId());
        System.out.println(events.toString());


        for (int i = 0; i < events.size(); i++) {
            final Button button = new Button(this);
            button.setText(eventSnap.child(events.get(i)).child("eventName").getValue(String.class));
            button.setLayoutParams(params);
            button.setId(i);
            button.setTextSize(30);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int x = button.getId();
                    setSelectedEvent(events.get(x));
                    startActivity(new Intent(getApplicationContext(), SingleEventPage.class));
                }
            });

            linearLayout.addView(button);
        }
    }

    public void setSelectedEvent (String id){
        selectedEvent = id;
    }

    //Change once events field is populated in db
    public static SocialHourEvent getSelectedEvent () {
        SocialHourEvent returnEvent = new SocialHourEvent(eventSnap.child(selectedEvent)
                    .child("eventName").getValue().toString(),
                    (DateTime) eventSnap.child(selectedEvent).child("startTime").getValue(),
                    (DateTime) eventSnap.child(selectedEvent).child("endTime").getValue(),
                    eventSnap.child(selectedEvent).child("groupId").getValue().toString(),
                    eventSnap.child(selectedEvent).child("eventID").getValue().toString(),
                    (ArrayList<String>) eventSnap.child(selectedEvent).child("attendees").getValue());
        return returnEvent;
    }




}
