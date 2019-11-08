package com.example.DataTypes;


import java.util.ArrayList;

public class Group {

    private String name;
    private String id;
    private ArrayList<String> events = new ArrayList<>();
    private ArrayList<String> members = new ArrayList<>();

    public Group(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Group(String name, String id, ArrayList<String> events, ArrayList<String> members) {
        this.name = name;
        this.id = id;
        if(events != null){
            this.events = events;
        }
        if(members != null){
            this.members = members;
        }
    }

    public void addUser(String userID){
        members.add(userID);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<String> events) {
        this.events = events;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }
}
