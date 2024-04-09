package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;
    TextView title;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        title = findViewById(R.id.title);

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            title.setText("Welcome " + currentUser.getDisplayName());
        }

        findViewById(R.id.playWrappedButton).setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, TransitionActivity.class);
            intent.putExtra(TransitionActivity.NEXT_ACTIVITY_KEY, "song");
            startActivity(intent);
        });
    }
}