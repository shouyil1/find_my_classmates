package com.example.findMyClassmates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private String userId;
    private String name;
    private String email;
    private String password;
    private String profilePicture;
    private String userType;
    private List<String> coursesEnrolled;

    // Constructor
    public User(String name, String email, String userType) {
        this.name = name;
        this.email = email;
        this.userType = userType;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<String> getCoursesEnrolled() {
        return coursesEnrolled;
    }

    public void setCoursesEnrolled(List<String> coursesEnrolled) {
        this.coursesEnrolled = coursesEnrolled;
    }

    public void register() {
    }

    // 我觉得login 和 register 应该不能在user class 里
    public boolean login(String email, String password) {
        return true;
    }


    public void editProfile() {
        // profile editing logic here
    }

    public void viewProfile() {
        // profile viewing logic here
    }

    public void enrollCourse(String courseId) {
        this.coursesEnrolled.add(courseId);
        //
    }

    public void unenrollCourse(String courseId) {
        this.coursesEnrolled.remove(courseId);
    }


}

