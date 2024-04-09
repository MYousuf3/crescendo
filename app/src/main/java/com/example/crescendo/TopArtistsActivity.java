package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class TopArtistsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_artists); // Ensure you have a layout named activity_top_artists

        setupArtistDetails();

        // Set the entire view as clickable to navigate to the next activity
        findViewById(R.id.topArtistsLayout).setOnClickListener(view -> goToTransitionActivity());
    }

    private void setupArtistDetails() {
        // Set the artist names and images
        ((TextView) findViewById(R.id.artistTitle1)).setText("Artist 1");
        ((ImageView) findViewById(R.id.artistThumbnail1)).setImageResource(R.drawable.ic_launcher_background);

        ((TextView) findViewById(R.id.artistTitle2)).setText("Artist 2");
        ((ImageView) findViewById(R.id.artistThumbnail2)).setImageResource(R.drawable.ic_launcher_background);

        ((TextView) findViewById(R.id.artistTitle3)).setText("Artist 3");
        ((ImageView) findViewById(R.id.artistThumbnail3)).setImageResource(R.drawable.ic_launcher_background);

        ((TextView) findViewById(R.id.artistTitle4)).setText("Artist 4");
        ((ImageView) findViewById(R.id.artistThumbnail4)).setImageResource(R.drawable.ic_launcher_background);

        ((TextView) findViewById(R.id.artistTitle5)).setText("Artist 5");
        ((ImageView) findViewById(R.id.artistThumbnail5)).setImageResource(R.drawable.ic_launcher_background);
    }

    private void goToTransitionActivity() {
        Intent intent = new Intent(TopArtistsActivity.this, TransitionActivity.class);
        intent.putExtra(TransitionActivity.NEXT_ACTIVITY_KEY, "genre");
        startActivity(intent);
        finish(); // Finish TopArtistsActivity
    }
}
