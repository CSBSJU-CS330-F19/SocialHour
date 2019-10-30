package com.example.DataTypes;

public class Groups {

    private String name;
    private String id;
    private String[] events;
    private String[] members;


    public String getGroupName() {
        return name;
    }

    public String setGroupId(String groupName) {
        this.name = groupName;
    }

    public String getID() {
        return this.id;
    }

    public String setGroupID(String groupID) {
        this.id = groupID;
    }

    public String[] getGroupEvents() {
        return events;
    }

    public void setGroupEvents(String[] events) {
        this.events = events;
    }

    public String[] getGroupMembers() {
        return members;
    }

    public void setGroupMembers(String[] members) {
        this.members = members;
    }
}
