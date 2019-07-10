package com.example.zaat;

public class Message {

    String message;
    String uID;
    String mDate;

    public Message() {
    }

    public Message(String message, String uID, String mDate) {
        this.message = message;
        this.uID = uID;
        this.mDate = mDate;
    }

    public String getMessage() {
        return message;
    }

    public String getuID() {
        return uID;
    }

    public String getmDate() {
        return mDate;
    }
}
