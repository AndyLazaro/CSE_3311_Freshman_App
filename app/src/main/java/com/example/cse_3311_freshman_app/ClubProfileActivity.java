package com.example.cse_3311_freshman_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ClubProfileActivity extends AppCompatActivity {

    ////////////////////////////////////////////////////////////////////////////////
    // Initialize Firebase authentification & database
    FirebaseAuth auth;
    FirebaseFirestore db;

    // Initialize activity parts

    // Button
    Button followButton;
    ImageButton homeBtn, refreshBtn, postBtn, searchBtn, profileBtn;    // buttons for the tabs below

    // Firebase Placeholders
    TextView clubName;
    TextView locationInfo;
    TextView emailInfo;
    TextView pnumberInfo;
    TextView addressInfo;
    TextView descInfo;
    ImageView clubAvatar;

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

//TODO: Search Clubs Button
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
}
