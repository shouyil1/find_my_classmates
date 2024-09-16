package com.example.findMyClassmates;

public class Chatroom {
    private String recipientEmail;
    private String recipientName;
    private String lastMessage;
    private String lastTimestamp;

    // Constructor
    public Chatroom(String recipientEmail, String recipientName, String lastMessage, String lastTimestamp) {
        this.recipientName = recipientName;
        this.recipientEmail = recipientEmail;
        this.lastMessage = lastMessage;
        this.lastTimestamp = lastTimestamp;
    }

    public String getChatroomName() {
        return recipientName;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getLastTimestamp() {
        return lastTimestamp;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setLastTimestamp(String lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }
}

