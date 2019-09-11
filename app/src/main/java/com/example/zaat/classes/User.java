package com.example.zaat.classes;

public class User {
    private String uName;
    private String uPassword;
    private String uGender;
    private String uStatue;
    private Boolean uInChat;
    private int uID;

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

    public void setuID(int uID) {
        this.uID = uID;
    }

    public User(String uName, String uPassword, String uGender, String uStatue, Boolean uInChat) {
        this.uName = uName;
        this.uPassword = uPassword;
        this.uGender = uGender;
        this.uStatue = uStatue;
        this.uInChat = uInChat;
    }

    public User(String uName, String uPassword, int uID, String uGender, String uStatue, Boolean uInChat) {
        this.uName = uName;
        this.uPassword = uPassword;
        this.uGender = uGender;
        this.uID = uID;
        this.uStatue = uStatue;
        this.uInChat = uInChat;
    }

    public String getUstatue() {
        return uStatue;
    }

    public void setUstatue(String ustatue) {
        this.uStatue = ustatue;
    }

    public Boolean getuInChat() {
        return uInChat;
    }

    public void setuInChat(Boolean uInChat) {
        this.uInChat = uInChat;
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

    public int getuID() {
        return uID;
    }

}
