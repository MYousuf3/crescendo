package com.example.crescendo;

import static android.content.ContentValues.TAG;
import static com.example.crescendo.MainActivity.isValidEmail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class RegisterActivity extends AppCompatActivity {
    Button createAcntBtn, signInBtn;

    EditText usernameEdit, passwordEdit, nameEdit, confirmPasswordEdit;
    FirebaseAuth auth;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signInBtn = findViewById(R.id.signInBtn);
        createAcntBtn = findViewById(R.id.createAcntBtn);

        usernameEdit = findViewById(R.id.usernameEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        nameEdit = findViewById(R.id.nameEdit);
        confirmPasswordEdit = findViewById(R.id.confirmPasswordEdit);

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(signInIntent);
            }
        });

        createAcntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }

    private void createUser() {
        String email = String.valueOf(usernameEdit.getText());
        String password = String.valueOf(passwordEdit.getText());
        String confirmPass = String.valueOf(confirmPasswordEdit.getText());

        if (nameEdit.getText().length() < 2) {
            nameEdit.setError("Please enter a name at least 2 characters long");
            nameEdit.requestFocus();
        }
        if (!isValidEmail(email)) {
            usernameEdit.setError("Please enter a valid email!");
            usernameEdit.requestFocus();
        } else if (password.length() < 6) {
            passwordEdit.setError("Password must be at least 6 characters!");
            passwordEdit.requestFocus();
        } else if (confirmPass.isEmpty() || !confirmPass.equals(password)) {
            confirmPasswordEdit.setError("Password and confirm password fields must match.");
            confirmPasswordEdit.requestFocus();
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser newUser = auth.getCurrentUser();
                        if (newUser != null) {
                            newUser.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(String.valueOf(nameEdit.getText())).build());
                        }
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("id", newUser.getUid());
                        map.put("name", nameEdit.getText().toString());
                        JSONArray jsonArray = new JSONArray();
                        map.put("wraps", jsonArray.toString());
                        database.collection("users").add(map);
                        Toast.makeText(RegisterActivity.this, "Account Registered", Toast.LENGTH_LONG).show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent myIntent = new Intent(RegisterActivity.this, HomeActivity.class);
                                startActivity(myIntent);
                                finish();
                            }
                        },1500);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error creating account", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}