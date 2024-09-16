package com.example.findMyClassmates;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FeedbackActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView feedbackRecyclerView;
    private Button rateClassButton;
    private List<Feedback> feedbackList;
    private FeedbackAdapter feedbackAdapter; // Assuming you have a FeedbackAdapter class for displaying feedback in the RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback); // Assuming you have a layout file named activity_feedback

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            // Finish the current activity, which will take you back to the previous activity in the stack.
            finish();
        });

        feedbackRecyclerView = findViewById(R.id.feedbackRecyclerView);
        feedbackRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        feedbackList = new ArrayList<>();
        feedbackAdapter = new FeedbackAdapter(feedbackList, this);
        feedbackRecyclerView.setAdapter(feedbackAdapter);
        feedbackRecyclerView.setNestedScrollingEnabled(false);

        db = FirebaseFirestore.getInstance();

        loadFeedbacks();

        rateClassButton = findViewById(R.id.rateClassButton);
        rateClassButton.setOnClickListener(view -> {
            Intent intent = new Intent(FeedbackActivity.this, WriteFeedbackActivity.class);
            intent.putExtra("CLASS_NAME", getIntent().getStringExtra("CLASS_NAME"));  // Passing the class name to WriteFeedbackActivity
            startActivity(intent);
            finish();
        });

    }

    private void loadFeedbacks() {
        String courseName = getIntent().getStringExtra("CLASS_NAME");

        db.collection("classes").document(courseName).collection("feedbacks").whereEqualTo("courseName", courseName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        feedbackList.clear(); // Clear existing feedback in case this is a refresh
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Assuming the Feedback class has a constructor that takes these arguments
                            Feedback feedback = document.toObject(Feedback.class);
                            // Set the feedbackTokenId, which isn't a field of the Feedback class
                            feedback.setFeedbackTokenId(document.getId());
                            feedbackList.add(feedback);
                        }
                        feedbackAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(FeedbackActivity.this, "Error loading feedbacks.", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}
