package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ImageViewCompat;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class TransitionActivity extends AppCompatActivity {

    public static final String NEXT_ACTIVITY_KEY = "next_activity";
    Random rand;
    TextView transitionText;
    ArrayList<String> quips;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        // Determine next activity based on the extra passed with the intent
        String nextActivity = getIntent().getStringExtra(NEXT_ACTIVITY_KEY);

        rand = new Random();
        transitionText = findViewById(R.id.transitionText);
        quips = new ArrayList<>();

        quips.add("You're not ready for this");
        quips.add("Hold on to your seats...");
        quips.add("And now for the big reveal...");
        quips.add("Cue the fanfare...");
        quips.add("Hold your applause...");
        quips.add("Get ready for this...");
        quips.add("It's called crescendo because it only gets better from here...");
        quips.add("Interesting choice...");
        quips.add("Care to explain this one?");
        quips.add("*crickets*");
        quips.add("As seen on TV!");
        quips.add("follow @muhammadyii on ig");

        int index = rand.nextInt(quips.size());
        String quip = quips.remove(index);

        ConstraintLayout layout = findViewById(R.id.relativeLayout);

        ArrayList<String> colors = new ArrayList<>();

        colors.add("#32532f");
        colors.add("#770e9b");
        colors.add("#6f0829");
        colors.add("#135616");
        colors.add("#000000");

        int indexColor = rand.nextInt(colors.size());


        transitionText.setText(quip);


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
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0f, 800f);
        animator.setDuration(1500); // Set the duration of the animation in milliseconds (1 second in this case)
        animator.start();
    }
}