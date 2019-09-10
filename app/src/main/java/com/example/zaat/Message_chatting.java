package com.example.zaat;

public class Message_chatting {
    private int uID;
    private String Message;

    public Message_chatting() {
    }

    public Message_chatting(int uID, String message) {
        this.uID = uID;
        Message = message;
    }

    public int getuID() {
        return uID;
    }

    public void setuID(int uID) {
        this.uID = uID;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
