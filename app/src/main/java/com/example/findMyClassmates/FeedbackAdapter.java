package com.example.findMyClassmates;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    private List<Feedback> feedbackList;
    private Context context;
    private FirebaseFirestore db;



    public FeedbackAdapter(List<Feedback> feedbackList, Context context) {
        this.feedbackList = feedbackList;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedbackViewHolder holder, int position) {
        Feedback feedback = feedbackList.get(position);
        String courseName = feedback.getCourseName();
        holder.comments.setText(feedback.getComments());
        holder.courseRating.setText("Course Rating: " + (String.valueOf(feedback.getCourseRating())));
        holder.workloadRating.setText("Workload Rating: " + (String.valueOf(feedback.getWorkloadRating())));
        holder.professorRating.setText("Professor Rating: " + (String.valueOf(feedback.getProfessorRating())));
        holder.checkAttendance.setText("Check Attendance? " + (feedback.isCheckAttendance() ? "Yes" : "No"));
        holder.lateSubmission.setText("Allow Late Submission? " + (feedback.isLateSubmission() ? "Yes" : "No"));

        holder.voteUpCount.setText(String.valueOf(feedback.getVoteUp()));
        holder.voteDownCount.setText(String.valueOf(feedback.getVoteDown()));

        holder.voteUpButton.setEnabled(!userHasVoted(feedback));
        holder.voteDownButton.setEnabled(!userHasVoted(feedback));

        holder.voteUpButton.setOnClickListener(v -> {
            if (!userHasVoted(feedback)) {
                disableVotingButtons(holder);
                updateVoteCount(holder, feedback, true);
            } else {
                Toast.makeText(context, "You have already voted", Toast.LENGTH_SHORT).show();
            }
        });

        holder.voteDownButton.setOnClickListener(v -> {
            if (!userHasVoted(feedback)) {
                disableVotingButtons(holder);
                updateVoteCount(holder, feedback, false);
            } else {
                Toast.makeText(context, "You have already voted", Toast.LENGTH_SHORT).show();
            }
        });


        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if (feedback.getUserEmail().equals(currentUserEmail)) {
            holder.editFeedbackButton.setVisibility(View.VISIBLE);
            holder.deleteFeedbackButton.setVisibility(View.VISIBLE);

            holder.editFeedbackButton.setOnClickListener(v -> {
                Intent editIntent = new Intent(context, EditFeedbackActivity.class);
                editIntent.putExtra("feedback", feedback); // Assuming Feedback class is Serializable or Parcelable
                editIntent.putExtra("feedbackTokenId", feedback.getFeedbackTokenId());
                context.startActivity(editIntent);
            });

            holder.deleteFeedbackButton.setOnClickListener(v -> {
                deleteFeedbackFromFirestore(feedback.getCourseName(), feedback.getFeedbackTokenId());
                feedbackList.remove(position);
                notifyItemRemoved(position);
            });
        } else {
            holder.editFeedbackButton.setVisibility(View.GONE);
            holder.deleteFeedbackButton.setVisibility(View.GONE);
        }
    }

    // Check if the user has already voted on this feedback
    private boolean userHasVoted(Feedback feedback) {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Map<String, Boolean> userVotes = feedback.getUserVotes();
        // Ensure userVotes is not null
        if (userVotes == null) {
            // If null, initialize the map
            userVotes = new HashMap<>();
            feedback.setUserVotes(userVotes); // This ensures that we're updating the reference in the feedback object.
        }
        // Check if the user has voted
        return userVotes.containsKey(userEmail);
    }


    // Update vote count in the database
    private void updateVoteCount(FeedbackViewHolder holder, Feedback feedback, boolean isVoteUp) {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String feedbackId = feedback.getFeedbackTokenId();
        DocumentReference feedbackRef = db.collection("classes").document(feedback.getCourseName())
                .collection("feedbacks").document(feedbackId);

        db.runTransaction(transaction -> {
            DocumentSnapshot feedbackSnapshot = transaction.get(feedbackRef);

            Long voteUpLong = feedbackSnapshot.getLong("voteUp");
            int currentVoteUp = voteUpLong != null ? voteUpLong.intValue() : 0;

            Long voteDownLong = feedbackSnapshot.getLong("voteDown");
            int currentVoteDown = voteDownLong != null ? voteDownLong.intValue() : 0;

            Map<String, Boolean> userVotes = (Map<String, Boolean>) feedbackSnapshot.get("userVotes");

            if (userVotes == null) {
                userVotes = new HashMap<>();
            }

            if (userVotes.containsKey(userEmail)) {
                // User has already voted, we should not allow another vote
                return null;
            }

            // Update the user vote
            userVotes.put(userEmail, isVoteUp);

            // Increment the appropriate vote count
            if (isVoteUp) {
                transaction.update(feedbackRef, "voteUp", currentVoteUp + 1);
            } else {
                transaction.update(feedbackRef, "voteDown", currentVoteDown + 1);
            }
            transaction.update(feedbackRef, "userVotes", userVotes);

            return null;
        }).addOnSuccessListener(aVoid -> {
            // Success handling
            if (isVoteUp) {
                int newVoteUp = feedback.getVoteUp() + 1;
                feedback.setVoteUp(newVoteUp);
                holder.voteUpCount.setText(String.valueOf(newVoteUp));
            } else {
                int newVoteDown = feedback.getVoteDown() + 1;
                feedback.setVoteDown(newVoteDown);
                holder.voteDownCount.setText(String.valueOf(newVoteDown));
            }
            // Update local map to prevent multiple votes without needing a refresh
            feedback.getUserVotes().put(userEmail, isVoteUp);
        }).addOnFailureListener(e -> {
            // Failure handling
            Toast.makeText(context, "Error updating vote: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }



    private void deleteFeedbackFromFirestore(String courseName, String tokenId) {
        db.collection("classes").document(courseName).collection("feedbacks")
                .document(tokenId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this.context, "Feedback deleted.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this.context, "Error deleting feedback.", Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        public TextView comments, courseRating, workloadRating, professorRating, checkAttendance, lateSubmission;
        public Button editFeedbackButton, deleteFeedbackButton;
        public TextView voteUpCount, voteDownCount;
        public Button voteUpButton, voteDownButton;

        public FeedbackViewHolder(View itemView) {
            super(itemView);
            comments = itemView.findViewById(R.id.comments);
            courseRating = itemView.findViewById(R.id.courseRating);
            workloadRating = itemView.findViewById(R.id.workloadRating);
            professorRating = itemView.findViewById(R.id.professorRating);
            checkAttendance = itemView.findViewById(R.id.checkAttendance);
            lateSubmission = itemView.findViewById(R.id.lateSubmission);
            editFeedbackButton = itemView.findViewById(R.id.editFeedbackButton);
            deleteFeedbackButton = itemView.findViewById(R.id.deleteFeedbackButton);
            voteUpCount = itemView.findViewById(R.id.voteUpCount);
            voteDownCount = itemView.findViewById(R.id.voteDownCount);
            voteUpButton = itemView.findViewById(R.id.voteUpButton);
            voteDownButton = itemView.findViewById(R.id.voteDownButton);
        }
    }

    private void disableVotingButtons(FeedbackViewHolder holder) {
        holder.voteUpButton.setEnabled(false);
        holder.voteDownButton.setEnabled(false);
    }

    private void enableVotingButtons(FeedbackViewHolder holder) {
        holder.voteUpButton.setEnabled(true);
        holder.voteDownButton.setEnabled(true);
    }

}
