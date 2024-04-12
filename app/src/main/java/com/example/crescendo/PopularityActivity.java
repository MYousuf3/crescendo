package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PopularityActivity extends AppCompatActivity {
    private int popTotal;
    private int popScore;
    private String popScoreMessage;
    private ArrayList<Song> topSongs;
    RelativeLayout layout;
    private String quip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popularity);
        layout = findViewById(R.id.poplayout);

        topSongs = HomeActivity.topSongs;
        int ct = 0;
        for (Song s : topSongs) {
            popTotal += s.popularity;
            ct++;
        }

        popScore = popTotal / ct;
        popScoreMessage = "Your Popularity Score Is: \n" + popScore;

        if (popScore >= 90) {
            quip = "Please go listen to something other than Taylor Swift or Drake.";
        } else if (popScore >= 70) {
            quip = "You're basic. Go soul search.";
        } else {
            quip = "So underground. You're probably terrible on aux.";
        }

        setUpPopularityDetails();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTransitionActivity();
            }
        });
    }

    private void setUpPopularityDetails() {
        if (topSongs == null || popScore <= 0 || popScore >= 100) {
            return;
        }

        ((TextView) findViewById(R.id.popScoreText)).setText(popScoreMessage);
        ((TextView) findViewById(R.id.quipText)).setText(quip);
    }

    private void goToTransitionActivity() {
        Intent intent = new Intent(PopularityActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}