package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class TopSongsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_songs);

        // Set up song details
        setupSongDetails();

        // Set up a click listener for the whole layout
        findViewById(R.id.topSongsLayout).setOnClickListener(view -> goToTransitionActivity());
    }

    private void setupSongDetails() {
        // Set the song titles and images
        ((TextView) findViewById(R.id.songTitle1)).setText("Song 1");
        ((ImageView) findViewById(R.id.songThumbnail1)).setImageResource(R.drawable.ic_launcher_background);

        ((TextView) findViewById(R.id.songTitle2)).setText("Song 2");
        ((ImageView) findViewById(R.id.songThumbnail2)).setImageResource(R.drawable.ic_launcher_background);

        ((TextView) findViewById(R.id.songTitle3)).setText("Song 3");
        ((ImageView) findViewById(R.id.songThumbnail3)).setImageResource(R.drawable.ic_launcher_background);

        ((TextView) findViewById(R.id.songTitle4)).setText("Song 4");
        ((ImageView) findViewById(R.id.songThumbnail4)).setImageResource(R.drawable.ic_launcher_background);

        ((TextView) findViewById(R.id.songTitle5)).setText("Song 5");
        ((ImageView) findViewById(R.id.songThumbnail5)).setImageResource(R.drawable.ic_launcher_background);
    }

    private void goToTransitionActivity() {
        Intent intent = new Intent(TopSongsActivity.this, TransitionActivity.class);
        intent.putExtra(TransitionActivity.NEXT_ACTIVITY_KEY, "artist");
        startActivity(intent);
        finish(); // Finish TopSongsActivity
    }
}