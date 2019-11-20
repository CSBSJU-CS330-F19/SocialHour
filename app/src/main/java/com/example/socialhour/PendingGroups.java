package com.example.socialhour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.*;
import com.example.DataTypes.User;
import com.example.services.DBConnection;
import com.google.firebase.database.DataSnapshot;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PendingGroups extends AppCompatActivity {

    static String selectedGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBConnection dbc = DBConnection.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_groups);
        DataSnapshot GroupsSnap = dbc.getGroupsSnapshot();

        LinearLayout linearLayout = findViewById(R.id.linear_layout);

        User currentUser = dbc.getCurrentUser();
        final ArrayList<String> pendingGroups = currentUser.getPendingGroups();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < pendingGroups.size(); i++){
            final Button button = new Button(this);
            button.setText(GroupsSnap.child(pendingGroups.get(i)).child("name").getValue(String.class));
            button.setLayoutParams(params);
            button.setTextSize(30);
            button.setId(i);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int x = button.getId();
                    setSelectedGroup(pendingGroups.get(x));
                    startActivity(new Intent(getApplicationContext(),AcceptInvite.class));
                }
            });

            linearLayout.addView(button);

        }
    }

    public void setSelectedGroup(String id){
        selectedGroup = id;
    }
}
