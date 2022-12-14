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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class SearchClubActivity extends AppCompatActivity {

    Button signOutBtn;              // variable for interacting with the sign out button in activity
    FirebaseAuth auth;              // variable giving us authorization in firebase
    ImageButton homeBtn, refreshBtn, postBtn, searchBtn, profileBtn;    // buttons for the tabs below

    //creating variables for the database and recycler view
    RecyclerView recyclerView;      // variable for interacting with the recyclerview in the activity
    ArrayList<Organizations> clubs;        // clubs to be held in the recycler adapter
    recycler_adapter_search adapter_search;       // variable to hold the adapter for connecting recycler view with db data
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
        clubs = new ArrayList<Organizations>();                // initialize clubs arraylist. Should already contain data from firebase due to connection
        adapter_search = new recycler_adapter_search(SearchClubActivity.this,clubs);   // initialize the adapter & make it hold the events arraylist

        recyclerView.setAdapter(adapter_search);               // attach the new adapter to the recyclerview to connect it and the events

        clubchange();

//------------Home button-------------------------------------------------
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

//------------Refresh button confirmed to work fine-----------------------
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
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

    // Queries the events in the database
    private void clubchange()
    {
        db.collection("/Organizations")        // query from this location
                .addSnapshotListener(new EventListener<QuerySnapshot>()     // Hold the data from firebase in this snapshot
                {
                    @SuppressLint("NotifyDataSetChanged")   // ignore this error if made
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
                    {
                        if(error != null)   // if there's an error and the query fails
                        {
                            Log.e("Firestore Error",error.getMessage());    // log that there was a firestore error
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges())     // for all entries in this location
                        {
                            if(dc.getType() == DocumentChange.Type.ADDED)   // if a query is found
                            {
                                clubs.add(dc.getDocument().toObject(Organizations.class));     // add this query to the events arraylist
                            }
                            adapter_search.notifyDataSetChanged();     // force layout managers to rebind and relayout
                        }


                    }
                });

    }

}


