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

    public void addUserToDB(User newUser){
        int indexOfAt = newUser.getEmail().indexOf("@");
        final String uniqueId = newUser.getEmail().substring(0, indexOfAt).replace('.', '-');
        db.child("Users").child(uniqueId).setValue(newUser);
    }

    public void addGroupToDB(Group newGroup, String uuID){
        db.child("Groups").child(uuID).setValue(newGroup);
    }

    public void addGroupToUser(User user, String groupID){
        db.child("Users").child(User.getUserKey(user.getEmail())).child("Groups").setValue(user.getGroups());
    }

    public void addPendingGroupToUser(String userID, ArrayList<String> pendingGroups){
        db.child("Users").child(userID).child("pendingGroups").setValue(pendingGroups);
    }

    public ArrayList<String> getPendingGroups(String userID){
        return (ArrayList<String>) userDataSnapshot.child(userID).child("pendingGroups").getValue();
    }

    public void addUserToGroup(String userID, Group group){
        db.child("Groups").child(group.getId()).child("members").setValue(group.getMembers());
    }

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
        addUserToGroup(User.getUserKey(currentUser.getEmail()), newGroup);
    }

    public void refuseInvite (String groupID, ArrayList<String> pendingGroups){
        db.child("Users").child(User.getUserKey(currentUser.getEmail())).child("pendingGroups").setValue(pendingGroups);
    }

    public Group getGroup (String groupId){
        Group retGroup = new Group(groupsSnapshot.child(groupId).child("name").getValue().toString(),
                groupsSnapshot.child(groupId).child("id").getValue().toString(),
                (ArrayList<String>) groupsSnapshot.child(groupId).child("events").getValue(),
                (ArrayList<String>) groupsSnapshot.child(groupId).child("members").getValue());
        return retGroup;
    }

    public void getUser(String email){
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
                System.out.println("name : " + currentUser.getFirstName());
                System.out.println("email : " + currentUser.getEmail());
                System.out.println("password : " + currentUser.getPassword());
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
        printData();
    }

    public void printData(){
        System.out.println("name : " + currentUser.getFirstName());
        System.out.println("email : " + currentUser.getEmail());
        System.out.println("password : " + currentUser.getPassword());
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
