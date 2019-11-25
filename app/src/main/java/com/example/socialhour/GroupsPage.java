package com.example.socialhour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.services.DBConnection;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.DataTypes.*;

public class GroupsPage extends AppCompatActivity {

    Button createGroup, viewPending;
    static String selectedGroup;
    static DataSnapshot groupsSnap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBConnection dbc = DBConnection.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_page);
        User currentUser = dbc.getCurrentUser();

        createGroup = (Button) findViewById(R.id.createGroup);
        viewPending = (Button) findViewById(R.id.pendingGroups);

        //dbc.getEventTimes(User.getUserKey(currentUser.getEmail()));

        groupsSnap = dbc.getGroupsSnapshot();


        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateGroup.class));
            }
        });

        viewPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PendingGroups.class));
            }
        });


        LinearLayout linearLayout = findViewById(R.id.linLayout2);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        final ArrayList<String> groups = currentUser.getGroups();


        for (int i = 0; i < groups.size(); i++){
            final Button button = new Button(this);
            button.setText(groupsSnap.child(groups.get(i)).child("name").getValue(String.class));
            button.setLayoutParams(params);
            button.setId(i);
            button.setTextSize(30);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int x = button.getId();
                    setSelectedGroup(groups.get(x));
                    startActivity(new Intent(getApplicationContext(),SingleGroupPage.class));
                }
            });

            linearLayout.addView(button);

        }
    }

    public void setSelectedGroup(String id){
        selectedGroup = id;
    }


    //Change once events field is populated in db
    public static Group getSelectedGroup(){
        Group returnGroup = new Group(groupsSnap.child(selectedGroup).child("name").getValue(String.class), selectedGroup, new ArrayList<String>(),
                (ArrayList<String>)groupsSnap.child(selectedGroup).child("members").getValue());
        return returnGroup;
    }


}
