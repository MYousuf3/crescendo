package com.example.crescendo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    Button signInBtn, createActBtn;

    EditText usernameEdit, passwordEdit, nameEdit;
    FirebaseAuth auth;
    FirebaseDatabase database;
    GoogleSignInClient mGoogleSignInClient;

    public static final String CLIENT_ID = "447825334968-e9gmfipr27kn997nr8cgshhvlu99o9d6.apps.googleusercontent.com";
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;
    int RC_SIGN_IN = 28;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInBtn = findViewById(R.id.signInBtn);
        createActBtn = findViewById(R.id.createAcntBtn);
        usernameEdit = findViewById(R.id.usernameEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        nameEdit = findViewById(R.id.nameEdit);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Intent myIntent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(myIntent);
        }



        /*
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(CLIENT_ID)
                .requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
         */
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //googleSignIn();
                signIn();
            }

        });
        createActBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
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
                    Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(myIntent);
                }
            });
        }
    }

    private void createUser() {
        String email = String.valueOf(usernameEdit.getText());
        String password = String.valueOf(passwordEdit.getText());
        if (nameEdit.getText().length() < 2) {
            nameEdit.setError("Please enter a name at least 2 characters long");
            nameEdit.requestFocus();
        }
        else if (!isValidEmail(email)) {
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
                        Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(myIntent);
                    }
                }
            });
        }
    }

    /*
    private void googleSignIn() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", user.getUid());
                            map.put("name", user.getDisplayName());
                            map.put("profile", user.getPhotoUrl().toString());

                            database.getReference().child("users").child(user.getUid()).setValue(map);

                            Intent myIntent = new Intent(MainActivity.this, MainActivity2.class);
                            startActivity(myIntent);

                        } else {
                            Toast.makeText(MainActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
     */

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}