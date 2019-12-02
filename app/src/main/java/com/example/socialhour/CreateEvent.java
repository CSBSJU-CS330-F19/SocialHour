package com.example.socialhour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.DataTypes.SocialHourEvent;
import com.example.DataTypes.Group;
import com.example.DataTypes.User;
import com.example.services.DBConnection;
import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.UUID;

public class CreateEvent extends AppCompatActivity {
    private DBConnection dbConnection;
    private Button createButton;
    private EditText eventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        createButton = findViewById(R.id.genMeetingTimes);
        eventName = findViewById(R.id.editText);
        dbConnection = DBConnection.getInstance();



        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User currentUser = dbConnection.getCurrentUser();

                String uniqueID = User.getUserKey(currentUser.getEmail());

                Group currentGroup = SingleGroupPage.selectedGroup;

                String name = eventName.getText().toString();

                //Hard coded for now
                DateTime startTime = new DateTime("2019-12-22T13:30:00.000");
                DateTime endTime = new DateTime("2019-12-22T14:30:00.000");
                System.out.println(startTime.toString());
                System.out.println(endTime.toString());

                String groupID = currentGroup.getId();

                String eventID = UUID.randomUUID().toString();

                ArrayList<String> attendees = new ArrayList<>();

                attendees.add(uniqueID);

                SocialHourEvent newSocialHourEvent = new SocialHourEvent(name, startTime, endTime, groupID, eventID, attendees);

                dbConnection.addEventToDB(newSocialHourEvent);

                startActivity(new Intent(getApplicationContext(), GroupsPage.class));

            }
        });
    }
}
