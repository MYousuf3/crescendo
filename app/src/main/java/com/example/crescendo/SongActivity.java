package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SongActivity extends AppCompatActivity {

    private int songNumber = 5; // Starting with song number 5
    private TextView songNameView;
    private TextView songArtistView;
    private ImageView songImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        // Initialize the TextViews and ImageView
        songNameView = findViewById(R.id.songName);
        songArtistView = findViewById(R.id.songArtist);
        songImageView = findViewById(R.id.songImage);

        findViewById(R.id.songView).setOnClickListener(view -> {
            songNumber--;
            if (songNumber > 0) {
                // Refresh the view or create a new activity for the next song
                displaySong(songNumber);
            } else {
                Intent intent = new Intent(SongActivity.this, TopSongsActivity.class);
                startActivity(intent);
                finish(); // Finish SongActivity
            }
        });

        // Display the initial song details
        displaySong(songNumber);
    }

    private void displaySong(int songNumber) {
        // Example data to simulate a song
        String songName = "Test Song " + songNumber;
        String artists = "Test Artist ";

        // Update the UI with the test song details
        songNameView.setText(songName);
        songArtistView.setText(artists);

        // Set a test image for the album
        songImageView.setImageResource(R.drawable.ic_launcher_background); //will use spotify api once we test navigation
    }
}
