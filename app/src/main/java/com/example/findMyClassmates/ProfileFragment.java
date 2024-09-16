package com.example.findMyClassmates;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText nameText, idText;
    private Spinner role;
    private Button editProfileButton, saveButton, logoutButton;
    private String userEmail;
    private FirebaseFirestore db;
    private ArrayAdapter<CharSequence> roleSpinnerAdapter;
    private String selectedRole;



    public ProfileFragment(String userEmail){
        this.userEmail = userEmail;
    }

    public void setDB(FirebaseFirestore ff){
        db = ff;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        db = FirebaseFirestore.getInstance();
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        saveButton = view.findViewById(R.id.saveButton);
        logoutButton = view.findViewById(R.id.logoutButton);
        initializeProfile(view);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfileMode(getContext());
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfile();
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void editProfileMode(Context context) {
        nameText.setEnabled(true);
        idText.setEnabled(true);
        role.setEnabled(true);
    }

    private void saveProfile(){
        String newName = nameText.getText().toString();
        String newId = idText.getText().toString();
        Map<String, Object> newProfile = new HashMap<>();
        newProfile.put("name", newName);
        newProfile.put("id", newId);
        newProfile.put("role", selectedRole);
        // Update the new profile to database
        db.collection("users").document(userEmail)
                .set(newProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Profile updated",
                                Toast.LENGTH_SHORT).show();
                        nameText.setEnabled(false);
                        idText.setEnabled(false);
                        role.setEnabled(false);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error! Please retry.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public boolean isIdValid(String id) {
         return id != null && !id.trim().isEmpty();
    }

    // Initialize the profile using data from database
    private void initializeProfile(View view){
        nameText = view.findViewById(R.id.userName);
        idText = view.findViewById(R.id.id);
        role = (Spinner) view.findViewById(R.id.role);
        //Setup the drop down
        roleSpinnerAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.role_array,
                android.R.layout.simple_spinner_item
        );
        roleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role.setAdapter(roleSpinnerAdapter);
        role.setOnItemSelectedListener(this);
        role.setPrompt("Select your role");

        // Get the document of the current user
        DocumentReference usersRef = db.collection("users").document(userEmail);
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> userProfile = document.getData();
                    nameText.setText(isNameValid((String) userProfile.get("name")) ? userProfile.get("name").toString() : "");
                    idText.setText(isIdValid((String) userProfile.get("id")) ? userProfile.get("id").toString() : "");

                    // If the current user has not selected the role
                    if (!userProfile.containsKey("role")) {
                        selectedRole = "";
                    } else {
                        selectedRole = userProfile.get("role").toString();
                        role.setSelection(roleSpinnerAdapter.getPosition(selectedRole));
                    }
                }
            } else {
            }
        });
        nameText.setEnabled(false);
        idText.setEnabled(false);
        role.setEnabled(false);
    }

    private void logout(){
        startActivity(new Intent(getActivity(),LoginActivity.class));
    }

    // Close keyboard is clicked outside of EditText
    @Override
    public void onClick(View v){
        if (v != null){
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(),0);
        }

    }

    // For selecting drop-down bar/spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       selectedRole = parent.getItemAtPosition(position).toString();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}


