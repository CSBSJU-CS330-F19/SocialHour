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

public class GroupsPage extends AppCompatActivity {

    Button createGroup, viewPending, group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBConnection dbc = LogOn.dbc;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_page);

        createGroup = (Button) findViewById(R.id.createGroup);
        viewPending = (Button) findViewById(R.id.pendingGroups);

        //System.out.println("NAME: " + dbc.getCurrentUser().getFirstName());
        DataSnapshot groupsSnap = dbc.getGroupsSnapshot();
        for(DataSnapshot snapshot : groupsSnap.getChildren()){
            System.out.println(snapshot.child("name").getValue(String.class));
        }

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

        ArrayList<String> groups = new ArrayList<String>(Arrays.asList("Group1", "Group2", "Group3", "Group4", "Group5"
                , "Group2", "Group3", "Group4", "Group5", "Group2", "Group3", "Group4", "Group5"));


        for (int i = 0; i < groups.size(); i++){
            Button button = new Button(this);
            button.setText(groups.get(i));
            button.setLayoutParams(params);
            button.setId(i);
            button.setTextSize(30);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),SingleGroupPage.class));
                }
            });

            linearLayout.addView(button);

        }
    }


}
