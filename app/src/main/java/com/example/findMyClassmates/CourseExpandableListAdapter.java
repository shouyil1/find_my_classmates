package com.example.findMyClassmates;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class CourseExpandableListAdapter extends BaseExpandableListAdapter {
    private final String CLASS_ENROLLED_FIELD = "class_enrolled";
    private final String USERS_COLLECTION = "users";
    private final String COURSES_COLLECTION = "courses";
    private final String DEPARTMENT_COLLECTION = "departments";
    private final String COURSE_STUDENTS_LIST_FIELD = "students_list";
    private String currentDepartment;
    private Context context;
    private Map<String, List<String>> courseCollection;
    private List<String> departmentList;
    private List<String> descriptionList;
    private String userEmail;
    private FirebaseFirestore db;
    private HashSet<String> enrolledClasses;
    private ExpandableListView nestedListView;

    // Customize the adapter
    public CourseExpandableListAdapter(Context context, List<String> departmentList, Map<String, List<String>> courseCollection, String userEmail) {
        this.context = context;
        this.courseCollection = courseCollection;
        this.departmentList = departmentList;
        this.userEmail = userEmail;
        this.db = FirebaseFirestore.getInstance();
        enrolledClasses = new HashSet<>();
        getUserEnrollments();
    }

    @Override
    public int getGroupCount() {
        return courseCollection.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return courseCollection.get(departmentList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return departmentList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return courseCollection.get(departmentList.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String department = getGroup(i).toString();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.department_item, null);
        }
        TextView item = view.findViewById(R.id.courses);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(department);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        currentDepartment = getGroup(i).toString();
        String courseName = getChild(i, i1).toString();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.course_item, null);
        }
        TextView classItem = view.findViewById(R.id.course);
        classItem.setText(courseName);

        Button enrollButton = view.findViewById(R.id.enrollButton);

        // When new section expanded, check the enrollment status of each course
        if(enrolledClasses.contains(courseName)){
            enrollButton.setText("Drop");
        }else{
            enrollButton.setText("Add");
        }
        // Refresh the view with changes
        notifyDataSetChanged();
        classItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getClassesDescription(courseName);
            }
        });
        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean enrolled = enrolledClasses.contains(courseName);

                // Setup the pop up window
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(enrolled? "Do you want to drop " + courseName + "?":"Do you want to enroll in " + courseName + "?");

                if (!enrolled){
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            updateEnrollment(courseName, false);
                            updateStudentsList(courseName, false);
                            enrolledClasses.add(courseName);
                            enrollButton.setText("Drop");
                            notifyDataSetChanged();
                        }
                    });
                }else{
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            updateEnrollment(courseName, true);
                            updateStudentsList(courseName, true);
                            enrolledClasses.remove(courseName);
                            enrollButton.setText("Add");
                            notifyDataSetChanged();
                        }
                    });
                }

                builder.setCancelable(true);
                // If select "No" in the pop up window
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return view;
    }

    private void getClassesDescription(String courseName) {
        DocumentReference courseDoc = db.collection(DEPARTMENT_COLLECTION).document(currentDepartment).collection(COURSES_COLLECTION).document(courseName);
        courseDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot course = task.getResult();
                    String courseDescription = course.get("description").toString();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(courseDescription);
                    builder.setCancelable(true);
                    builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private void updateEnrollment(String courseName, Boolean enrolled) {
        DocumentReference user = db.collection(USERS_COLLECTION).document(userEmail);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userDocument = task.getResult();
                    // If the current user has a "class_enrolled" field and has not enrolled in this class
                    if (userDocument.contains(CLASS_ENROLLED_FIELD) && !enrolled) {
                        user.update(CLASS_ENROLLED_FIELD, FieldValue.arrayUnion(courseName));
                    }
                    // If the current user does not have a "class_enrolled" field and has not enrolled in this class
                    else if (!enrolled) {
                        Map<String, Object> classEnrolled = new HashMap<>();
                        // Create a "class_enrolled" field
                        classEnrolled.put(CLASS_ENROLLED_FIELD, Arrays.asList(courseName));
                        // Add the new class
                        user.update(classEnrolled);
                    }
                    // If the current user has a "class_enrolled" field and has enrolled in this class
                    else {
                        // Remove the class from the field
                        user.update(CLASS_ENROLLED_FIELD, FieldValue.arrayRemove(courseName));
                    }
                }
            }
        });
    }

    private void updateStudentsList(String courseName, Boolean enrolled){
        DocumentReference course = db.collection(DEPARTMENT_COLLECTION).document(currentDepartment).collection(COURSES_COLLECTION).document(courseName);
        course.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot courseDocument = task.getResult();
                    // If the current user has a "class_enrolled" field and has not enrolled in this class
                    if (courseDocument.contains(COURSE_STUDENTS_LIST_FIELD) && !enrolled) {
                        course.update(COURSE_STUDENTS_LIST_FIELD, FieldValue.arrayUnion(userEmail));
                    }
                    // If the current user does not have a "class_enrolled" field and has not enrolled in this class
                    else if (!enrolled) {
                        Map<String, Object> studentsList = new HashMap<>();
                        // Create a "class_enrolled" field
                        studentsList.put(COURSE_STUDENTS_LIST_FIELD, Arrays.asList(userEmail));
                        // Add the new class
                        course.update(studentsList);
                    }
                    // If the current user has a "class_enrolled" field and has enrolled in this class
                    else {
                        // Remove the class from the field
                        course.update(COURSE_STUDENTS_LIST_FIELD, FieldValue.arrayRemove(userEmail));
                    }
                }
            }
        });    }

    private void getUserEnrollments(){
        if (userEmail == null) {
            // Handle the null case, perhaps log an error or set up default values
            return;
        }
        DocumentReference user = db.collection(USERS_COLLECTION).document(userEmail);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userDocument = task.getResult();
                    // If the current user has a "class_enrolled" field and has not enrolled in this class
                    if (userDocument.contains(CLASS_ENROLLED_FIELD)) {
                        List<String> enrollments = (List<String>) userDocument.get(CLASS_ENROLLED_FIELD);
                        for(String course : enrollments){
                            enrolledClasses.add(course);
                        }
                    }
                }
            }
        });
    }
}
