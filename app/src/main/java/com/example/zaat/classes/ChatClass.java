package com.example.zaat.classes;

import com.example.zaat.classes.Message_chatting;

import java.util.ArrayList;

public class ChatClass {
    private int ID;
    private int fID;
    private int sID;


    public ChatClass() {
    }

    public ChatClass(int fID, int sID) {
        this.fID = fID;
        this.sID = sID;

    }

    public ChatClass(int fID, int sID, ArrayList<Message_chatting> messageList) {
        this.fID = fID;
        this.sID = sID;
    }

    public int getfID() {
        return fID;
    }

    public void setfID(int fID) {
        this.fID = fID;
    }

    public int getsID() {
        return sID;
    }

    public void setsID(int sID) {
        this.sID = sID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
