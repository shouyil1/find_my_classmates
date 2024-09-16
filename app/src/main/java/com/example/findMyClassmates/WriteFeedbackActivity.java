package com.example.findMyClassmates;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WriteFeedbackActivity extends AppCompatActivity {
    private RatingBar courseRatingBar, workloadRatingBar, professorRatingBar;
    private EditText commentsEditText;
    private Switch checkAttendanceSwitch, lateSubmissionSwitch;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_feedback);  // Assuming you have a layout file named activity_write_feedback

        courseRatingBar = findViewById(R.id.courseRatingBar);
        workloadRatingBar = findViewById(R.id.workloadRatingBar);
        professorRatingBar = findViewById(R.id.professorRatingBar);
        commentsEditText = findViewById(R.id.commentsEditText);
        checkAttendanceSwitch = findViewById(R.id.checkAttendanceSwitch);
        lateSubmissionSwitch = findViewById(R.id.lateSubmissionSwitch);
        db = FirebaseFirestore.getInstance();

        findViewById(R.id.submitFeedbackButton).setOnClickListener(view -> {
            saveFeedback();
        });
    }

    private void saveFeedback() {
        String className = getIntent().getStringExtra("CLASS_NAME");

        int courseRating = courseRatingBar.getProgress();
        int workloadRating = workloadRatingBar.getProgress();
        int professorRating = professorRatingBar.getProgress();
        String comments = commentsEditText.getText().toString();
        boolean checkAttendance = checkAttendanceSwitch.isChecked();
        boolean lateSubmission = lateSubmissionSwitch.isChecked();

        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        Feedback feedback = new Feedback(0, userEmail, className, courseRating, comments, workloadRating, professorRating, checkAttendance, lateSubmission, 0, 0, new HashMap<>());

        db.collection("classes").document(className).collection("feedbacks").add(feedback)
                .addOnSuccessListener(documentReference -> {
                    // After success, set the feedbackTokenId with the newly generated document ID
                    String feedbackTokenId = documentReference.getId();
                    feedback.setFeedbackTokenId(feedbackTokenId);

                    // Update the document with the feedbackTokenId
                    documentReference.update("feedbackTokenId", feedbackTokenId)
                            .addOnSuccessListener(aVoid -> {
                                // Document updated successfully with the feedbackTokenId
                            })
                            .addOnFailureListener(e -> {
                                // Handle the error
                            });
                    Toast.makeText(this, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show();

                    // Start the FeedbackActivity and pass the className
                    Intent intent = new Intent(WriteFeedbackActivity.this, FeedbackActivity.class);
                    intent.putExtra("CLASS_NAME", className);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error submitting feedback.", Toast.LENGTH_SHORT).show();
                });
    }


}
