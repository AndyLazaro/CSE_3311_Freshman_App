package com.example.cse_3311_freshman_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ClubProfileActivity extends AppCompatActivity {

    ////////////////////////////////////////////////////////////////////////////////
    // Initialize Firebase authentification & database
    FirebaseAuth auth;
    FirebaseFirestore db;

    // Initialize activity parts

    // Button
    Button followButton;
    ImageButton homeBtn, refreshBtn, postBtn, searchBtn, profileBtn;    // buttons for the tabs below

    // Organization holder
    Organizations club;

    // Firebase Placeholders
    TextView clubName;
    TextView locationInfo;
    TextView emailInfo;
    TextView pnumberInfo;
    TextView addressInfo;
    TextView descInfo;
    ImageView clubAvatar;

    // Query variables for following a club
    Query qUsers;
    Query qUsers1;

    ///////////////////////////////////////////////////////////////////////////////
    // On creation of the page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /////////////////////////////////////////
        // XML File this java file is attached to
        setContentView(R.layout.activity_club_profile);

        /////////////////////////////////
        // connecting vars to tab buttons
        homeBtn = findViewById(R.id.home_button);
        refreshBtn = findViewById(R.id.refresh_button);
        postBtn = findViewById(R.id.post_button);
        searchBtn = findViewById(R.id.search_button);
        profileBtn = findViewById(R.id.profile_button);

        ///////////////////////////////////////
        // Declare the Buttons and Firebase Info Placeholders
        followButton = findViewById(R.id.follow_club_button);
        clubName = findViewById(R.id.club_profile_name);
        locationInfo = findViewById(R.id.location_info);
        emailInfo = findViewById(R.id.email_info);
        pnumberInfo = findViewById(R.id.pnumber_info);
        addressInfo = findViewById(R.id.address_info);
        descInfo = findViewById(R.id.description_info);
        clubAvatar = findViewById(R.id.club_avatar);
        followButton = findViewById(R.id.follow_club_button);

        // retrieve club from recycler_adapter_search
        club = (Organizations) getIntent().getSerializableExtra("CLUB");
        populateClubPage();

        

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
            public void onClick(View view)
            {
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
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
//-----------Search Clubs button-------------------------------------------------
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClubProfileActivity.this, SearchClubActivity.class);
                startActivity(intent);
                finish();
            }
        });
//-------------------------------------------------------------------------------

        // Profile button will go to create club for now while testing
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();//finish current activity
                Intent intent = new Intent(ClubProfileActivity.this, CreateClubActivity.class);
                startActivity(intent);//start current activity again
            }
        });

    }

    // Method to populate the club profile page with the club info
    public void populateClubPage() {
        clubName.setText(club.name);
        locationInfo.setText(club.location);
        emailInfo.setText(club.email);
        pnumberInfo.setText(club.pNumber);
        addressInfo.setText(club.address);
        descInfo.setText(club.desc);
        //Glide.with(getApplicationContext()).load(club.getE_image()).apply(new RequestOptions().override(1000,1000)).into(img);
    }

    // Method to follow the club if the follow/unfollow button is clicked
    /*
    public void RSVP(View view) {
        qUsers.get().addOnCompleteListener( qr -> { // query event from db that matches current event's name
            if (qr.isSuccessful()) {
                String docRef = ""; // temp documentReference val storage
                for (QueryDocumentSnapshot doc : qr.getResult()) { // for loop but only ever one item in list
                    docRef = doc.getId(); // iterate thru results and update docRef val
                }
                String finalDocRef = docRef; // final docRef val
                qUsers1.get().addOnCompleteListener(r -> { // query doc and check if uid is present in array "rsvp"
                    if (r.isSuccessful()) {
                        QuerySnapshot res = r.getResult();
                        if (res.isEmpty()) { // if the result is empty, then add uid
                            db.collection("Users").document(finalDocRef)
                                    //.update("followedClubs", FieldValue.arrayUnion(auth.getCurrentUser().getUid()))
                                    .update("followedClubs", FieldValue.arrayUnion(club.name))
                                    .addOnCompleteListener(u -> {
                                        if (u.isSuccessful()) {
                                            Toast.makeText(ClubProfileActivity.this, "Followed " + club.name, Toast.LENGTH_SHORT).show(); // if all succesful, send toast
                                        }
                                    });
                        } else { // if club found, then remove club
                            db.collection("Users").document(finalDocRef)
                                    .update("followedClubs", FieldValue.arrayRemove(club.name))
                                    .addOnCompleteListener(u -> {
                                        if (u.isSuccessful()) {
                                            Toast.makeText(ClubProfileActivity.this, "Unfollowed " + club.name, Toast.LENGTH_SHORT).show(); // if all succesful, send toast
                                        }
                                    });
                        }
                    }
                });
            }
        });
    }
    */

}
