package com.example.findMyClassmates;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditFeedbackActivity extends AppCompatActivity {

    private EditText commentsEditText;
    private RatingBar courseRatingBar, workloadRatingBar, professorRatingBar;
    private Switch checkAttendanceSwitch, lateSubmissionSwitch;
    private Button updateFeedbackButton;
    private FirebaseFirestore db;
    private String feedbackTokenId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_feedback);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        commentsEditText = findViewById(R.id.commentsEditText);
        courseRatingBar = findViewById(R.id.courseRatingBar);
        workloadRatingBar = findViewById(R.id.workloadRatingBar);
        professorRatingBar = findViewById(R.id.professorRatingBar);
        checkAttendanceSwitch = findViewById(R.id.checkAttendanceSwitch);
        lateSubmissionSwitch = findViewById(R.id.lateSubmissionSwitch);
        updateFeedbackButton = findViewById(R.id.updateFeedbackButton);

        // Get the feedback data passed from FeedbackAdapter
        Feedback feedback = (Feedback) getIntent().getSerializableExtra("feedback");
        feedbackTokenId = getIntent().getStringExtra("feedbackTokenId");

        // Populate the fields with existing feedback data
        if (feedback != null) {
            commentsEditText.setText(feedback.getComments());
            courseRatingBar.setRating(feedback.getCourseRating());
            workloadRatingBar.setRating(feedback.getWorkloadRating());
            professorRatingBar.setRating(feedback.getProfessorRating());
            checkAttendanceSwitch.setChecked(feedback.isCheckAttendance());
            lateSubmissionSwitch.setChecked(feedback.isLateSubmission());
        }

        // Setup listener for the update button
        updateFeedbackButton.setOnClickListener(v -> {
            // Retrieve updated values from input fields
            feedback.setComments(commentsEditText.getText().toString());
            feedback.setCourseRating((int) courseRatingBar.getRating());
            feedback.setWorkloadRating((int) workloadRatingBar.getRating());
            feedback.setProfessorRating((int) professorRatingBar.getRating());
            feedback.setCheckAttendance(checkAttendanceSwitch.isChecked());
            feedback.setLateSubmission(lateSubmissionSwitch.isChecked());

            // Update feedback in Firestore
            updateFeedbackInFirestore(feedback);
        });
    }

    private void updateFeedbackInFirestore(Feedback feedback) {
        String courseName = feedback.getCourseName();
        db.collection("classes").document(courseName).collection("feedbacks")
                .document(feedbackTokenId)
                .set(feedback)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditFeedbackActivity.this, "Feedback updated.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditFeedbackActivity.this, FeedbackActivity.class);
                    intent.putExtra("CLASS_NAME", courseName);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditFeedbackActivity.this, "Error updating feedback.", Toast.LENGTH_SHORT).show();
                });
    }
}
