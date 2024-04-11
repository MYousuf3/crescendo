package com.example.crescendo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SettingsActivity extends AppCompatActivity {

    EditText nameChange, passChange;
    Button signOut, deleteAct, cancel, confirmName, confirmPass;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        nameChange = findViewById(R.id.nameSettingsEdit);
        passChange = findViewById(R.id.passwordSettingsEdit);
        signOut = findViewById(R.id.signOutBtnSettings);
        deleteAct = findViewById(R.id.deleteAccountButton);
        cancel = findViewById(R.id.cancelButton);
        confirmName = findViewById(R.id.confirmNameButton);
        confirmPass = findViewById(R.id.confirmPasswordButton);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

        deleteAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert user != null;
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SettingsActivity.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
                        startActivity(myIntent);
                    }
                });
            }
        });

        confirmName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = nameChange.getText().toString();
                if (newName.length() > 2) {
                    assert user != null;
                    if (!newName.equals(user.getDisplayName())) {
                        user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(newName).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(SettingsActivity.this, "Name Changed!", Toast.LENGTH_SHORT).show();
                                nameChange.setText("");
                            }
                        });
                    } else {
                        nameChange.requestFocus();
                        nameChange.setError("Name change must be different.");
                    }
                } else {
                    nameChange.requestFocus();
                    nameChange.setError("Name change must be at least 2 characters long.");
                }
            }
        });

        confirmPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPass = passChange.getText().toString();
                if (newPass.length() < 6) {
                    passChange.setError("Password must be at least 6 characters!");
                    passChange.requestFocus();
                } else {
                    assert user != null;
                    user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(SettingsActivity.this, "Password Changed!", Toast.LENGTH_SHORT).show();
                            passChange.setText("");
                        }
                    });
                }
            }
        });


    }
}