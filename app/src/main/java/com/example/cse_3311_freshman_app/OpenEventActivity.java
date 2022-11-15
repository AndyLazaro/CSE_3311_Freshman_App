package com.example.cse_3311_freshman_app;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class OpenEventActivity extends AppCompatActivity {

    Event event;
    TextView eName, eOrg, eDesc, eLoc, eTime;
    ImageView img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        this.event = (Event) getIntent().getSerializableExtra("EVENT");
        this.eName = findViewById(R.id.eventName);
        this.eOrg = findViewById(R.id.eventOrgName);
        this.eDesc = findViewById(R.id.eventDesc);
        this.eLoc = findViewById(R.id.eventLocation);
        this.eTime = findViewById(R.id.eventTime);
        this.img = findViewById(R.id.eventImage);
        populateEventPage();
    }

    public void populateEventPage() {
        eName.setText(event.e_name);
        //eOrg.setText(event.e_name);
        eDesc.setText(event.e_desc);
        eLoc.setText(event.e_location);
        eTime.setText(event.time);
        //img.setBackgroundResource(event.e_image);
        Glide.with(getApplicationContext()).load(event.getE_image()).apply(new RequestOptions().override(1000,1000)).into(img);
    }
}
