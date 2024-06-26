package com.example.crescendo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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

import java.util.HashMap;
import java.util.LinkedList;


public class MainActivity extends AppCompatActivity {

    Button signInBtn, createAcntBtn;

    EditText usernameEdit, passwordEdit, nameEdit;
    FirebaseAuth auth;
    FirebaseFirestore database;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInBtn = findViewById(R.id.signInBtn);
        createAcntBtn = findViewById(R.id.createAcntBtn);
        usernameEdit = findViewById(R.id.usernameEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        nameEdit = findViewById(R.id.nameEdit);
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(myIntent);
        }

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //googleSignIn();
                signIn();
            }

        });
        createAcntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent (MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    private void signIn(){
        String email = String.valueOf(usernameEdit.getText());
        String password = String.valueOf(passwordEdit.getText());
        if (!isValidEmail(email)) {
            usernameEdit.setError("Please enter a valid email!");
            usernameEdit.requestFocus();
        }
        else if (password.length() < 6) {
            passwordEdit.setError("Password must be at least 6 characters!");
            passwordEdit.requestFocus();
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(myIntent);
                    } else {
                        Toast.makeText(MainActivity.this, "Error logging in", Toast.LENGTH_LONG).show();
                        passwordEdit.setError("Password may be incorrect");
                        passwordEdit.requestFocus();
                    }
                }
            });
        }
    }

    /*
    private void createUser() {
        String email = String.valueOf(usernameEdit.getText());
        String password = String.valueOf(passwordEdit.getText());

        if (nameEdit.getText().length() < 2) {
            nameEdit.setError("Please enter a name at least 2 characters long");
            nameEdit.requestFocus();
        }


        if (!isValidEmail(email)) {
            usernameEdit.setError("Please enter a valid email!");
            usernameEdit.requestFocus();
        }
        else if (password.length() < 6) {
            passwordEdit.setError("Password must be at least 6 characters!");
            passwordEdit.requestFocus();
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser newUser = auth.getCurrentUser();
                        if (newUser != null) {
                            System.out.println("not null user");
                            newUser.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(String.valueOf(nameEdit.getText())).build());
                        }
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("id", newUser.getUid());
                        map.put("name", nameEdit.getText());
                        map.put("wraps", new Archive(new LinkedList<>()));
                        database.collection("users").add(map);
                        Toast.makeText(MainActivity.this, "Account Registered", Toast.LENGTH_LONG).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(myIntent);
                                finish();
                            }
                        },1000);
                    } else {
                        Toast.makeText(MainActivity.this, "Error creating account", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    } */


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}