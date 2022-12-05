package com.example.cse_3311_freshman_app;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event implements Serializable { // Serializable needed to send thru intent
    String e_name;              // String to hold the event name
    String e_org;
    String e_desc;              // String to hold the event description
    String e_location;          // String to hold the event location
    Date time;             // Timestamp to hold the event time
    String e_image;             // String to hold the event image


    public Event() {}           // Default constructor

    // Constructor assigning event aspects
    public Event(String e_name, String e_org, String e_desc, String e_location, Timestamp time, String e_image) {
        this.e_name = e_name;
        this.e_org = e_org;
        this.e_desc = e_desc;
        this.e_location = e_location;
        this.e_image = e_image;
        this.time = time.toDate();
        //this.time = time;
    }

    public String getE_name() {
        return e_name;
    }   // getter for event name

    public void setE_name(String e_name) {
        this.e_name = e_name;
    }   // setter for event name

    public String getE_org() {
        return e_org;
    }   // getter for event org name

    public void setE_org(String e_org) {
        this.e_org = e_org;
    }   // setter for event org name

    public String getE_desc() {
        return e_desc;
    }   // getter for event description

    public void setE_desc(String e_desc) {
        this.e_desc = e_desc;
    }   // setter for event description

    public String getE_location() {
        return e_location;
    }   // getter for event location

    public void setE_location(String e_location) {      // setter for event location
        this.e_location = e_location;
    }

    public Date getTime() {
        return time;
    }   // getter for event time

    public String getStringTime() {
        return new SimpleDateFormat("MM/dd/yyyy  h:mm aa").format(time);
    }   // getter for event time in String format

    public void setTime(Date date) {   // setter for event time in timestamp format
        this.time = time;
        //this.time = new SimpleDateFormat("MM/dd/yyyy  h:mm aa").format(time.toDate());
    }

    public String getE_image() {
        return e_image;
    }   // getter for event image

    public void setE_image(String e_image) {
        this.e_image = e_image;
    }   // setter for event image

}
