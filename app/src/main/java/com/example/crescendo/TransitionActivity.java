package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class TransitionActivity extends AppCompatActivity {

    public static final String NEXT_ACTIVITY_KEY = "next_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        // Determine next activity based on the extra passed with the intent
        String nextActivity = getIntent().getStringExtra(NEXT_ACTIVITY_KEY);
        new Handler().postDelayed(() -> {
            Intent intent;
            switch (nextActivity) {
                case "song":
                    intent = new Intent(TransitionActivity.this, SongActivity.class);
                    break;
                case "artist":
                    intent = new Intent(TransitionActivity.this, ArtistActivity.class);
                    break;
                case "genre":
                    intent = new Intent(TransitionActivity.this, GenresActivity.class);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + nextActivity);
            }
            startActivity(intent);
            finish(); // Finish TransitionActivity
        }, 3000); // Delay for 3 seconds
    }
}