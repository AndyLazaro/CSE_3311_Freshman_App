package com.example.cse_3311_freshman_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    Button signOutBtn;              // variable for interacting with the sign out button in activity
    FirebaseAuth auth;              // variable giving us authorization in firebase
    ImageButton homeBtn, refreshBtn, postBtn, searchBtn, profileBtn;    // buttons for the tabs below

    //creating variables for the database and recycler view
    RecyclerView recyclerView;      // variable for interacting with the recyclerview in the activity
    ArrayList<Event> events, followedOrgEvents;        // events to be held in the recycler adapter
    ArrayList<String> followedOrgs;
    recycler_adapter adapter, adapterPersonal;       // variable to hold the adapter for connecting recycler view with db data
    FirebaseFirestore db;           // variable to hold the firestore database in firebase
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signOutBtn = findViewById(R.id.button_logout);  // connect variable to button
        auth = FirebaseAuth.getInstance();              // connect variable to firebase

        // connecting vars to tab buttons
        homeBtn = findViewById(R.id.home_button);
        refreshBtn = findViewById(R.id.refresh_button);
        postBtn = findViewById(R.id.post_button);
        searchBtn = findViewById(R.id.search_button);
        profileBtn = findViewById(R.id.profile_button);

        // Connect current data in firebase to program
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.events_list);  // connect variable to recycler view
        recyclerView.setHasFixedSize(true);             // keep the recycler view a fixed size
        recyclerView.setLayoutManager(new LinearLayoutManager(this));   // give the recycler view a linear layout

        db = FirebaseFirestore.getInstance();           // connect variable to firestore database
        events = new ArrayList<Event>();                // initialize events arraylist. Should already contain data from firebase due to connection
        followedOrgEvents = new ArrayList<Event>();
        followedOrgs = new ArrayList<String>();

        adapter = new recycler_adapter(MainActivity.this, events);   // initialize the adapter & make it hold the events arraylist
        adapterPersonal = new recycler_adapter(MainActivity.this, followedOrgEvents);

        //recyclerView.setAdapter(adapter);               // attach the new adapter to the recyclerview to connect it and the events
        //recyclerView.setAdapter(adapterPersonal);

        if (getIntent().hasExtra("CONTENT") && getIntent().getStringExtra("CONTENT").equals("Personal")) {
            recyclerView.setAdapter(adapterPersonal);
            //Toast.makeText(MainActivity.this, "Personal", Toast.LENGTH_SHORT).show();
        } else {
            recyclerView.setAdapter(adapter);
        }

//------------Sign out the user and send back to login page
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                //intent.putExtra("auth", auth);
                startActivity(intent);
            }
        });
//------------Home button-------------------------------------------------
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);
                //recyclerView.setAdapter(adapterPersonal);
                Intent intent = getIntent();
                intent.putExtra("CONTENT", "All");
                startActivity(intent);//start current activity again
            }
        });

//------------Refresh button confirmed to work fine-----------------------
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();//finish current activity
                //recyclerView.setAdapter(adapter);
                Intent intent = getIntent();
                intent.putExtra("CONTENT", "Personal");
                startActivity(intent);//start current activity again
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

    @Override   // this method checks if the current user is logged in on start
    protected void onStart() {
        super.onStart();

        //Check to see if there is a current user logged in, if not, go to login page
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
        {
            Intent intent = new Intent (MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        if (user != null)
        {
            retrieveFollowedOrgs();
            eventChange();
        }
    }

    //private void retrieveFollowedOrgs()

    private void retrieveFollowedOrgs()
    {
        db.collection("Users").document(auth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>(){
                @SuppressLint("NotifyDataSetChanged")
                //@Override
                public void onEvent(@Nullable DocumentSnapshot  value, @Nullable FirebaseFirestoreException error)
                {
                    if(error != null)
                    {
                        Log.e("Firestore Error",error.getMessage());
                        return;
                    }

                    if (!followedOrgs.isEmpty()) {followedOrgs.clear();}
                    followedOrgs = (ArrayList<String>) value.get("followedClubs");
                    filterEventList();
                    adapterPersonal.notifyDataSetChanged();
                }
            });
    }

    private void eventChange()
    {
        db.collection("/Events")
            .addSnapshotListener(new EventListener<QuerySnapshot>()
            {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
                {
                    if(error != null)
                    {
                        Log.e("Firestore Error",error.getMessage());
                        return;
                    }

                    for(DocumentChange dc : value.getDocumentChanges())
                    {
                        if(dc.getType() == DocumentChange.Type.ADDED)
                        {
                            events.add(dc.getDocument().toObject(Event.class));
                        }
                        adapter.notifyDataSetChanged();
                        filterEventList();
                        adapterPersonal.notifyDataSetChanged();
                    }
                }
            });

    }

    private void filterEventList() {
        if (!followedOrgEvents.isEmpty()) {followedOrgEvents.clear();}
        if (followedOrgs == null) {return;}
        for (Event e : events) {
            if (followedOrgs.contains(e.e_org)) {
                followedOrgEvents.add(e);
            }
        }
    }

}


