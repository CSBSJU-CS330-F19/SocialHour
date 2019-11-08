package com.example.socialhour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.DataTypes.*;
import com.example.services.DBConnection;

import java.util.ArrayList;

public class SingleGroupPage extends AppCompatActivity {

    Button addMember;
    Group selectedGroup;
    DBConnection dbc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_group_page);
        LinearLayout groupMembers = findViewById(R.id.groupMembers);
        dbc = LogOn.dbc;

        addMember = findViewById(R.id.pendingGroups);

        selectedGroup = GroupsPage.getSelectedGroup();
        System.out.println(selectedGroup.getName());
        System.out.println(selectedGroup.getMembers().get(0));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        ArrayList<String> members = selectedGroup.getMembers();
        TextView member = new TextView(this);
        for(String name: members){
            member.setText(name);
            member.setLayoutParams(params);
            member.setTextSize(20);
            groupMembers.addView(member);
        }

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///send a request
                //read in name entered
                //dbc.add group to user(User, String group id)
                //dbc.
                //make methods in the dbc class
            }
        });
    }
}
