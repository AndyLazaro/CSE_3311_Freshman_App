package com.example.cse_3311_freshman_app;

import java.io.Serializable;

public class Organizations implements Serializable {
    String name;
    String desc;
    String address;
    String email;
    String location;
    String pNumber;
    String category;
    String uid;
    String rsvp [];

    public Organizations() {
    }

    public Organizations(String name, String desc, String cAddress, String cEmail, String location, String cPhoneNumber, String category, String uid) {
        this.name = name;
        this.desc = desc;
        this.address = cAddress;
        this.email = cEmail;
        this.location = location;
        this.pNumber = cPhoneNumber;
        this.category = category;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String location) {
        this.desc = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String cAddress) {
        this.address = cAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String cEmail) {
        this.email = cEmail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPNumber() {
        return pNumber;
    }

    public void setPNumber(String cPhoneNumber) {
        this.pNumber = cPhoneNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {this.category = category;}

    public void setUid(String uid) {this.uid = uid;}

    public String getUid() {return uid;}

    public void setRSVP(String rsvp[]){this.rsvp = rsvp;}

}
