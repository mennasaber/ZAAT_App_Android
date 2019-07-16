package com.example.zaat;

import java.util.ArrayList;
import java.util.Map;

public class ChatClass {

    private String fID;
    private String sId;
    private ArrayList<Message_chatting> messageList;


    public ChatClass(String fID, String sId) {
        this.fID = fID;
        this.sId = sId;
    }

    public ChatClass(String fID, String sId, ArrayList<Message_chatting> messageList) {
        this.fID = fID;
        this.sId = sId;
        this.messageList = messageList;
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

    public ArrayList<Message_chatting> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<Message_chatting> messageList) {
        this.messageList = messageList;
    }
}
