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


public class MainActivity extends AppCompatActivity {

    Button signOutBtn;
    ImageButton homeBtn, refreshBtn, postBtn, searchBtn, profileBtn;
    FirebaseAuth auth;
    //creating variables for the database and recycler view

    RecyclerView recyclerView;
    ArrayList<Event> events;
    recycler_adapter adapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        signOutBtn = findViewById(R.id.button_logout);
        homeBtn = findViewById(R.id.home_button);
        refreshBtn = findViewById(R.id.refresh_button);
        postBtn = findViewById(R.id.post_button);
        searchBtn = findViewById(R.id.search_button);
        profileBtn = findViewById(R.id.profile_button);
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.events_list);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        events = new ArrayList<Event>();
        adapter = new recycler_adapter(MainActivity.this,events);

        recyclerView.setAdapter(adapter);

        eventchange();


        //Sign out the user and send back to login page
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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
//-----------App crashes when post button is pressed; needs work---------
        /*postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //No closing the MainActivity for posting feeds
                Intent intent = new Intent(MainActivity.this, PostActivity.class);
                //Immediately open add post
                startActivity(intent);
            }
        });*/

    }

    @Override
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
    }


    private void eventchange()
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
                        }


                    }
                });

    }


}


