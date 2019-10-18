package com.example.socialhour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccount extends AppCompatActivity {
    String name, email, password;

    EditText nameCreate, emailCreate, passwordCreate;
    Button submitCreate;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        nameCreate = (EditText) findViewById(R.id.nameCreate);
        emailCreate = (EditText) findViewById(R.id.emailCreate);
        passwordCreate = (EditText) findViewById(R.id.passwordCreate);

        submitCreate = (Button) findViewById(R.id.submitCreate);

        mAuth = FirebaseAuth.getInstance();

        submitCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameCreate.getText().toString().trim();
                email = emailCreate.getText().toString().trim();
                password = passwordCreate.getText().toString().trim();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                } else {
                                    Toast.makeText(CreateAccount.this, "Account already exists with that email address", Toast.LENGTH_SHORT).show();
                                }
                            }
                            });
            }
        });

    }
}
