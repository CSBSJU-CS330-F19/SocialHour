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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBConnection dbc = LogOn.dbc;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_groups);
        DataSnapshot pGroupsSnap = dbc.getGroupsSnapshot();

        LinearLayout linearLayout = findViewById(R.id.linear_layout);

        User currentUser = dbc.getCurrentUser();
        ArrayList<String> pendingGroups = currentUser.getPendingGroups();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < pendingGroups.size(); i++){
            Button button = new Button(this);
            button.setText(pGroupsSnap.child(pendingGroups.get(i)).child("name").getValue(String.class));
            button.setLayoutParams(params);
            button.setTextSize(30);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),AcceptInvite.class));
                }
            });

            linearLayout.addView(button);

        }
    }
}
