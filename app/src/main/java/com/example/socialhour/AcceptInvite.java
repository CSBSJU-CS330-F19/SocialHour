package com.example.socialhour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.services.DBConnection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AcceptInvite extends AppCompatActivity {

    private Button accept, refuse;
    DBConnection dbc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_invite);

        accept = findViewById(R.id.button);
        refuse = findViewById(R.id.button2);
        dbc = LogOn.dbc;

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), ""+R.id.textView10,
                        Toast.LENGTH_LONG).show();
                //moveFromPendingToGroups(String.valueOf(R.id.textView10));
               // addUserToGroup(MainActivity.name);
                //startActivity(new Intent(getApplicationContext(), PendingGroups.class));
            }
        });

        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                //removeFromPending(String.valueOf(R.id.textView10));
                startActivity(new Intent(getApplicationContext(), PendingGroups.class));
            }
        });



    }

    /*public void moveFromPendingToGroups(String group){
        databaseReference.child("Users").child("groups").child("name").setValue(group);
        databaseReference.child("Users").child("pendingGroups").child(group).removeValue();

    }

    public void addUserToGroup(String username){
        databaseReference.child("Groups").child("members").setValue(username);
        Toast.makeText(this, "User successfully added to Group", Toast.LENGTH_LONG)
                .show();
    }

    public void removeFromPending(String groupToRemove){
        databaseReference.child("Users").child("pendingGroups").child(groupToRemove).removeValue();
        Toast.makeText(this, "Group removed from the list of pending groups",
                Toast.LENGTH_LONG).show();
    }*/


}
