package com.example.services;

import android.os.AsyncTask;

import com.example.DataTypes.SocialHourEvent;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;

public class TestClass extends AsyncTask<Void, Void, Void> {
    private GoogleAccountCredential accountCredential;
    private SocialHourEvent socialHourEvent;

    protected static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "Social Hour";
    private final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    public TestClass(GoogleAccountCredential credential, SocialHourEvent event)
    {
        this.accountCredential = credential;
        this.socialHourEvent = event;
    }

    @Override
    protected Void doInBackground(Void... none) {
        try {
            Event googleEvent = new Event();
            googleEvent.setSummary(socialHourEvent.getEventName());

            googleEvent.setStart(new EventDateTime()
                    .setDateTime(socialHourEvent.getStartTime()));
            googleEvent.setEnd(new EventDateTime()
                    .setDateTime(socialHourEvent.getEndTime()));

            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, this.accountCredential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            service.events().insert("primary", googleEvent).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
