package com.example.findMyClassmates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView buttonSignup;
    private FirebaseAuth mAuth;

    public void setmAuth(FirebaseAuth mAuth){
        this.mAuth = mAuth;
    }

    public boolean isValidEmail(String email) {
        // Define your email validation logic
        return !email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidPassword(String password) {
        // Define your password validation logic
        return !password.isEmpty() && password.length() >= 6;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup = findViewById(R.id.buttonSignup);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                login(userEmail, password);
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    public void login(String userEmail, String password) {
        if (isValidEmail(userEmail) && isValidPassword(password)) {
            mAuth.signInWithEmailAndPassword(userEmail, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login successfully",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("userEmail", userEmail);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Login failed. Please correct your password/account",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
