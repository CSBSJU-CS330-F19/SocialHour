package com.example.socialhour;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.DataTypes.User;
import com.example.services.DBConnection;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class PendingEvents extends AppCompatActivity {

    static String selectedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBConnection dbc = DBConnection.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_events);
        DataSnapshot EventsSnap = dbc.getEventDataSnapshot();

        LinearLayout linearLayout = findViewById(R.id.linear_lay);

        User currentUser = dbc.getCurrentUser();
        final ArrayList<String> pendingEvents = dbc.getPendingSocialHourEvents(User.getUserKey(currentUser.getEmail()));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < pendingEvents.size(); i++){
            final Button button = new Button(this);
            button.setText(EventsSnap.child(pendingEvents.get(i)).child("eventName").getValue(String.class));
            button.setLayoutParams(params);
            button.setTextSize(30);
            button.setId(i);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int x = button.getId();
                    setSelectedEvent(pendingEvents.get(x));
                    startActivity(new Intent(getApplicationContext(),AcceptEvent.class));
                }
            });

            linearLayout.addView(button);
        }
    }

    public void setSelectedEvent(String id){
        selectedEvent = id;
    }
}
