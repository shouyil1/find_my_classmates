package com.example.findMyClassmates;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Feedback implements Serializable {

    private int feedbackId;
    private String userEmail;
    private String courseName;
    private int courseRating;
    private String comments;
    private int workloadRating;
    private int professorRating;
    private boolean checkAttendance;
    private boolean lateSubmission;
    private String feedbackTokenId;
    private int voteUp;
    private int voteDown;
    private Map<String, Boolean> userVotes;

    // Default Constructor for Firebase
    public Feedback() {}

    // Constructor
    public Feedback(int feedbackId, String userEmail, String courseName, int courseRating,
                    String comments, int workloadRating, int professorRating, boolean checkAttendance,
                    boolean lateSubmission, int voteUp, int voteDown, Map<String, Boolean> userVotes) {
        this.feedbackId = feedbackId;
        this.userEmail = userEmail;
        this.courseName = courseName;
        this.courseRating = courseRating;
        this.comments = comments;
        this.workloadRating = workloadRating;
        this.professorRating = professorRating;
        this.checkAttendance = checkAttendance;
        this.lateSubmission = lateSubmission;
        this.voteUp = voteUp;
        this.voteDown = voteDown;
        this.userVotes = userVotes;
    }

    // Getters and Setters
    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseRating() {
        return courseRating;
    }

    public void setCourseRating(int courseRating) {
        this.courseRating = courseRating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getWorkloadRating() {
        return workloadRating;
    }

    public void setWorkloadRating(int workloadRating) {
        this.workloadRating = workloadRating;
    }

    public int getProfessorRating() {
        return professorRating;
    }

    public void setProfessorRating(int professorRating) {
        this.professorRating = professorRating;
    }

    public boolean isCheckAttendance() {
        return checkAttendance;
    }

    public void setCheckAttendance(boolean checkAttendance) {
        this.checkAttendance = checkAttendance;
    }

    public boolean isLateSubmission() {
        return lateSubmission;
    }

    public void setLateSubmission(boolean lateSubmission) {
        this.lateSubmission = lateSubmission;
    }

    public String getFeedbackTokenId() {
        return feedbackTokenId;
    }

    public void setFeedbackTokenId(String feedbackTokenId) {
        this.feedbackTokenId = feedbackTokenId;
    }

    public int getVoteUp() {
        return voteUp;
    }

    public void setVoteUp(int cnt) {
        this.voteUp = cnt;
    }

    public int getVoteDown() {
        return voteDown;
    }

    public void setVoteDown(int cnt) {
        this.voteDown = cnt;
    }

    public Map<String, Boolean> getUserVotes() {
        if (this.userVotes == null) {
            this.userVotes = new HashMap<>();
        }
        return userVotes;
    }

    public void setUserVotes(Map<String, Boolean> userVotes) {
        this.userVotes = userVotes;
    }

}
