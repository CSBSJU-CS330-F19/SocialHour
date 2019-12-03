package com.example.services;

import android.util.Log;

import com.example.DataTypes.SocialHourEvent;
import com.example.DataTypes.Group;
import com.example.DataTypes.User;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DBConnection {

    private static DBConnection dbConnection;
    private DatabaseReference db;
    private User currentUser;
    private DataSnapshot userDataSnapshot;
    private DataSnapshot groupsSnapshot;
    private DataSnapshot eventDataSnapshot;

    public static DBConnection getInstance(){
        if(dbConnection == null){
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }
    private DBConnection(){
        this.db = FirebaseDatabase.getInstance().getReference();
    }

    /*Adds a new user entry in the database. Takes a pre-made user as a parameter.
    * The user is stored in the database as "Users/userKey/userObject"
    */
    public void addUserToDB(User newUser){
        String uniqueId = User.getUserKey(newUser.getEmail());
        dbConnection.db.child("Users").child(uniqueId).setValue(newUser);
    }

    /*Adds a new group entry in the database, takes a group and a unique id.
    * The group is stored in the database as "Groups/uuid/group object"
    */
    public void addGroupToDB(Group newGroup, String uuID){
        dbConnection.db.child("Groups").child(uuID).setValue(newGroup);
    }

    /*adds a group id to the groups child of a user object in the database.
    * Stored at "Users/userKey/Groups
    */
    public void addGroupToUser(User user){
        dbConnection.db.child("Users").child(User.getUserKey(user.getEmail())).child("Groups").setValue(user.getGroups());
    }

    /*adds a group id to the pendingGroups child of a user object in the database.
     * Stored at "Users/userKey/pendingGroups
     */
    public void addPendingGroupToUser(String userID, ArrayList<String> pendingGroups){
        dbConnection.db.child("Users").child(userID).child("pendingGroups").setValue(pendingGroups);
    }

    /*returns a users pending groups
     */
    public ArrayList<String> getPendingGroups(String userID){
        return (ArrayList<String>) dbConnection.userDataSnapshot.child(userID).child("pendingGroups").getValue();
    }

    /*adds a user key to a group object by taking a new group that includes the new member
     * Stored at "Groups/uuid/members"
     */
    public void addUserToGroup(Group group){
        dbConnection.db.child("Groups").child(group.getId()).child("members").setValue(group.getMembers());
    }

    /*sets a new pending group child in the db of the user object that no longer includes the accepted group.
    * adds the group id to the group child in the db.
    * Calls addUserToGroup to add the current user to the group that invited them
    */
    public void acceptInvite (String groupID, ArrayList<String> pendingGroups){
        dbConnection.db.child("Users").child(User.getUserKey(dbConnection.currentUser.getEmail())).child("pendingGroups").setValue(pendingGroups);
        ArrayList<String> groups = dbConnection.currentUser.getGroups();
        if (groups == null){
            groups = new ArrayList<String>();
        }
        groups.add(groupID);
        dbConnection.currentUser.setGroups(groups);
        dbConnection.db.child("Users").child(User.getUserKey(dbConnection.currentUser.getEmail())).child("Groups").setValue(dbConnection.currentUser.getGroups());
        Group newGroup = getGroup(groupID);
        newGroup.addUser(User.getUserKey(dbConnection.currentUser.getEmail()));
        addUserToGroup(newGroup);
    }

    /* removes the rejected group from the users list of pending groups
    */
    public void refuseInvite (ArrayList<String> pendingGroups){
        dbConnection.db.child("Users").child(User.getUserKey(dbConnection.currentUser.getEmail())).child("pendingGroups").setValue(pendingGroups);
    }

    /*returns the requested group object from the db.
    */
    public Group getGroup (String groupId){
        Group retGroup = new Group(dbConnection.groupsSnapshot.child(groupId).child("name").getValue().toString(),
                dbConnection.groupsSnapshot.child(groupId).child("id").getValue().toString(),
                (ArrayList<String>) dbConnection.groupsSnapshot.child(groupId).child("events").getValue(),
                (ArrayList<String>) dbConnection.groupsSnapshot.child(groupId).child("members").getValue());
        return retGroup;
    }

    /*sets the current user object and updates the user object whenever the db is changed*/
    public void updateCurrentUser(String email){
        final String userKey = User.getUserKey(email);
        ValueEventListener readDB = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                setGroupsSnapshot(dataSnapshot.child("Groups"));
                setUserDataSnapshot(dataSnapshot.child("Users"));
                setEventDataSnapshot(dataSnapshot.child("Events"));
                DataSnapshot userSnap = dataSnapshot.child("Users").child(userKey);
                dbConnection.currentUser = new User(userSnap.child("firstName").getValue().toString(), userSnap.child("email").getValue().toString(), userSnap.child("password").getValue().toString(),
                        (ArrayList<String>) userSnap.child("Groups").getValue(), (ArrayList<String>) userSnap.child("pendingGroups").getValue(),
                        (ArrayList<String>) userSnap.child("SocialHourEvents").getValue(), (ArrayList<String>) userSnap.child("PendingSocialHourEvents").getValue());
                setCurrentUser(dbConnection.currentUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        dbConnection.db.addValueEventListener(readDB);
    }

    public ArrayList<LocalDateTime> getEventStartTimes(String userID){
        DataSnapshot allEvents = dbConnection.userDataSnapshot.child(userID).child("Events");
        ArrayList<LocalDateTime> retList = new ArrayList<>();
        for(DataSnapshot d : allEvents.getChildren()){
            String startTime = d.child("StringTimes").child("start").getValue().toString();
            int newHours = Integer.parseInt(startTime.substring(11, 13));
            LocalDateTime t = LocalDateTime.of(Integer.parseInt(startTime.substring(0,4)),
                                                Integer.parseInt(startTime.substring(5,7)),
                                                Integer.parseInt(startTime.substring(8, 10)),
                                                newHours,
                                                Integer.parseInt(startTime.substring(14, 16)));
            retList.add(t);
        }
        return retList;
    }

    public ArrayList<LocalDateTime> getEventEndTimes(String userID){
        DataSnapshot allEvents = dbConnection.userDataSnapshot.child(userID).child("Events");
        ArrayList<LocalDateTime> retList = new ArrayList<>();
        for(DataSnapshot d : allEvents.getChildren()){
            String endTime = d.child("StringTimes").child("end").getValue().toString();
            int newHours = Integer.parseInt(endTime.substring(11, 13));
            LocalDateTime t = LocalDateTime.of(Integer.parseInt(endTime.substring(0,4)),
                    Integer.parseInt(endTime.substring(5,7)),
                    Integer.parseInt(endTime.substring(8, 10)),
                    newHours,
                    Integer.parseInt(endTime.substring(14, 16)));
            retList.add(t);
        }
        return retList;
    }

    /*add a new event entry into the database*/
    public void addEventToDB(SocialHourEvent newSocialHourEvent){

        String start = newSocialHourEvent.getStartTime().toString();
        String end = newSocialHourEvent.getEndTime().toString();
        int oldHours = Integer.parseInt(start.substring(11, 13));
        if(oldHours < 18) {
            int newHours = oldHours + 6;
            String oldHourString = Integer.toString(oldHours);
            String newHourString = Integer.toString(newHours);
            if (oldHours < 10) {
                oldHourString = "0" + oldHourString;
            }
            if (newHours < 10) {
                newHourString = "0" + newHourString;
            }
            start = start.replace("T" + oldHourString, "T" + newHourString);
            start = start.replace("-06:00", "");
            System.out.println(start);
            int eOldHours = Integer.parseInt(end.substring(11, 13));
            int eNewHours = eOldHours + 6;
            String eOldHourString = Integer.toString(eOldHours);
            String eNewHourString = Integer.toString(eNewHours);
            if (eOldHours < 10) {
                eOldHourString = "0" + eOldHourString;
            }
            if (eNewHours < 10) {
                eNewHourString = "0" + eNewHourString;
            }
            end = end.replace("T" + eOldHourString, "T" + eNewHourString);
            end = end.replace("-06:00", "");
            System.out.println(end);
        }
        else{
            start = start.replace("-6:00", "");
            end = end.replace("-6:00", "");
        }
        dbConnection.db.child("Events").child(newSocialHourEvent.getEventID()).setValue(newSocialHourEvent);
        dbConnection.db.child("Events").child(newSocialHourEvent.getEventID()).child("StringTimes").child("start").setValue(start);
        dbConnection.db.child("Events").child(newSocialHourEvent.getEventID()).child("StringTimes").child("end").setValue(end);
        ArrayList<String> userEvents = getUserSocialHourEvents(User.getUserKey(currentUser.getEmail()));
        userEvents.add(newSocialHourEvent.getEventID());
        dbConnection.db.child("Users").child(User.getUserKey(currentUser.getEmail())).child("SocialHourEvents").setValue(userEvents);
        ArrayList<String> groupEvents = getGroupSocialHourEvents(newSocialHourEvent.getGroupId());
        groupEvents.add(newSocialHourEvent.getEventID());
        dbConnection.db.child("Groups").child(newSocialHourEvent.getGroupId()).child("SocialHourEvents").setValue(groupEvents);
        inviteAllMembersToEvent(newSocialHourEvent);
    }

    public void inviteAllMembersToEvent(SocialHourEvent newSocialHourEvent){
        ArrayList<String> members = getAllMembersOfGroup(newSocialHourEvent.getGroupId());
        String curID = User.getUserKey(currentUser.getEmail());
        for(String m : members){
            if(!m.equals(curID)){
                ArrayList<String> mEvents = getPendingSocialHourEvents(m);
                mEvents.add(newSocialHourEvent.getEventID());
                dbConnection.db.child("Users").child(m).child("PendingSocialHourEvents").setValue(mEvents);
            }
        }
    }

    public ArrayList<String> getAllMembersOfGroup(String groupID){
        ArrayList<String> allMembers = (ArrayList<String>) groupsSnapshot.child(groupID).child("members").getValue();
        return allMembers;
    }

    public ArrayList<String> getUserSocialHourEvents(String userId){
        ArrayList<String> events = (ArrayList<String>) userDataSnapshot.child(userId).child("SocialHourEvents").getValue();
        if (events != null)
            return events;
        else
            return new ArrayList<String>();
    }

    public ArrayList<String> getPendingSocialHourEvents(String userId){
        ArrayList<String> events = (ArrayList<String>) userDataSnapshot.child(userId).child("PendingSocialHourEvents").getValue();
        if (events != null)
            return events;
        else
            return new ArrayList<String>();
    }

    public ArrayList<String> getGroupSocialHourEvents(String groupId){
        ArrayList<String> events = (ArrayList<String>) groupsSnapshot.child(groupId).child("SocialHourEvents").getValue();
        if (events != null)
            return events;
        else
            return new ArrayList<String>();
    }

    public void setCurrentUser (User u){
        dbConnection.currentUser = u;
    }

    public User getCurrentUser(){
        return dbConnection.currentUser;
    }

    public void setUserDataSnapshot (DataSnapshot d){
        dbConnection.userDataSnapshot = d;
    }

    public DataSnapshot getUserDataSnapshot(){
        return dbConnection.userDataSnapshot;
    }

    public void setGroupsSnapshot (DataSnapshot d){
        dbConnection.groupsSnapshot = d;
    }

    public DataSnapshot getGroupsSnapshot(){
        return dbConnection.groupsSnapshot;
    }

    public void setEventDataSnapshot (DataSnapshot d){
        dbConnection.eventDataSnapshot = d;
    }

    public DataSnapshot getEventDataSnapshot(){
        return dbConnection.eventDataSnapshot;
    }
}
