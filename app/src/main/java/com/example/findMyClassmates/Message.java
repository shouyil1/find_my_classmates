package com.example.findMyClassmates;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

// Message Class
public class Message {

    private String messageId; // Firestore document IDs are strings
    private String senderEmail;
    private String receiverEmail;
    private String content;

    @ServerTimestamp
    private Date timestamp; // Firestore will set this field with the server timestamp

    // Empty constructor needed for Firestore data conversion
    public Message() {}

    // Constructor without timestamp since Firestore will manage this
    public Message(String senderEmail, String receiverEmail, String content) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.content = content;
        // Don't set the timestamp here; Firestore will do it automatically
    }

    // Getters and setters
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    // Nullable Date return type for the server timestamp to work properly
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String email) {
        this.senderEmail = email;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String email) {
        this.receiverEmail = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSentByCurrentUser(String currentUserEmail) {
        return senderEmail != null && senderEmail.equals(currentUserEmail);
    }
}
