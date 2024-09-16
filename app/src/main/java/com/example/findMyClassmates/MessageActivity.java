package com.example.findMyClassmates;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView messagesRecyclerView;
    private EditText editTextMessage;
    private Button buttonSend,buttonBlock;
    protected String receiver_email;
    private FirebaseFirestore db;
    String userEmail;
    private MessagesAdapter messagesAdapter; // You will need to create this adapter
    private ArrayList<Message> messageList = new ArrayList<>(); // Message is a hypothetical model class for messages
    private boolean isBlocked = false;

    public void setDB(FirebaseFirestore ff){
        db = ff;
    }
    private String getMessageInput() {
        return editTextMessage.getText().toString().trim();
    }

    public boolean validateInput(String message) {
        return message != null && !message.trim().isEmpty();
    }

    private void clearMessageInput() {
        editTextMessage.setText("");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        db  = FirebaseFirestore.getInstance();
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        receiver_email = getIntent().getStringExtra("user_email");



        messagesRecyclerView = findViewById(R.id.chatroomRecyclerView);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        buttonBlock = findViewById(R.id.buttonBlock);

        if (receiver_email != null) {
            loadMessages(receiver_email);
            checkIfUserIsBlocked(receiver_email);
            updateBlockButton();
        }


        // Set up RecyclerView
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesAdapter = new MessagesAdapter(messageList, this, userEmail);
        messagesRecyclerView.setAdapter(messagesAdapter);




        // Send button click listener
        buttonSend.setOnClickListener(v -> {
            String messageText = getMessageInput();
            if (!validateInput(messageText)) {
                showToast("Message cannot be empty");

            } else if(isBlocked){
                showToast("User is blocked");
            }
            else{
                sendMessage(userEmail, receiver_email, messageText);
                clearMessageInput();
            }
        });

        buttonBlock.setOnClickListener(v -> toggleBlockUser(receiver_email));

    }


    private void toggleBlockUser(String targetEmail) {
        DocumentReference blockedUsersRef = db.collection("users").document(userEmail).collection("blockedUsers").document(targetEmail);
        blockedUsersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    // If blocked, unblock by deleting the document
                    blockedUsersRef.delete().addOnSuccessListener(aVoid -> {
                        isBlocked = false;
                        updateBlockButton();
                        showToast(isBlocked ? "User blocked" : "User unblocked");
                    });
                } else {
                    // Block for the first time if document does not exist
                    Map<String, Object> blockedData = new HashMap<>();
                    blockedData.put("blocked", true);
                    blockedUsersRef.set(blockedData).addOnSuccessListener(aVoid -> {
                        isBlocked = true;
                        updateBlockButton();
                        showToast(isBlocked ? "User blocked" : "User unblocked");
                    });
                }
            } else {
                showToast("Error in toggling block status");
            }
        });
    }


    private void updateBlockButton() {
        buttonBlock.setText(isBlocked ? "Unblock" : "Block");
    }

    private void checkIfUserIsBlocked(String receiverEmail) {
        DocumentReference blockedUsersRef = db.collection("users").document(userEmail).collection("blockedUsers").document(receiverEmail);
        blockedUsersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                isBlocked = document != null && document.exists() && document.getBoolean("blocked");
                runOnUiThread(() -> updateBlockButton()); // Update UI on the main thread
            } else {
                showToast("Error checking block status");
            }
        });
    }


    private void loadMessages(String receiverEmail) {
        DocumentReference chatRoomRef = db.collection("users")
                .document(userEmail)
                .collection("chatrooms")
                .document(receiverEmail);

        CollectionReference messagesRef = chatRoomRef.collection("messages");

        // Check if the sender is blocked before displaying messages
        DocumentReference blockedUserRef = db.collection("users")
                .document(userEmail)
                .collection("blockedUsers")
                .document(receiverEmail);

        blockedUserRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot blockedDoc = task.getResult();
                boolean isSenderBlocked = blockedDoc != null && blockedDoc.exists();

                messagesRef.orderBy("timestamp", Query.Direction.ASCENDING)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot snapshots,
                                                @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    return;
                                }

                                messageList.clear();
                                for (DocumentSnapshot doc : snapshots) {
                                    Message message = doc.toObject(Message.class);
                                    // Add message only if sender is not blocked
                                    if (!isSenderBlocked || !message.getSenderEmail().equals(receiverEmail)) {
                                        messageList.add(message);
                                    }
                                }
                                messagesAdapter.notifyDataSetChanged();
                                messagesRecyclerView.scrollToPosition(messageList.size() - 1);
                            }
                        });
            }
        });
    }


    private void sendMessage(String senderEmail, String receiverEmail, String msg) {
        String messageText = editTextMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {
            // Create a map for the message data
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("senderEmail", senderEmail);
            messageData.put("receiverEmail", receiverEmail);
            messageData.put("content", msg);
            messageData.put("timestamp", FieldValue.serverTimestamp()); // Use server timestamp

            // Paths to the document references where the chatrooms for the sender and receiver are stored
            DocumentReference senderChatRoomRef = db.collection("users")
                    .document(senderEmail)
                    .collection("chatrooms")
                    .document(receiverEmail);
            DocumentReference receiverChatRoomRef = db.collection("users")
                    .document(receiverEmail)
                    .collection("chatrooms")
                    .document(senderEmail);

            // Reference to the messages subcollection for the sender and receiver
            CollectionReference senderMessagesRef = senderChatRoomRef.collection("messages");
            CollectionReference receiverMessagesRef = receiverChatRoomRef.collection("messages");

            // Check if the receiver has blocked the sender
            DocumentReference receiverBlockedUserRef = db.collection("users").document(receiverEmail).collection("blockedUsers").document(senderEmail);
            receiverBlockedUserRef.get().addOnCompleteListener(task -> {
                boolean receiverHasBlockedSender = task.isSuccessful() && task.getResult() != null && task.getResult().exists();

                // Transaction to send a message: add to both sender and receiver messages collection
                db.runTransaction(transaction -> {
                    // Add message to sender's Firestore messages
                    transaction.set(senderMessagesRef.document(), messageData);

                    // Add message to receiver's Firestore messages
                    transaction.set(receiverMessagesRef.document(), messageData);

                    // Update the last message and timestamp for sender's chatroom
                    Map<String, Object> senderChatRoomData = new HashMap<>();
                    senderChatRoomData.put("RecipientEmail", receiverEmail);
                    senderChatRoomData.put("lastMessage", msg);
                    senderChatRoomData.put("lastTimestamp", FieldValue.serverTimestamp());
                    transaction.set(senderChatRoomRef, senderChatRoomData, SetOptions.merge());

                    if (!receiverHasBlockedSender) {
                        // Update the last message and timestamp for receiver's chatroom only if not blocked
                        Map<String, Object> receiverChatRoomData = new HashMap<>();
                        receiverChatRoomData.put("RecipientEmail", senderEmail);
                        receiverChatRoomData.put("lastMessage", msg);
                        receiverChatRoomData.put("lastTimestamp", FieldValue.serverTimestamp());
                        transaction.set(receiverChatRoomRef, receiverChatRoomData, SetOptions.merge());
                    }

                    // Return null as this function does not return any result
                    return null;
                }).addOnSuccessListener(aVoid -> {
                    // Clear the input field after sending
                    editTextMessage.setText("");
                }).addOnFailureListener(e -> {
                    // Handle the error for transaction failure
                });
            });
        }
    }




}
