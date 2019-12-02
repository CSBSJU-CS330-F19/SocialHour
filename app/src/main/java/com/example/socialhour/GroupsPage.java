package com.example.socialhour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.services.DBConnection;
import com.example.services.WriteEventToGoogleCalendar;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.firebase.database.DataSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import com.example.services.GenerateMeetingTimes;
import com.google.firebase.database.DataSnapshot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

import com.example.DataTypes.*;

public class GroupsPage extends AppCompatActivity {

    Button createGroup, viewPending;
    static String selectedGroup;
    static DataSnapshot groupsSnap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBConnection dbc = DBConnection.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_page);
        User currentUser = dbc.getCurrentUser();

        createGroup = (Button) findViewById(R.id.createGroup);
        viewPending = (Button) findViewById(R.id.pendingGroups);

        groupsSnap = dbc.getGroupsSnapshot();


        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateGroup.class));
            }
        });

        viewPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<LocalDateTime> times = GenerateMeetingTimes.generateMeetingTime("d4bb10af-9ed3-4426-b3af-bdd019e565a9",12,6,2019, 600, 2330);
                System.out.println(times);
                startActivity(new Intent(getApplicationContext(), PendingGroups.class));
            }
        });


        LinearLayout linearLayout = findViewById(R.id.linLayout2);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        final ArrayList<String> groups = currentUser.getGroups();


        for (int i = 0; i < groups.size(); i++){
            final Button button = new Button(this);
            button.setText(groupsSnap.child(groups.get(i)).child("name").getValue(String.class));
            button.setLayoutParams(params);
            button.setId(i);
            button.setTextSize(30);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int x = button.getId();
                    setSelectedGroup(groups.get(x));
                    startActivity(new Intent(getApplicationContext(),SingleGroupPage.class));
                }
            });

            linearLayout.addView(button);

        }
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null)
        {
            GoogleAccountCredential credential =
                    GoogleAccountCredential.usingOAuth2(
                            this,
                            Collections.singleton(CalendarScopes.CALENDAR));
            credential.setSelectedAccount(account.getAccount());
            Event event = new Event();
            event.setDescription("Test Event");
            event.setSummary("test event");

            Calendar cal = Calendar.getInstance();
            TimeZone timezone = TimeZone.getDefault();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
            df.setTimeZone(timezone);

            cal.set(2019,10,25,23,00, 00);
            Instant startInstant = Instant.parse(df.format(cal.getTime()));
//            startInstant.atOffset(ZoneOffset.ofHours(6));

            Date startTime = Date.from(startInstant);

            cal.set(2019,10,25,23,30, 00);
            Instant endInstant = Instant.parse(df.format(cal.getTime()));
//            startInstant.atOffset(ZoneOffset.ofHours(6));
            Date endTime = Date.from(endInstant);


            String startDateISO = df.format(startTime);
            String endDateISO = df.format(endTime);

            DateTime startDateTime = new DateTime(startDateISO);
            DateTime endDateTime = new DateTime(endDateISO);

//            System.out.println(startDateISO);
            System.out.println(startTime);
            event.setStart(new EventDateTime()
                    .setDateTime(startDateTime));
            event.setEnd(new EventDateTime()
                    .setDateTime(endDateTime));

//            event.setStart(new EventDateTime()
//                    .setDate(new DateTime(true, startTime.getTime(), timezone.getRawOffset())));
//            event.setEnd(new EventDateTime()
//                    .setDate(new DateTime(true, endTime.getTime(), timezone.getRawOffset())));
            GoogleEventContainer container = new GoogleEventContainer(credential, event);
            new WriteEventToGoogleCalendar().execute(container);
        }
    }

    public void setSelectedGroup(String id){
        selectedGroup = id;
    }


    //Change once events field is populated in db
    public static Group getSelectedGroup(){
        Group returnGroup = new Group(groupsSnap.child(selectedGroup).child("name").getValue(String.class), selectedGroup, new ArrayList<String>(),
                (ArrayList<String>)groupsSnap.child(selectedGroup).child("members").getValue());
        return returnGroup;
    }


}
