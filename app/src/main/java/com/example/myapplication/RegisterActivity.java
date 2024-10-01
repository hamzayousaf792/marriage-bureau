package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;  // Add this import for Button
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputUsername, inputEmail, inputPassword, inputRePassword;
    private Button btnRegister, btnGoToLogin;  // Add btnGoToLogin here
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Firebase Authentication instance
        auth = FirebaseAuth.getInstance();

        // Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        inputUsername = findViewById(R.id.username);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputRePassword = findViewById(R.id.re_enter_password);
        btnRegister = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);

        // Reference for the "Go to Login" button
        btnGoToLogin = findViewById(R.id.btn_goto_login);

        // Click listener for "Go to Login" button
        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = inputUsername.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String rePassword = inputRePassword.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    inputUsername.setError("Enter a username");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError("Enter email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    inputPassword.setError("Enter password");
                    return;
                }

                if (password.length() < 6) {
                    inputPassword.setError("Password too short, enter minimum 6 characters");
                    return;
                }

                if (!password.equals(rePassword)) {
                    inputRePassword.setError("Passwords do not match");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Create user with email and password using Firebase Auth
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, task -> {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                // Registration success
                                FirebaseUser user = auth.getCurrentUser();
                                if (user != null) {
                                    String userId = user.getUid();

                                    // Save additional user info in Firebase Realtime Database
                                    HashMap<String, String> userMap = new HashMap<>();
                                    userMap.put("username", username);
                                    userMap.put("email", email);
                                    userMap.put("Password", password);

                                    // Save to Realtime Database under 'Users/userId'
                                    databaseReference.child(userId).setValue(userMap)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                                                    // Redirect to MainActivity
                                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();  // Close the current activity
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, "Failed to save user info in database", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}
