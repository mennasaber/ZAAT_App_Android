package com.example.zaat.classes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Message {

    int mID;
    String message;
    int uID;
    String mDate;

    public Message() {
    }

    public Message(String message, int uID) {
        this.message = message;
        this.uID = uID;
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        this.mDate = df.format(c);
    }

    public Message(int mID, String message, int uID, String mDate) {
        this.mID = mID;
        this.message = message;
        this.uID = uID;
        this.mDate = mDate;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public int getmID() {
        return mID;
    }

    public String getMessage() {
        return message;
    }

    public int getuID() {
        return uID;
    }

    public String getmDate() {
        return mDate;
    }
}
