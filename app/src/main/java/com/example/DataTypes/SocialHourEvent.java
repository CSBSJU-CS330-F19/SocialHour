package com.example.DataTypes;

import com.google.api.client.util.DateTime;

import java.util.ArrayList;

public class SocialHourEvent {

    private String eventName;
    private DateTime startTime;
    private DateTime endTime;
    private String groupId;
    private String eventID;
    private ArrayList<String> attendees = new ArrayList<>();

    public SocialHourEvent(String eventName, DateTime start, DateTime end, String groupId, String eventID, ArrayList<String> attendees) {
        this.eventName = eventName;
        this.startTime = start;
        this.endTime = end;
        this.groupId = groupId;
        this.eventID = eventID;
        if(attendees != null) {
            this.attendees = attendees;
        }
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }


    public String getEventID() {
        return eventID;
    }

    public void setId(String EventID) {
        this.eventID = eventID;
    }

    public ArrayList<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(ArrayList<String> attendees) {
        this.attendees = attendees;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.startTime = dateTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime dateTime) {
        this.endTime = dateTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
