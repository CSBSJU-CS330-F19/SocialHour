package com.example.socialhour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.DataTypes.Group;
import com.example.DataTypes.SocialHourEvent;
import com.example.DataTypes.User;
import com.example.services.DBConnection;
import com.example.services.WriteEventToGoogleCalendar;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class ViewGenMeetingTimes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_gen_meeting_times);
        final DBConnection dbc = DBConnection.getInstance();

        TextView message = findViewById(R.id.messageText);
        message.setText("Choose which time you would like the event : " + InputGenerateMeetingTimes.eventNameText + " to start");

        LinearLayout linearLayout = findViewById(R.id.generatedTimes);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        final ArrayList<LocalDateTime> times = InputGenerateMeetingTimes.times;


        for (int i = 0; i < times.size(); i++){
            final Button button = new Button(this);
            button.setText(times.get(i).toString().replace("T", " : "));
            button.setLayoutParams(params);
            button.setId(i);
            button.setTextSize(30);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int x = button.getId();
                    LocalDateTime l = times.get(x);
                    LocalDateTime end = l.plusHours(InputGenerateMeetingTimes.lenHour).plusMinutes(InputGenerateMeetingTimes.lenMin);
                    String dtString = l.toString().concat(":00.000");
                    String dtEndString = end.toString().concat(":00.000");

                    DateTime startTime = new DateTime(dtString);
                    DateTime endTime = new DateTime(dtEndString);

                    String name = InputGenerateMeetingTimes.eventNameText;

                    User currentUser = dbc.getCurrentUser();
                    String uniqueID = User.getUserKey(currentUser.getEmail());

                    Group currentGroup = SingleGroupPage.selectedGroup;
                    String groupID = currentGroup.getId();

                    String eventID = UUID.randomUUID().toString();

                    ArrayList<String> attendees = new ArrayList<>();

                    attendees.add(uniqueID);

                    SocialHourEvent newSocialHourEvent = new SocialHourEvent(name, startTime, endTime, groupID, eventID, attendees);


                    writeToCal(newSocialHourEvent);


                    dbc.addEventToDB(newSocialHourEvent);

                    startActivity(new Intent(getApplicationContext(), GroupsPage.class));
                }
            });

            linearLayout.addView(button);

        }
    }

    public void writeToCal(SocialHourEvent s){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(this, Collections.singleton(CalendarScopes.CALENDAR));
            credential.setSelectedAccount(account.getAccount());
            new WriteEventToGoogleCalendar(credential, s).execute();
        }
    }
}
