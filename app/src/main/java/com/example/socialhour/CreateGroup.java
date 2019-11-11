package com.example.socialhour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.DataTypes.Group;
import com.example.DataTypes.User;
import com.example.services.DBConnection;
import java.util.UUID;

public class CreateGroup extends AppCompatActivity {

    private DBConnection dbConnection;

    private Button makeGroup;
    private EditText groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        makeGroup = findViewById(R.id.createGroup);
        groupName = findViewById(R.id.editText);
        dbConnection = LogOn.dbc;



        makeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User currentUser = dbConnection.getCurrentUser();

                String uniqueID = User.getUserKey(currentUser.getEmail());

                String nameOfGroup = groupName.getText().toString();

                String groupID = UUID.randomUUID().toString();

                Group newGroup = new Group(nameOfGroup, groupID);

                newGroup.addUser(uniqueID);
                currentUser.addGroup(groupID);

                dbConnection.addGroupToUser(currentUser);
                dbConnection.addUserToGroup(newGroup);

                dbConnection.addGroupToDB(newGroup, groupID);

                startActivity(new Intent(getApplicationContext(), GroupsPage.class));

            }
        });
    }
}
