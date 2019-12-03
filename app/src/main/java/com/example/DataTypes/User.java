package com.example.DataTypes;

import com.google.api.services.calendar.model.Event;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User {
    private String email;
    private String password;
    private String firstName;
    private ArrayList<String> groups;
    private ArrayList<String> pendingGroups;
    private ArrayList<String> eventsList;
    private ArrayList<String> pendingEvents;
    private ArrayList<Event> CalendarEvents;

    public User(String firstName, String email, String password){
        this.setEmail(email);
        this.setPassword(password);
        this.setFirstName(firstName);
        this.groups = new ArrayList<String>();
        this.pendingGroups = new ArrayList<String>();
    }

    public User(String firstName, String email, String password, ArrayList<String> groups, ArrayList<String> pendingGroups){
        this.setEmail(email);
        this.setPassword(password);
        this.setFirstName(firstName);
        if (groups != null){
            this.setGroups(groups);
        }
        else {
            this.groups = new ArrayList<String>();
        }
        if (pendingGroups != null) {
            this.setPendingGroups(pendingGroups);
        }
        else {
            this.pendingGroups = new ArrayList<String>();
        }
    }

    public User(String frstName, String email, String password, ArrayList<String> groups,
                ArrayList<String> pendingGroups, ArrayList<String> socialHourEvents,
                ArrayList<String> pendingEvents){
        this.setEmail(email);
        this.setPassword(password);
        this.setFirstName(firstName);
        if (groups != null){
            this.setGroups(groups);
        }
        else {
            this.groups = new ArrayList<String>();
        }
        if (pendingGroups != null) {
            this.setPendingGroups(pendingGroups);
        }
        else {
            this.pendingGroups = new ArrayList<String>();
        }
    }

    /*creates a key for a user object based on their email.
    * Firebase does not accept "@" or "." as the name of a node so these are stripped and replaced*/
    public static String getUserKey(String email){
        int indexOfAt = email.indexOf("@");
        return email.substring(0, indexOfAt).replace('.', '-');
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addGroup(String groupID){
        groups.add(groupID);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }

    public ArrayList<String> getPendingGroups() {
        return pendingGroups;
    }

    public void setPendingGroups(ArrayList<String> pendingGroups) {
        this.pendingGroups = pendingGroups;
    }

    public void setEventsList(ArrayList<String> eventsList) { this.eventsList = eventsList; }


    public ArrayList<String> getEventsList() { return eventsList; }

    public void setPendingEvents(ArrayList<String> eventsList) { this.eventsList = eventsList; }

    public ArrayList<String> getPendingEvents() { return eventsList; }
}
