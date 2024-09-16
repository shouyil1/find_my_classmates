package com.example.findMyClassmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnrollmentFragment extends Fragment {
    private final String DB_COLLECTION_NAME = "users";
    private final String DB_FIELD_NAME = "class_enrolled";
    private String userEmail;
    private FirebaseFirestore db;
    private List<String> enrolledClassList;
    private Map<String, List<String>> enrolledClassCollection;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;

    public EnrollmentFragment(String userEmail){
        this.userEmail = userEmail;
    }

    // Inflate the fragment_enrollment layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enrollment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        expandableListView = view.findViewById(R.id.enrollments);
        readEnrolledClassFromDB();

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
                String selected = expandableListAdapter.getChild(i, i1).toString();
                Toast.makeText(getContext(), "Selected" + selected, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    // Read all the classes that the current user has enrolled
    private void readEnrolledClassFromDB() {
        if (userEmail == null) {
            // userEmail is null, handle accordingly
            return;
        }
        enrolledClassList = new ArrayList<>();
        enrolledClassCollection = new HashMap<>();
        // Get the reference of the current user document
        DocumentReference userRef = db.collection(DB_COLLECTION_NAME).document(userEmail);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()&& task.getResult() != null){
                    DocumentSnapshot userDocumentSnapshot = task.getResult();
                    // Convert the field to list
                    List<String> enrolledClasses = (List<String>) userDocumentSnapshot.get(DB_FIELD_NAME);
                    // Set up the adapter
                    if(enrolledClasses !=null){
                        expandableListAdapter = new EnrollmentExpandableListAdapter(getContext(), enrolledClasses, userEmail);
                        expandableListView.setAdapter(expandableListAdapter);
                    }
                }
            }
        });
    }

    // When the user return to current tab, reassign the adapter
    @Override
    public void onResume() {
        super.onResume();
        readEnrolledClassFromDB();
    }

    // Clean up view setup after destroyed
    @Override
    public void onDestroyView() {
        expandableListAdapter = null;
        expandableListView = null;
        super.onDestroyView();
    }
}
