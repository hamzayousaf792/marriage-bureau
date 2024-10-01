package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etAge, etWeight, etHeight, etNationality, etCaste, etReligion,
            etEducationalDetail, etIslamicEducation, etFatherName, etFatherOccupation,
            etSiblings, etPhoneNumber;
    private Button btnSubmit, btnAdminLogin; // Keep the button for admin login
    private DatabaseReference databaseReference;

    // Constants for SharedPreferences
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if user is already logged in
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
        if (!isLoggedIn) {
            // User is not logged in, redirect to LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return; // Exit the onCreate method
        }

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Personal Information");

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("UserDetails");

        // Initialize form fields
        etName = findViewById(R.id.et_name);
        etAge = findViewById(R.id.et_age);
        etWeight = findViewById(R.id.et_weight);
        etHeight = findViewById(R.id.et_height);
        etNationality = findViewById(R.id.et_nationality);
        etCaste = findViewById(R.id.et_caste);
        etReligion = findViewById(R.id.et_religion);
        etEducationalDetail = findViewById(R.id.et_educational_detail);
        etIslamicEducation = findViewById(R.id.et_islamic_education);
        etFatherName = findViewById(R.id.et_father_name);
        etFatherOccupation = findViewById(R.id.et_father_occupation);
        etSiblings = findViewById(R.id.et_siblings);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        btnSubmit = findViewById(R.id.btn_submit);
        btnAdminLogin = findViewById(R.id.btn_admin_login);  // Keep this for navigation

        // Submit button logic
        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String age = etAge.getText().toString().trim();
            String weight = etWeight.getText().toString().trim();
            String height = etHeight.getText().toString().trim();
            String nationality = etNationality.getText().toString().trim();
            String caste = etCaste.getText().toString().trim();
            String religion = etReligion.getText().toString().trim();
            String educationalDetail = etEducationalDetail.getText().toString().trim();
            String islamicEducation = etIslamicEducation.getText().toString().trim();
            String fatherName = etFatherName.getText().toString().trim();
            String fatherOccupation = etFatherOccupation.getText().toString().trim();
            String siblings = etSiblings.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(age) || TextUtils.isEmpty(phoneNumber)) {
                Toast.makeText(MainActivity.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a HashMap to store the user details
            HashMap<String, String> userMap = new HashMap<>();
            userMap.put("name", name);
            userMap.put("age", age);
            userMap.put("weight", weight);
            userMap.put("height", height);
            userMap.put("nationality", nationality);
            userMap.put("caste", caste);
            userMap.put("religion", religion);
            userMap.put("educationalDetail", educationalDetail);
            userMap.put("islamicEducation", islamicEducation);
            userMap.put("fatherName", fatherName);
            userMap.put("fatherOccupation", fatherOccupation);
            userMap.put("siblings", siblings);
            userMap.put("phoneNumber", phoneNumber);

            // Save the details to Firebase Realtime Database
            databaseReference.push().setValue(userMap)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Details submitted successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, ThankYouActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to submit details", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Admin Login button logic
        btnAdminLogin.setOnClickListener(v -> {
            // Navigate to AdminLoginActivity when the admin login button is clicked
            Intent intent = new Intent(MainActivity.this, AdminLoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.hello) {
            // Clear the login status from SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(KEY_IS_LOGGED_IN, false); // Set login status to false
            editor.apply(); // Save changes

            // Redirect to LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the MainActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
