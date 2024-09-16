package com.example.findMyClassmates;

import java.util.ArrayList;
import java.util.List;

public class Course {

    // Attributes
    private String courseId;
    private String courseName;
    private String department;
    private String courseDescription;
    private String courseInstructor;
    private String semester;
    private List<User> classmates; // Assuming User is a class in your project

    // Constructor
    public Course(String courseId, String courseName, String department, String courseDescription, String courseInstructor, String semester) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.department = department;
        this.courseDescription = courseDescription;
        this.courseInstructor = courseInstructor;
        this.semester = semester;
        this.classmates = new ArrayList<>();
    }

    // Methods
    public String getCourseId() {
        return courseId;
    }

    public String getCourseDetails() {
        return "Name: " + courseName + "\nDepartment: " + department + "\nDescription: " + courseDescription + "\nInstructor: " + courseInstructor + "\nSemester: " + semester;
    }

    public List<User> getClassmates() {
        return classmates;
    }

    public void addStudent(User student) {
        classmates.add(student);
    }

    public void removeStudent(User student) {
        classmates.remove(student);
    }

}
