package com.example.cse_3311_freshman_app;

public class Organizations {
    String name;
    String location;
    String cAddress;
    String cEmail;
    String cPhoneNumber;

    public Organizations() {
    }

    public Organizations(String name, String location, String cAddress, String cEmail, String cPhoneNumber) {
        this.name = name;
        this.location = location;
        this.cAddress = cAddress;
        this.cEmail = cEmail;
        this.cPhoneNumber = cPhoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCAddress() {
        return cAddress;
    }

    public void setCAddress(String cAddress) {
        this.cAddress = cAddress;
    }

    public String getCEmail() {
        return cEmail;
    }

    public void setCEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    public String getCPhoneNumber() {
        return cPhoneNumber;
    }

    public void setCPhoneNumber(String cPhoneNumber) {
        this.cPhoneNumber = cPhoneNumber;
    }
}
