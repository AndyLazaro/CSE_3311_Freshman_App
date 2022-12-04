package com.example.cse_3311_freshman_app;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class club_page extends AppCompatActivity {


    ImageButton homeBtn, refreshBtn, postBtn, searchBtn, profileBtn;    // buttons for the tabs below
    // Text views
    TextView email_view, description_view, name_view;


    //creating variables for the database and recycler view
    RecyclerView recyclerView;      // variable for interacting with the recyclerview in the activity
    recycler_adapter adapter;       // variable to hold the adapter for connecting recycler view with db data
    ArrayList<Event> events;        // events to be held in the recycler adapter
    FirebaseFirestore db;           // variable to hold the firestore database in firebase
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    Organizations org;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_page);

        // Get org
        this.org = (Organizations) getIntent().getSerializableExtra("org");         // retreive event from intent

        // connecting vars to tab buttons
        homeBtn = findViewById(R.id.home_button);
        refreshBtn = findViewById(R.id.refresh_button);
        postBtn = findViewById(R.id.post_button);
        searchBtn = findViewById(R.id.search_button);
        profileBtn = findViewById(R.id.profile_button);

        // Connect current data in firebase to program
        auth = FirebaseAuth.getInstance();

        // connect text views
        name_view = findViewById(R.id.club_name_view);
        description_view = findViewById(R.id.club_description);
        email_view = findViewById(R.id.club_email_view);

        // Set text
        name_view.setText(org.name);
        description_view.setText(org.desc);
        email_view.setText(org.email);


        recyclerView = findViewById(R.id.events_list);  // connect variable to recycler view
        recyclerView.setHasFixedSize(true);             // keep the recycler view a fixed size

        recyclerView.setLayoutManager(new LinearLayoutManager(this));   // give the recycler view a linear layout

        db = FirebaseFirestore.getInstance();           // connect variable to firestore database
        events = new ArrayList<Event>();                // initialize events arraylist. Should already contain data from firebase due to connection
        adapter = new recycler_adapter(club_page.this,events);   // initialize the adapter & make it hold the events arraylist

        recyclerView.setAdapter(adapter);               // attach the new adapter to the recyclerview to connect it and the event

        // Request to db for rsvp events
        db.collection("/Events").whereEqualTo("e_org", org.name)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                events.add(dc.getDocument().toObject(Event.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        // Connect submit button
        final Button edit_button = findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), edit_club.class);
                intent.putExtra("org", org); // stores the item's org to send to next acitivity
                startActivity(intent);              // starts up edit club
            }
        });
//------------Home button-------------------------------------------------
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
//------------Refresh button confirmed to work fine-----------------------
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();//finish current activity
                startActivity(getIntent());//start current activity again
            }
        });
//-----------App crashes when post button is pressed; needs work. Fixed---------
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //No closing the MainActivity for posting feeds
                finish();
                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                //Immediately open add post
                startActivity(intent);
            }
        });

        // Profile button will go to create club for now while testing
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();//finish current activity
                Intent intent = new Intent(getApplicationContext(), Profile_Page.class);
                startActivity(intent);//start current activity again
            }
        });
    }
}