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
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ParticipantActivityEditProfile extends AppCompatActivity {

    EditText editName, editEmail, editPassword;
    Button saveButton;
    String nameUser, usernameUser, passwordUser, roleUser;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_activity_edit_profile);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                usernameUser = null;
                nameUser = null;
                passwordUser = null;
            } else {
                usernameUser = extras.getString("username");
                nameUser = extras.getString("name");
                passwordUser = extras.getString("password");
            }
        } else {
            usernameUser = (String) savedInstanceState.getSerializable("username");
            nameUser = (String) savedInstanceState.getSerializable("name");
            passwordUser = (String) savedInstanceState.getSerializable("password");
        }

        reference = FirebaseDatabase.getInstance().getReference("users");

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editUsername);

        showUserData();

        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!editName.getText().toString().isEmpty() && !editEmail.getText().toString().isEmpty() && !editPassword.getText().toString().isEmpty()) {
                    reference.child(usernameUser).child("name").setValue(editName.getText().toString());
                    reference.child(usernameUser).child("email").setValue(editEmail.getText().toString());
                    reference.child(usernameUser).child("password").setValue(editPassword.getText().toString());
                    Toast.makeText(ParticipantActivityEditProfile.this, "Saved", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ParticipantActivityEditProfile.this, ParticipantActivityProfile.class);

                    intent.putExtra("name", editName.getText().toString());
                    intent.putExtra("username", usernameUser);
                    intent.putExtra("email", editEmail.getText().toString());
                    intent.putExtra("password", editPassword.getText().toString());
                    intent.putExtra("role", roleUser);

                    startActivity(intent);
                } else if (editName.getText().toString().isEmpty()) {
                    editName.setError("No Name Specified");
                    editName.requestFocus();
                }
                else if (editEmail.getText().toString().isEmpty()) {
                    editEmail.setError("No Email Specified");
                    editEmail.requestFocus();
                }
                else if (editPassword.getText().toString().isEmpty()) {
                    editPassword.setError("No Password Specified");
                    editPassword.requestFocus();
                }
                else {
                    Toast.makeText(ParticipantActivityEditProfile.this, "Ensure All Fields Are Entered", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    private boolean isNameChanged() {
//        if (!nameUser.equals(editName.getText().toString())) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    private boolean isEmailChanged() {
//        if (!emailUser.equals(editEmail.getText().toString())) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//
//    private boolean isPasswordChanged() {
//        if (!passwordUser.equals(editPassword.getText().toString())) {
//
//            return true;
//        } else {
//            return false;
//        }
//    }

    public void showUserData() {

        DatabaseReference specificUserReference = database.getInstance().getReference().child("users").child(usernameUser);

        final String[] nameFromDatabase = new String[1];
        final String[] emailFromDatabase = new String[1];
        final String[] passwordFromDatabase = new String[1];
        final String[] roleFromDatabase = new String[1];

        specificUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    nameFromDatabase[0] = dataSnapshot.child("name").getValue(String.class);
                    emailFromDatabase[0] = dataSnapshot.child("email").getValue(String.class);
                    passwordFromDatabase[0] = dataSnapshot.child("password").getValue(String.class);
                    roleFromDatabase[0] = dataSnapshot.child("role").getValue(String.class);

                    editName.setText(nameFromDatabase[0]);
                    editEmail.setText(emailFromDatabase[0]);
                    editPassword.setText(passwordFromDatabase[0]);
                    roleUser = roleFromDatabase[0];

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