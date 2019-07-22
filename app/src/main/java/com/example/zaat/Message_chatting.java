package com.example.zaat;

public class Message_chatting {
    private String uID;
    private String Message;

    public Message_chatting() {
    }

    public Message_chatting(String uID, String message) {
        this.uID = uID;
        Message = message;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
