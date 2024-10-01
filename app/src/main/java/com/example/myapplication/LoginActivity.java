package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnLogin, btnGoToRegister;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    // Constants for SharedPreferences
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check if user is already logged in
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
        if (isLoggedIn) {
            // User is logged in, redirect to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Firebase Authentication instance
        auth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.login_email);
        inputPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.logbut);
        progressBar = findViewById(R.id.progressBar);

        // Reference for the "Go to Register" button
        btnGoToRegister = findViewById(R.id.btn_goto_register);

        // Click listener for "Go to Register" button
        btnGoToRegister.setOnClickListener(v -> {
            // Navigate to RegisterActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                inputEmail.setError("Enter email");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                inputPassword.setError("Enter password");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            // Authenticate user
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, redirect to MainActivity
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                            // Set logged in status in SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(KEY_IS_LOGGED_IN, true);
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Sign in fails, show error message
                            Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clear progress bar
        progressBar.setVisibility(View.GONE);
    }
}
