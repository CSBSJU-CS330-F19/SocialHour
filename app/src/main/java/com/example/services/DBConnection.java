package com.example.services;

import android.util.Log;

import com.example.DataTypes.Group;
import com.example.DataTypes.User;
import com.example.socialhour.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DBConnection {

    private DatabaseReference db;
    private User currentUser;
    private DataSnapshot userDataSnapshot;
    private DataSnapshot groupsSnapshot;

    public DBConnection(){
        this.db = FirebaseDatabase.getInstance().getReference();
    }

    /*Adds a new user entry in the database. Takes a pre-made user as a parameter.
    * The user is stored in the database as "Users/userKey/userObject"
    */
    public void addUserToDB(User newUser){
        String uniqueId = User.getUserKey(newUser.getEmail());
        db.child("Users").child(uniqueId).setValue(newUser);
    }

    /*Adds a new group entry in the database, takes a group and a unique id.
    * The group is stored in the database as "Groups/uuid/group object"
    */
    public void addGroupToDB(Group newGroup, String uuID){
        db.child("Groups").child(uuID).setValue(newGroup);
    }

    /*adds a group id to the groups child of a user object in the database.
    * Stored at "Users/userKey/Groups
    */
    public void addGroupToUser(User user){
        db.child("Users").child(User.getUserKey(user.getEmail())).child("Groups").setValue(user.getGroups());
    }

    /*adds a group id to the pendingGroups child of a user object in the database.
     * Stored at "Users/userKey/pendingGroups
     */
    public void addPendingGroupToUser(String userID, ArrayList<String> pendingGroups){
        db.child("Users").child(userID).child("pendingGroups").setValue(pendingGroups);
    }

    /*returns a users pending groups
     */
    public ArrayList<String> getPendingGroups(String userID){
        return (ArrayList<String>) userDataSnapshot.child(userID).child("pendingGroups").getValue();
    }

    /*adds a user key to a group object by taking a new group that includes the new member
     * Stored at "Groups/uuid/members"
     */
    public void addUserToGroup(Group group){
        db.child("Groups").child(group.getId()).child("members").setValue(group.getMembers());
    }

    /*sets a new pending group child in the db of the user object that no longer includes the accepted group.
    * adds the group id to the group child in the db.
    * Calls addUserToGroup to add the current user to the group that invited them
    */
    public void acceptInvite (String groupID, ArrayList<String> pendingGroups){
        db.child("Users").child(User.getUserKey(currentUser.getEmail())).child("pendingGroups").setValue(pendingGroups);
        ArrayList<String> groups = currentUser.getGroups();
        if (groups == null){
            groups = new ArrayList<String>();
        }
        groups.add(groupID);
        currentUser.setGroups(groups);
        db.child("Users").child(User.getUserKey(currentUser.getEmail())).child("Groups").setValue(currentUser.getGroups());
        Group newGroup = getGroup(groupID);
        newGroup.addUser(User.getUserKey(currentUser.getEmail()));
        addUserToGroup(newGroup);
    }

    /* removes the rejected group from the users list of pending groups
    */
    public void refuseInvite (ArrayList<String> pendingGroups){
        db.child("Users").child(User.getUserKey(currentUser.getEmail())).child("pendingGroups").setValue(pendingGroups);
    }

    /*returns the requested group object from the db.
    */
    public Group getGroup (String groupId){
        Group retGroup = new Group(groupsSnapshot.child(groupId).child("name").getValue().toString(),
                groupsSnapshot.child(groupId).child("id").getValue().toString(),
                (ArrayList<String>) groupsSnapshot.child(groupId).child("events").getValue(),
                (ArrayList<String>) groupsSnapshot.child(groupId).child("members").getValue());
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
                currentUser = new User(userSnap.child("firstName").getValue().toString(), userSnap.child("email").getValue().toString(), userSnap.child("password").getValue().toString(),
                        (ArrayList<String>) userSnap.child("Groups").getValue(), (ArrayList<String>) userSnap.child("pendingGroups").getValue());
                setCurrentUser(currentUser);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        db.addValueEventListener(readDB);
    }

    public void setCurrentUser (User u){
        currentUser = u;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public void setUserDataSnapshot (DataSnapshot d){
        userDataSnapshot = d;
    }

    public DataSnapshot getUserDataSnapshot(){
        return userDataSnapshot;
    }

    public void setGroupsSnapshot (DataSnapshot d){
        groupsSnapshot = d;
    }

    public DataSnapshot getGroupsSnapshot(){
        return groupsSnapshot;
    }
}
