package com.example.cse_3311_freshman_app;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String clubOwner;
    private ArrayList<String>  followedClubs;
    private String uid;

    public User() {
    }

    public User(String clubOwner, ArrayList<String> followedClubs, String uid) {
        this.clubOwner = clubOwner;
        this.followedClubs = followedClubs;
        this.uid = uid;
    }

    public String getClubOwner() {
        return clubOwner;
    }

    public void setClubOwner(String clubOwner) {
        this.clubOwner = clubOwner;
    }

    public ArrayList<String>  getFollowedClubs() {
        return followedClubs;
    }

    public void setFollowedClubs(ArrayList<String>  followedClubs) {
        this.followedClubs = followedClubs;
    }

    public void setUid(String uid){this.uid = uid;}

    public String getUid(){return uid;}
}
