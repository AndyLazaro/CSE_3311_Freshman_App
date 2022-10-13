package com.example.cse_3311_freshman_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button signOutBtn;
    ImageButton homeBtn, refreshBtn, postBtn, searchBtn, profileBtn;
    FirebaseAuth auth;

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

        //Sign out the user and send back to login page
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //----Refresh button will refresh main feed ------------------
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();//finish current activity
                startActivity(getIntent());//start current activity again
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //No closing the MainActivity for posting feeds
                Intent intent = new Intent(MainActivity.this, PostActivity.class);
                //Immediately open add post
                startActivity(intent);
            }
        });

        /*----Search button is commented for now--------------------
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });*/

        /*------Profile button commented for now--------------------
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
            }
        });*/

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Check to see if there is a current user logged in, if not, go to login page
        //commented for test purposes
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
        {
            Intent intent = new Intent (MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

}