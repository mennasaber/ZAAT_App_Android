package com.example.zaat;

import java.util.ArrayList;

public class ChatClass {

    private String fID;
    private String sId;
    private ArrayList<String> messages;

    public ChatClass(String fID, String sId) {
        this.fID = fID;
        this.sId = sId;
        this.messages = new ArrayList<>();
    }

    public ChatClass(String fID, String sId, ArrayList<String> messages) {
        this.fID = fID;
        this.sId = sId;
        this.messages = messages;
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

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }
}
