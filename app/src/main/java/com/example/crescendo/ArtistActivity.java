package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ArtistActivity extends AppCompatActivity {

    private int artistNumber = 5; // Starting with artist number 5
    private TextView artistNameView;
    private ImageView artistImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        // Initialize the TextView and ImageView
        artistNameView = findViewById(R.id.artistName);
        artistImageView = findViewById(R.id.artistImage);

        findViewById(R.id.artistView).setOnClickListener(view -> {
            artistNumber--;
            if (artistNumber > 0) {
                // Refresh the view or create a new activity for the next artist
                displayArtist(artistNumber);
            } else {
                Intent intent = new Intent(ArtistActivity.this, TopArtistsActivity.class);
                startActivity(intent);
                finish(); // Finish ArtistActivity
            }
        });

        // Display the initial artist details
        displayArtist(artistNumber);
    }

    private void displayArtist(int artistNumber) {
        // Example data to simulate an artist
        String artistName = "Test Artist " + artistNumber;

        // Update the UI with the test artist details
        artistNameView.setText(artistName);

        // Set a test image for the artist
        artistImageView.setImageResource(R.drawable.ic_launcher_background); // will use actual data once we test navigation
    }
}
