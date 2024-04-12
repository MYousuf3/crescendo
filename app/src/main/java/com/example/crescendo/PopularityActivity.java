package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class PopularityActivity extends AppCompatActivity {
    private int popTotal;
    private int popScore;
    private String popScoreMessage;
    private ArrayList<Song> topSongs;
    private String quip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popularity);

        topSongs = HomeActivity.topSongs;
        int ct = 0;
        for (Song s : topSongs) {
            popTotal += s.popularity;
            ct++;
        }

        popScore = popTotal / ct;
        popScoreMessage = "Your popularity score is " + popScore;

        if (popScore >= 90) {
            quip = "Please go listen to something other than Taylor Swift or Drake.";
        } else if (popScore >= 70) {
            quip = "You're basic. Go soul search.";
        } else {
            quip = "So underground. You're probably terrible on aux.";
        }

        setUpPopularityDetails();
    }

    private void setUpPopularityDetails() {
        if (topSongs == null || popScore <= 0 || popScore >= 100) {
            return;
        }

        ((TextView) findViewById(R.id.textView1)).setText(popScoreMessage);
        ((TextView) findViewById(R.id.textView2)).setText(quip);
    }

    private void goToTransitionActivity() {
        Intent intent = new Intent(PopularityActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}