package com.example.services;

import android.util.Log;

import com.example.DataTypes.SocialHourEvent;
import com.example.DataTypes.Group;
import com.example.DataTypes.User;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DBConnection {

    private static DBConnection dbConnection;
    private DatabaseReference db;
    private User currentUser;
    private DataSnapshot userDataSnapshot;
    private DataSnapshot groupsSnapshot;

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
                DataSnapshot userSnap = dataSnapshot.child("Users").child(userKey);
                dbConnection.currentUser = new User(userSnap.child("firstName").getValue().toString(), userSnap.child("email").getValue().toString(), userSnap.child("password").getValue().toString(),
                        (ArrayList<String>) userSnap.child("Groups").getValue(), (ArrayList<String>) userSnap.child("pendingGroups").getValue());
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

    public void getEventTimes(String userID){
        DataSnapshot allEvents = dbConnection.userDataSnapshot.child(userID).child("events");
        //System.out.println("HELLLOOOOOOOOOOOO");
        for(DataSnapshot d : allEvents.getChildren()){
            EventDateTime startTime = d.child("start").child("dateTime").getValue(EventDateTime.class);
            EventDateTime endTime = d.child("end").child("dateTime").getValue(EventDateTime.class);
            System.out.println("Start Time: " + startTime);
            System.out.println("End Time: " + endTime);
        }
    }

    /*add a new event entry into the database*/
    public void addEventToDB(SocialHourEvent newSocialHourEvent){
        dbConnection.db.child("Events").child(newSocialHourEvent.getEventID()).setValue(newSocialHourEvent);
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
}
