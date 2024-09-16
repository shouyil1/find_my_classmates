package com.example.findMyClassmates;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class EnrollmentExpandableListAdapter implements ExpandableListAdapter {
    private final String CLASS_ENROLLED_FIELD = "class_enrolled";
    private final String USERS_COLLECTION = "users";
    private Context context;
    private List<String> enrolledClasses;
    private String userEmail;
    private FirebaseFirestore db;

    // Customize adapter
    public EnrollmentExpandableListAdapter(Context context, List<String> enrolledClasses, String userEmail) {
        this.context = context;
        this.enrolledClasses = enrolledClasses;
        this.userEmail = userEmail;
        this.db = FirebaseFirestore.getInstance();
    }
    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        return enrolledClasses.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return enrolledClasses.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String className = getGroup(i).toString();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.enrolled_class_item, null);
        }
        TextView item = view.findViewById(R.id.enrolled_class_name);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(className);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.enrolled_class_features, null);
        }
        Button addCommentButton = view.findViewById(R.id.seeCommentButton);
        Button findClassmatesButton = view.findViewById(R.id.findClassmatesButton);
        findClassmatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFindActivity(enrolledClasses.get(i));
            }
        });

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFeedbackActivity(enrolledClasses.get(i));
            }
        });

        return view;
    }

    private void navigateToFindActivity(String className) {
        Intent intent = new Intent(context, FindActivity.class);
        intent.putExtra("CLASS_NAME", className);
        context.startActivity(intent);
    }

    private void navigateToFeedbackActivity(String className) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        intent.putExtra("CLASS_NAME", className);
        context.startActivity(intent);
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int i) {

    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    @Override
    public long getCombinedChildId(long l, long l1) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long l) {
        return 0;
    }
}
