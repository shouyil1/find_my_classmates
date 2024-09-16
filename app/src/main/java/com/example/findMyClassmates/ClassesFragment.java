package com.example.findMyClassmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassesFragment extends Fragment {
    private final String DB_COLLECTION_NAME = "departments";
    private final String DB_DEPARTMENT_COURSES_NAME = "courses";
    private String userEmail;
    private List<String> departmentList;
    private List<String> courseList;
    private Map<String, List<String>> departmentCourseCollection;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private FirebaseFirestore db;

    // Save email to access the database of current user
    public ClassesFragment(String userEmail) {
        this.userEmail = userEmail;
    }

    // Inflate fragment_classes layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_classes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        expandableListView = view.findViewById(R.id.departments);
        readCourseListsFromDB();

        // Keep track of which section in expanded, close the current one if another is expanded
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;

            @Override
            public void onGroupExpand(int i) {
                if (lastExpandedPosition != -1 && i != lastExpandedPosition) {
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = i;
            }
        });
        // Allow child items to be selectable
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                return true;
            }
        });
    }

    // Get the reference of department documents from firebase
    private void readCourseListsFromDB() {
        departmentList = new ArrayList<>();
        departmentCourseCollection = new HashMap<>();
        CollectionReference departmentRef = db.collection(DB_COLLECTION_NAME);
        departmentRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                readDepartmentsFromDB(task, departmentRef);
            }
        });
    }

    // Read the list of department documents
    private void readDepartmentsFromDB(Task<QuerySnapshot> task, CollectionReference departmentRef) {
        for (DocumentSnapshot doc : task.getResult()) {
            // Read the document id, which is the department name
            String departmentName = doc.getId();
            departmentList.add(departmentName);
            // Get the reference of specific department document
            DocumentReference courseRef = departmentRef.document(departmentName);
            courseRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(Task<DocumentSnapshot> task) {
                    readCoursesFromDepartments(departmentName, courseRef);
                }
            });
        }
    }

    // Read the list of courses within a department document
    private void readCoursesFromDepartments(String departmentName, DocumentReference courseRef) {
        if (userEmail == null) {
            // Handle the null case, perhaps log an error or set up default values
            return;
        }
        // Get the "courses" collection within the current department document
        CollectionReference courses = courseRef.collection(DB_DEPARTMENT_COURSES_NAME);
        courses.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    courseList = new ArrayList<>();
                    // Read the document id, which is the course names
                    for (DocumentSnapshot course : task.getResult()) {
                        String courseName = course.getId();
                        courseList.add(courseName);
                    }
                    // Put the current department name with all its courses in a map
                    departmentCourseCollection.put(departmentName, courseList);
                    // Set up adapter
                    expandableListAdapter = new CourseExpandableListAdapter(getContext(), departmentList, departmentCourseCollection, userEmail);
                    expandableListView.setAdapter(expandableListAdapter);
                }
                else {
                    // Handle error
                    Exception e = task.getException();
                }

            }
        });
    }

    // When the user return to current tab, reassign the adapter
    @Override
    public void onResume() {
        super.onResume();
        expandableListView.setAdapter(expandableListAdapter);
    }

    // Clean up view setup after destroyed
    @Override
    public void onDestroyView() {
        expandableListAdapter = null;
        expandableListView = null;
        super.onDestroyView();
    }
}
