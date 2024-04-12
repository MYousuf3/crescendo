package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class TransitionActivity extends AppCompatActivity {

    public static final String NEXT_ACTIVITY_KEY = "next_activity";
    Random rand;
    TextView transitionText;
    String[] quips = new String[]{
                                  "Drumroll please...",
                                  "You're not ready for this",
                                  "Hold on to your seats...",
                                  "And now for the big reveal...",
                                  "Cue the fanfare...",
                                  "Hold your applause...",
                                  "Get ready for this...",
                                  "It's called crescendo because it only gets better from here..."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        // Determine next activity based on the extra passed with the intent
        String nextActivity = getIntent().getStringExtra(NEXT_ACTIVITY_KEY);

        rand = new Random();
        transitionText = findViewById(R.id.transitionText);
        int index = rand.nextInt(quips.length);
        String quip = quips[index];


        transitionText.setText(quip);
        animateViewDownwards(transitionText);


        new Handler().postDelayed(() -> {
            Intent intent;
            switch (nextActivity) {
                case "song":
                    intent = new Intent(TransitionActivity.this, SongActivity.class);
                    break;
                case "artist":
                    intent = new Intent(TransitionActivity.this, LLMActivity.class);
                    break;
                case "genre":
                    intent = new Intent(TransitionActivity.this, GenresActivity.class);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + nextActivity);
            }
            startActivity(intent);
            finish(); // Finish TransitionActivity
        }, 2000); // Delay for 3 seconds
    }
    private void animateViewDownwards(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0f, 1200f);
        animator.setDuration(1900); // Set the duration of the animation in milliseconds (1 second in this case)
        animator.start();
    }
}