package com.example.eastcyclingclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ParticipantActivityProfile extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    TextView profileName, profileRole, profileUsername, welcomeText;
    Button editProfile, logout;
    String userUsername, userName, userRole, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_activity_profile);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                userUsername = null;
                userName = null;
                userPassword = null;
                userRole = null;
            } else {
                userUsername = extras.getString("username");
                userName = extras.getString("name");
                userPassword = extras.getString("password");
                userRole = extras.getString("role");
            }
        } else {
            userUsername = (String) savedInstanceState.getSerializable("username");
            userName = (String) savedInstanceState.getSerializable("name");
            userRole = (String) savedInstanceState.getSerializable("role");
            userPassword = (String) savedInstanceState.getSerializable("password");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.event) {
                Intent intent = new Intent(getApplicationContext(), ParticipantActivityEvents.class);
                intent.putExtra("username", userUsername);
                intent.putExtra("name", userName);
                intent.putExtra("role", userRole);
                intent.putExtra("password", userPassword);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }
            if (id == R.id.profile) {
                return true;
            }
            return false;
        });

        // Initializing database
        database = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");

        profileName = findViewById(R.id.profileName);
        profileRole = findViewById(R.id.profileRole);
        profileUsername = findViewById(R.id.profileUsername);

        welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome Back\n" + userName);
        editProfile = findViewById(R.id.editButton);
        logout = findViewById(R.id.logoutButton);

        showUserData();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ParticipantActivityEditProfile.class);
                intent.putExtra("username", userUsername);
                intent.putExtra("name", userName);
                intent.putExtra("password", userPassword);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ParticipantActivityProfile.this, GeneralActivityLogin.class));
            }
        });

    }

    public void showUserData() {

        DatabaseReference specificUserReference = database.getInstance().getReference().child("users").child(userUsername);

        final String[] nameFromDatabase = new String[1];
        final String[] roleFromDatabase = new String[1];
        final String[] usernameFromDatabase = new String[1];

        specificUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    nameFromDatabase[0] = dataSnapshot.child("name").getValue(String.class);
                    roleFromDatabase[0] = dataSnapshot.child("role").getValue(String.class);
                    usernameFromDatabase[0] = dataSnapshot.child("username").getValue(String.class);

                    profileName.setText("Name: " + nameFromDatabase[0]);
                    profileRole.setText("Role: " + roleFromDatabase[0]);
                    profileUsername.setText("Username: " + usernameFromDatabase[0]);

                    Log.d("TAG", "Profile values retrieved");
                } else {
                    Log.d("TAG", "Profile values do not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "Error retrieving profile values", databaseError.toException());
            }
        });
    }
}