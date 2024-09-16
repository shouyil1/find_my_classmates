package com.example.findMyClassmates;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ClassmatesProfile extends AppCompatActivity {

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView roleTextView;
    private FirebaseFirestore db;
    private Button sendMessageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classmates_profile);

        nameTextView = findViewById(R.id.userName);
        emailTextView = findViewById(R.id.email);
        roleTextView = findViewById(R.id.role);
        sendMessageButton = findViewById(R.id.sendMessageButton); // Find the button by ID

        db = FirebaseFirestore.getInstance();
        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        loadClassmateProfile(userEmail);

        sendMessageButton.setOnClickListener(v -> navigateToChatRoomActivity(userEmail)); // Set the click listener
    }

    private void navigateToChatRoomActivity(String email) {
        Intent chatIntent = new Intent(ClassmatesProfile.this, MessageActivity.class);
        chatIntent.putExtra("user_email", email);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Reference to the chatroom between the currentUser and receiver.
        DocumentReference chatRoomRef = db.collection("users")
                .document(currentUserEmail)
                .collection("chatrooms")
                .document(email);

        // Create a new chatroom or get the existing one.
        chatRoomRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (!document.exists()) {
                    // Create a new chatroom if it doesn't exist
                    Map<String, Object> newChatRoom = new HashMap<>();
                    newChatRoom.put("receiver_email", email);
                    // Add other necessary details to the chatroom if needed

                    chatRoomRef.set(newChatRoom).addOnSuccessListener(aVoid -> {

                        startActivity(chatIntent);
                    }).addOnFailureListener(e -> {

                    });
                } else {
                    startActivity(chatIntent);
                }
            } else {
            }
        });
    }


    private void loadClassmateProfile(String userEmail) {
        db.collection("users").document(userEmail).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                nameTextView.setText(documentSnapshot.getString("name"));
                emailTextView.setText(userEmail);
                roleTextView.setText(documentSnapshot.getString("role"));
            } else {
            }
        }).addOnFailureListener(e -> {
        });
    }
}
