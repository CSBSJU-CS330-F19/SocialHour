package com.example.services;

import android.os.AsyncTask;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;

public class ImportCalendar extends AsyncTask<GoogleAccountCredential, Void, Void> {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "Social Hour";
    private final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    @Override
    protected Void doInBackground(GoogleAccountCredential... credentials) {
        try {
            GoogleAccountCredential credential = credentials[0];
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            Events events = service.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(new DateTime(System.currentTimeMillis()))
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            List<Event> items = events.getItems();


            String username = credential.getSelectedAccountName()
                    .substring(0, credential.getSelectedAccountName().indexOf("@"));
            DatabaseReference userEventsRef = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(username)
                    .child("Events");

            userEventsRef.setValue(items);

            int num = 0;
            for (Event i : items){
                userEventsRef.child(String.valueOf(num))
                        .child("StringTimes")
                        .child("start")
                        .setValue(i.getStart().getDateTime().toString());
                userEventsRef.child(String.valueOf(num))
                        .child("StringTimes")
                        .child("end")
                        .setValue(i.getEnd().getDateTime().toString());
                num++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
