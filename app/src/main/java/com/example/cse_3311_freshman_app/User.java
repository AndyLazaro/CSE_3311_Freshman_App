package com.example.cse_3311_freshman_app;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String clubOwner;
    private String followedClubs;

    public User() {
    }

    public User(String clubOwner, String followedClubs) {
        this.clubOwner = clubOwner;
        this.followedClubs = followedClubs;
    }

    public String getClubOwner() {
        return clubOwner;
    }

    public void setClubOwner(String clubOwner) {
        this.clubOwner = clubOwner;
    }

    public String getFollowedClubs() {
        return followedClubs;
    }

    public void setFollowedClubs(String followedClubs) {
        this.followedClubs = followedClubs;
    }
}
