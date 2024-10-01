package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnAdminLogin;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    // Define a tag for logging
    private static final String TAG = "AdminLoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        // Firebase Authentication instance
        auth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.admin_email);
        inputPassword = findViewById(R.id.admin_password);
        btnAdminLogin = findViewById(R.id.btn_admin_login);
        progressBar = findViewById(R.id.progressBar);

        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty fields
                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError("Enter admin email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    inputPassword.setError("Enter password");
                    return;
                }

                // Check if the email and password are correct
                if (email.equals("hamzayousaf1@gmail.com") && password.equals("hamza098")) {
                    progressBar.setVisibility(View.VISIBLE);

                    // Authenticate admin
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(AdminLoginActivity.this, task -> {
                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    // Log success
                                    Toast.makeText(AdminLoginActivity.this, "Admin login successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AdminLoginActivity.this, AdminPanelActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Log failure
                                    String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                                    Toast.makeText(AdminLoginActivity.this, "Login failed: " + errorMessage, Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "Login failed: " + errorMessage); // Log the error
                                }
                            });
                } else {
                    Toast.makeText(AdminLoginActivity.this, "Only authorized admin can log in.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
