package com.example.socialhour;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;



public class LogOn extends AppCompatActivity {

    String username, password;

    EditText usernameInput;
    EditText passwordInput;

    Button logOnButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_on);

        usernameInput = (EditText) findViewById(R.id.username);
        passwordInput = (EditText) findViewById(R.id.password);

        logOnButton = (Button) findViewById(R.id.logOnbutton);

        mAuth = FirebaseAuth.getInstance();

        logOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameInput.getText().toString().trim();
                password = passwordInput.getText().toString().trim();

                mAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(LogOn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    
                                } else {
                                    Toast.makeText(LogOn.this, "Login Failed or User Not Available", Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
            }
        });
    }


}


