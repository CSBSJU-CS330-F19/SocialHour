package com.example.socialhour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import android.os.Bundle;
import android.widget.Toast;

import com.example.DataTypes.User;
import com.example.services.DBConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class CreateAccount extends AppCompatActivity {
    static String name, email, password;

    private EditText nameCreate, emailCreate, passwordCreate;
    private Button submitCreate;

    private FirebaseAuth mAuth;
    private DBConnection dbc;
    private User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        nameCreate = findViewById(R.id.nameBox);
        emailCreate = findViewById(R.id.emailBox);
        passwordCreate = findViewById(R.id.passwordBox);

        submitCreate = findViewById(R.id.submitButton);
        dbc = DBConnection.getInstance();
        mAuth = FirebaseAuth.getInstance();

        submitCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameCreate.getText().toString().trim();
                email = emailCreate.getText().toString().trim();
                password = passwordCreate.getText().toString().trim();
                newUser = new User(name, email, password);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    dbc.addUserToDB(newUser);
                                    Toast.makeText(CreateAccount.this,
                                            "Your account has been successfully created",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), LogOn.class));

                                } else {
                                    Toast.makeText(CreateAccount.this,
                                            "Account already exists with that email address",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}
