package com.example.eastcyclingclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParticipantActivityEvents extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String userUsername, userName, userRole, userPassword, selectedSearch;
    ListView listViewEventsP;
    DatabaseReference databaseEvents;

    List<ClubHelperClassEvent> clubHelperClassEvents;

    Spinner spinner;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_activity_events);



        spinner = findViewById(R.id.searchoptions);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.SearchOptions, R.layout.participant_spinner_search);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        selectedSearch = spinner.getSelectedItem().toString();

        searchView = findViewById(R.id.searchView);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                userUsername = null;
                userName = null;
                userRole = null;
                userPassword = null;
            } else {
                userUsername = extras.getString("username");
                userName = extras.getString("name");
                userRole = extras.getString("role");
                userPassword = extras.getString("password");
            }
        } else {
            userUsername = (String) savedInstanceState.getSerializable("username");
            userName = (String) savedInstanceState.getSerializable("name");
            userRole = (String) savedInstanceState.getSerializable("role");
            userPassword = (String) savedInstanceState.getSerializable("password");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.event);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.event){
                return true;
            }if (id == R.id.profile){
                Intent intent = new Intent(getApplicationContext(), ParticipantActivityProfile.class);
                intent.putExtra("username", userUsername);
                intent.putExtra("name", userName);
                intent.putExtra("role", userRole);
                intent.putExtra("password", userPassword);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }
            return false;
        });


       databaseEvents = FirebaseDatabase.getInstance().getReference().child("users");
       listViewEventsP = (ListView) findViewById(R.id.listViewEventsP);




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
    }


    public void searchList(String text){
        ArrayList <ClubHelperClassEvent> searchList = new ArrayList<>();
        if (selectedSearch.equals("Event Name")){
            databaseEvents.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    searchList.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (postSnapshot.hasChildren()) {
                            for (DataSnapshot postpostSnapshot : postSnapshot.getChildren()) {
                                if (postpostSnapshot.hasChildren()) {
                                    for (DataSnapshot eventSnapshot : postpostSnapshot.getChildren()) {
                                        ClubHelperClassEvent clubHelperClassEvent = eventSnapshot.getValue(ClubHelperClassEvent.class);
                                        if (clubHelperClassEvent.getEventName().toLowerCase().contains(text.toLowerCase())){
                                            searchList.add(clubHelperClassEvent);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }ClubListEvent eventAdapter = new ClubListEvent(ParticipantActivityEvents.this, clubHelperClassEvents);
        eventAdapter.searchDataList(searchList);
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();

        // Depending on the selected item, perform actions accordingly
        if (selectedItem.equals("Event Type")) {
            // Perform actions specific to "Join a Club"
            Toast.makeText(this, "event type selected", Toast.LENGTH_SHORT).show();
        } else // Perform actions specific to "Participants"
            if (selectedItem.equals("Event Name"))
                Toast.makeText(this, "event name selected", Toast.LENGTH_SHORT).show();
    }


    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Nothing selected", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}