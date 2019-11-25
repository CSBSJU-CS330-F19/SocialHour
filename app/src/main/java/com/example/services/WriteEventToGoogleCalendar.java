package com.example.services;

import android.os.AsyncTask;

import com.example.DataTypes.GoogleEventContainer;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;

import java.io.IOException;

public class WriteEventToGoogleCalendar extends AsyncTask<GoogleEventContainer, Void, Void> {
        protected static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
        private static final String APPLICATION_NAME = "Social Hour";
        private final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();

        @Override
        protected Void doInBackground(GoogleEventContainer... tempContainer) {
            try {
                GoogleEventContainer container = tempContainer[0];

                Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, container.credential)
                        .setApplicationName(APPLICATION_NAME)
                        .build();
                service.events().insert("primary",container.event).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
}

