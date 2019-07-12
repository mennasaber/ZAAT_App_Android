package com.example.zaat;

public class User {
    private String uName;
    private String uPassword;
    private String uGender;
    String uID;

    public User() {
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public void setuGender(String uGender) {
        this.uGender = uGender;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public User(String uName, String uPassword, String uGender) {
        this.uName = uName;
        this.uPassword = uPassword;
        this.uGender = uGender;
    }

    public User(String uName, String uPassword, String uID, String uGender) {
        this.uName = uName;
        this.uPassword = uPassword;
        this.uGender = uGender;
        this.uID = uID;
    }


    public String getuName() {
        return uName;
    }

    public String getuPassword() {
        return uPassword;
    }

    public String getuGender() {
        return uGender;
    }
}
