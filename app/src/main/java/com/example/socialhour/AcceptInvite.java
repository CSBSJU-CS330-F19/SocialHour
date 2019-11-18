package com.example.socialhour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.services.DBConnection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AcceptInvite extends AppCompatActivity {

    private Button accept, refuse;
    DBConnection dbc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_invite);

        DataSnapshot groupsSnap = LogOn.dbc.getGroupsSnapshot();
        String id =  PendingGroups.selectedGroup;
        String displayName = groupsSnap.child(id).child("name").getValue(String.class);

        TextView name = findViewById(R.id.textView10);
        name.setText(displayName);

        accept = findViewById(R.id.button);
        refuse = findViewById(R.id.button2);
        dbc = LogOn.dbc;

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> pendingGroups = dbc.getCurrentUser().getPendingGroups();
                pendingGroups.remove(PendingGroups.selectedGroup);
                dbc.acceptInvite(PendingGroups.selectedGroup, pendingGroups);
                startActivity(new Intent(getApplicationContext(), GroupsPage.class));
            }
        });

        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                //removeFromPending(String.valueOf(R.id.textView10));
                ArrayList<String> pendingGroups = dbc.getCurrentUser().getPendingGroups();
                pendingGroups.remove(PendingGroups.selectedGroup);
                dbc.refuseInvite(pendingGroups);
                startActivity(new Intent(getApplicationContext(), GroupsPage.class));
            }
        });



    }


}
