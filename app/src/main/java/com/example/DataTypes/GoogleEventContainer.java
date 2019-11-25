package com.example.DataTypes;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.calendar.model.Event;

public class GoogleEventContainer {

    public GoogleEventContainer(GoogleAccountCredential cred, Event tempEvent)
    {
        credential = cred;
        event = tempEvent;
    }

    public GoogleAccountCredential credential;
    public Event event;
}
