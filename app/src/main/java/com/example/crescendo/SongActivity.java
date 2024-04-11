package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SongActivity extends AppCompatActivity {

    private int songNumber = 4; // Starting with the index of the 5th song
    private TextView songNameView;
    private TextView songArtistView;
    private ImageView songImageView;
    private List<Song> topSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        // Initialize the TextViews and ImageView
        songNameView = findViewById(R.id.songName);
        songArtistView = findViewById(R.id.songArtist);
        songImageView = findViewById(R.id.songImage);

        // Get the top songs list from HomeActivity
        topSongs = HomeActivity.topSongs;

        findViewById(R.id.songView).setOnClickListener(view -> {
            songNumber--;
            if (songNumber >= 0) {
                // Refresh the view with the next song
                displaySong(songNumber);
            } else {
                Intent intent = new Intent(SongActivity.this, TopSongsActivity.class);
                startActivity(intent);
                finish(); // Finish SongActivity
            }
        });

        // Display the initial song details if the list is not empty
        if (!topSongs.isEmpty()) {
            displaySong(songNumber);
        }
    }

    private void displaySong(int songNumber) {
        Song song = topSongs.get(songNumber);
        String songName = song.songName;
        String artists = song.artists.get(0); // Assuming there is at least one artist

        // Update the UI with the song details
        songNameView.setText(songName);
        songArtistView.setText(artists);

        // For now, using a placeholder image for the album
        // In future, you can load the image from the song's imageURL
        songImageView.setImageResource(R.drawable.ic_launcher_background);
    }
}
