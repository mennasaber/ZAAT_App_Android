package com.example.zaat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Message {

    String mID;
    String message;
    String uID;
    String mDate;

    public Message() {
    }

    public Message(String message, String uID) {
        this.message = message;
        this.uID = uID;
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        this.mDate = df.format(c);
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getmID() {
        return mID;
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
