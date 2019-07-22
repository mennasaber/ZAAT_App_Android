package com.example.zaat;

import java.util.ArrayList;

public class ChatClass {

    private String fID;
    private String sId;
    String mID;


    public ChatClass() {
    }

    public ChatClass(String fID, String sId) {
        this.fID = fID;
        this.sId = sId;

    }

    public ChatClass(String fID, String sId, ArrayList<Message_chatting> messageList) {
        this.fID = fID;
        this.sId = sId;
    }

    public String getfID() {
        return fID;
    }

    public void setfID(String fID) {
        this.fID = fID;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }
}
