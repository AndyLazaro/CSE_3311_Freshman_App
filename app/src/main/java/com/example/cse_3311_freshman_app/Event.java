package com.example.cse_3311_freshman_app;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
    String e_name;
    String e_desc;
    String e_location;
    String time;
    String e_image;


    public Event() {}

    public Event(String e_name, String e_desc, String e_location, Timestamp time, String e_image) {
        this.e_name = e_name;
        this.e_desc = e_desc;
        this.e_location = e_location;
        this.e_image = e_image;
        //this.e_time = e_time;
        this.time = new SimpleDateFormat("MM/dd/yyyy  h:mm aa").format(time.toDate());
        //this.time = time.toString();
    }


    public String getE_name() {
        return e_name;
    }

    public void setE_name(String e_name) {
        this.e_name = e_name;
    }

    public String getE_desc() {
        return e_desc;
    }

    public void setE_desc(String e_desc) {
        this.e_desc = e_desc;
    }

    public String getE_location() {
        return e_location;
    }

    public void setE_location(String e_location) {
        this.e_location = e_location;
    }

    public String getTime() {
        return time;
    }

    @Exclude
    public void setTime(String time) {
        this.time = time;
    }

    public void setTime(Timestamp time) {
        this.time = new SimpleDateFormat("MM/dd/yyyy  h:mm aa").format(time.toDate());
    }

    public String getE_image() {
        return e_image;
    }

    public void setE_image(String e_image) {
        this.e_image = e_image;
    }

}
