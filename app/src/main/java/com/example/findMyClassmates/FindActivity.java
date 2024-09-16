package com.example.findMyClassmates;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FindActivity extends AppCompatActivity {

    private RecyclerView usersRecyclerView;
    private UserAdapter usersAdapter;
    private Context context;
    private List<User> userList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            // Finish the current activity, which will take you back to the previous activity in the stack.
            finish();
        });

        usersRecyclerView = findViewById(R.id.classmatesRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new UserAdapter(context, userList);
        usersRecyclerView.setAdapter(usersAdapter);

        String className = getIntent().getStringExtra("CLASS_NAME");

        if (className != null) {
            TextView classMatesTitle = findViewById(R.id.classMatesTitle);
            classMatesTitle.setText("Your Classmates in " + className);
            fetchStudentsFromSameClass(className);
        }
    }

    private void fetchStudentsFromSameClass(String currentUsersClass) {
            db.collection("users").whereArrayContains("class_enrolled", currentUsersClass)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String email = document.getId();
                            if(!email.equals(userEmail)) {
                            String name = document.getString("name");
                            String role = document.getString("role");

                            User user = new User(name, email, role); // Assuming User class has a constructor that accepts these fields
                            userList.add(user);
                            }
                        }
                        usersAdapter.notifyDataSetChanged();
                    } else {

                    }
                });
    }





}
