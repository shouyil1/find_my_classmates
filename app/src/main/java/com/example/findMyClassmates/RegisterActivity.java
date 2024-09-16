package com.example.findMyClassmates;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextName;
    private Button buttonRegister;
    private TextView buttonLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public boolean isEmailValid(String email) {
        return !email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    public boolean doPasswordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public void setEditTextEmail(EditText editTextEmail) {
        this.editTextEmail = editTextEmail;
    }

    public void setEditTextPassword(EditText editTextPassword) {
        this.editTextPassword = editTextPassword;
    }

    public void setEditTextConfirmPassword(EditText editTextConfirmPassword) {
        this.editTextConfirmPassword = editTextConfirmPassword;
    }

    public void setEditTextName(EditText editTextName) {
        this.editTextName = editTextName;
    }

    public void setButtonRegister(Button buttonRegister) {
        this.buttonRegister = buttonRegister;
    }

    public Button getButtonRegister(){
        return this.buttonRegister;
    }

    public void setButtonLogin(TextView buttonLogin) {
        this.buttonLogin = buttonLogin;
    }

    public void setFirebaseAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public void setFirebaseFirestore(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextName = findViewById(R.id.editTextName);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();

                String name = editTextName.getText().toString();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();
                // Firebase require password to be more than 6 digits
                if (!isPasswordValid(password)) {
                    Toast.makeText(RegisterActivity.this, "Password should be at least 6 digits",
                            Toast.LENGTH_SHORT).show();
                }
                else if (!isEmailValid(email)){
                    Toast.makeText(RegisterActivity.this, "Email address is not valid",
                            Toast.LENGTH_SHORT).show();
                }
                else if (doPasswordsMatch(password,confirmPassword)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NotNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        addNewUser(email,name);
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(RegisterActivity.this, "Account created",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.putExtra("userEmail", email);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Authorization failed. Display the exception message
                                        String errorMessage = task.getException().getLocalizedMessage();
                                        Toast.makeText(RegisterActivity.this, errorMessage,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    // Add new user to database
    private void addNewUser(String email, String name) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        CollectionReference colRef = db.collection("users");
        DocumentReference newUser = colRef.document(email);
        newUser.set(user);
    }
}
