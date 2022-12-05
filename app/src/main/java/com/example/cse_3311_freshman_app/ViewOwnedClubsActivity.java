package com.example.cse_3311_freshman_app;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse_3311_freshman_app.databinding.ViewOwnedClubsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewOwnedClubsActivity extends AppCompatActivity {

    FirebaseAuth auth;              // variable giving us authorization in firebase

    //creating variables for the database and recycler view
    RecyclerView recyclerView;      // variable for interacting with the recyclerview in the activity
    ArrayList<Organizations> clubs;        // Organizations to be held in the recycler adapter
    club_list_adapter adapter;       // variable to hold the adapter for connecting recycler view with db data
    FirebaseFirestore db;           // variable to hold the firestore database in firebase
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_owned_clubs);



        // Get list of owned clubs
        // initialize recycler
        // set adapter

        auth = FirebaseAuth.getInstance();              // connect variable to firebase


        recyclerView = findViewById(R.id.org_list);  // connect variable to recycler view
        recyclerView.setHasFixedSize(true);             // keep the recycler view a fixed size

        recyclerView.setLayoutManager(new LinearLayoutManager(this));   // give the recycler view a linear layout

        db = FirebaseFirestore.getInstance();           // connect variable to firestore database
        clubs = new ArrayList<Organizations>();                // initialize events arraylist. Should already contain data from firebase due to connection
        adapter = new club_list_adapter(this, clubs);   // initialize the adapter & make it hold the events arraylist

        recyclerView.setAdapter(adapter);               // attach the new adapter to the recyclerview to connect it and the events

        db.collection("/Organizations").whereEqualTo("uid", auth.getUid())
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
                                clubs.add(dc.getDocument().toObject(Organizations.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}