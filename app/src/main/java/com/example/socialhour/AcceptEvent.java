package com.example.socialhour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.services.DBConnection;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class AcceptEvent extends AppCompatActivity {
    private Button accept, decline;
    DBConnection dbc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_event);

        DataSnapshot eventsSnap = LogOn.dbc.getEventDataSnapshot();
        String id =  PendingEvents.selectedEvent;
        String displayName = eventsSnap.child(id).child("eventName").getValue(String.class);

        TextView name = findViewById(R.id.EventName);
        name.setText(displayName);

        accept = findViewById(R.id.Accept);
        decline = findViewById(R.id.Decline);
        dbc = DBConnection.getInstance();

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> pendingEvents = dbc.getCurrentUser().getPendingEvents();
                pendingEvents.remove(PendingEvents.selectedEvent);
                dbc.acceptEventInvite(PendingGroups.selectedGroup, pendingEvents);
                startActivity(new Intent(getApplicationContext(), EventsPage.class));
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                //removeFromPending(String.valueOf(R.id.textView10));
                ArrayList<String> pendingEvents = dbc.getCurrentUser().getPendingGroups();
                pendingEvents.remove(PendingGroups.selectedGroup);
                dbc.declineEventInvite(pendingEvents);
                startActivity(new Intent(getApplicationContext(), EventsPage.class));
            }
        });
    }
}
