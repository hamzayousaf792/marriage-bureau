package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminPanelActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextView tvUserDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        // Initialize UI components
        tvUserDetails = findViewById(R.id.tv_user_details);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("UserDetails");

        // Fetch and display user details
        fetchUserDetails();
    }

    private void fetchUserDetails() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StringBuilder userDetails = new StringBuilder();

                // Loop through each user data snapshot
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract user details
                    String name = snapshot.child("name").getValue(String.class);
                    String age = snapshot.child("age").getValue(String.class);
                    String weight = snapshot.child("weight").getValue(String.class);
                    String height = snapshot.child("height").getValue(String.class);
                    String nationality = snapshot.child("nationality").getValue(String.class);
                    String caste = snapshot.child("caste").getValue(String.class);
                    String religion = snapshot.child("religion").getValue(String.class);
                    String educationalDetail = snapshot.child("educationalDetail").getValue(String.class);
                    String islamicEducation = snapshot.child("islamicEducation").getValue(String.class);
                    String fatherName = snapshot.child("fatherName").getValue(String.class);
                    String fatherOccupation = snapshot.child("fatherOccupation").getValue(String.class);
                    String siblings = snapshot.child("siblings").getValue(String.class);
                    String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);

                    // Append user details to the StringBuilder
                    userDetails.append("Name: ").append(name).append("\n")
                            .append("Age: ").append(age).append("\n")
                            .append("Weight: ").append(weight).append("\n")
                            .append("Height: ").append(height).append("\n")
                            .append("Nationality: ").append(nationality).append("\n")
                            .append("Caste: ").append(caste).append("\n")
                            .append("Religion: ").append(religion).append("\n")
                            .append("Educational Detail: ").append(educationalDetail).append("\n")
                            .append("Islamic Education: ").append(islamicEducation).append("\n")
                            .append("Father's Name: ").append(fatherName).append("\n")
                            .append("Father's Occupation: ").append(fatherOccupation).append("\n")
                            .append("Siblings: ").append(siblings).append("\n")
                            .append("Phone Number: ").append(phoneNumber).append("\n\n");
                }

                // Set the fetched data to the TextView
                tvUserDetails.setText(userDetails.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read data", databaseError.toException());
            }
        });
    }
}
