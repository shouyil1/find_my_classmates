package com.example.findMyClassmates;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InboxActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView chatroomRecyclerView;
    private List<Chatroom> chatroomList;
    private ChatroomAdapter chatroomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            // Finish the current activity, which will take you back to the previous activity in the stack.
            finish();
        });

        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(view -> {
            // Clear the existing chatroom list
            chatroomList.clear();
            // Load the chatrooms again
            loadChatrooms();
        });

        chatroomRecyclerView = findViewById(R.id.chatroomRecyclerView);
        chatroomRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatroomList = new ArrayList<>();
        chatroomAdapter = new ChatroomAdapter(chatroomList, this);
        chatroomRecyclerView.setAdapter(chatroomAdapter);

        db = FirebaseFirestore.getInstance();

        loadChatrooms();
    }


    private void getUserNameFromEmail(String email, final OnNameFetchedListener listener) {
        db.collection("users").document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        listener.onNameFetched(name);
                    } else {
                        listener.onError("User not found");
                    }
                })
                .addOnFailureListener(e -> {
                    listener.onError(e.getMessage());
                });
    }

    public interface OnNameFetchedListener {
        void onNameFetched(String name);
        void onError(String errorMessage);
    }


    private void loadChatrooms() {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        db.collection("users").document(userEmail).collection("chatrooms")
                .orderBy("lastTimestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String chatroomId = document.getId();
                            String recipientEmail = document.getString("RecipientEmail");
                            String lastMessage = document.getString("lastMessage");
                            Timestamp timestamp = document.getTimestamp("lastTimestamp");
                            Date date = timestamp.toDate();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String formattedDate = sdf.format(date);

                            System.out.println(recipientEmail);
                            getUserNameFromEmail(recipientEmail, new OnNameFetchedListener() {
                                @Override
                                public void onNameFetched(String name) {
                                    // Use the fetched name as chatroomName
                                    Chatroom chatroom = new Chatroom(recipientEmail, name, lastMessage, formattedDate);
                                    chatroomList.add(chatroom);
                                    chatroomAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    // Handle error
                                    Toast.makeText(InboxActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        chatroomAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(InboxActivity.this, "Error loading chatrooms.", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}
