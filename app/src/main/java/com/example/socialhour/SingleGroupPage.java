package com.example.socialhour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DataTypes.*;
import com.example.services.DBConnection;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class SingleGroupPage extends AppCompatActivity {

    Button addMember;
    Group selectedGroup;
    DBConnection dbc;
    private EditText usernameInput;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_group_page);
        LinearLayout groupMembers = findViewById(R.id.groupMembers);
        dbc = DBConnection.getInstance();

        addMember = findViewById(R.id.pendingGroups);
        usernameInput = findViewById(R.id.editText2);

        selectedGroup = GroupsPage.getSelectedGroup();
        System.out.println(selectedGroup.getName());
        System.out.println(selectedGroup.getMembers().get(0));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        ArrayList<String> members = selectedGroup.getMembers();
        for(String name: members){
            TextView member = new TextView(this);
            member.setText(name);
            member.setLayoutParams(params);
            member.setTextSize(20);
            groupMembers.addView(member);
        }

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("HELLO");
                //read in name entered


                try {
                    username = usernameInput.getText().toString().trim();
                    DataSnapshot users = dbc.getUserDataSnapshot();
                    String userId = User.getUserKey(username);
                    for (DataSnapshot user : users.getChildren()) {
                        //System.out.println(user.getValue().toString());
                        if (user.child("email").getValue().toString().equals(username)) {
                            ArrayList<String> currentPendingGroups = dbc.getPendingGroups(userId);
                            if (currentPendingGroups == null) {
                                currentPendingGroups = new ArrayList<String>();
                                currentPendingGroups.add(selectedGroup.getId());
                            } else {
                                if(!currentPendingGroups.contains(selectedGroup.getId())) {
                                    currentPendingGroups.add(selectedGroup.getId());
                                }
                                else{
                                    Toast.makeText(SingleGroupPage.this, "Invite already sent to " + username,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                            //System.out.println("current p Groups: " + currentPendingGroups.get(0));
                            dbc.addPendingGroupToUser(userId, currentPendingGroups);
                            Toast.makeText(SingleGroupPage.this, "An invite has been sent to " + username,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch(Exception e){
                    Toast.makeText(SingleGroupPage.this, "The username you entered does not exist",
                            Toast.LENGTH_SHORT).show();
                }

                //System.out.println("entered a bad username");


            }
        });
    }
}
