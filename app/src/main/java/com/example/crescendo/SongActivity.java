package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SongActivity extends AppCompatActivity {

    private int songNumber = 4; // Starting with the index of the 5th song
    private TextView songNameView;
    private TextView songArtistView;
    private TextView songTaglineView;
    private ImageView songImageView;
    private ImageView popularityImageView;
    private List<Song> topSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        // Initialize the TextViews and ImageView
        songNameView = findViewById(R.id.songName);
        songArtistView = findViewById(R.id.songArtist);
        songImageView = findViewById(R.id.songImage);
        //songTaglineView = findViewById(R.id.songTagline);
        popularityImageView = findViewById(R.id.popularityBarSong);

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
        String artists = "By: " + song.artists.get(0); // Assuming there is at least one artist
        String tagline = song.getTagline();
        String imageUrl = song.getImageURL();

        // Update the UI with the song details
        songNameView.setText(songName);
        songArtistView.setText(artists);
        //songTaglineView.setText(tagline);
        // In future, you can load the image from the song's imageURL
        Picasso.get()
                .load(imageUrl)
                .into(songImageView);
        setPopularityImage(song.popularity, popularityImageView);
    }
    private void setPopularityImage(int popularity, ImageView imageView) {
        if (popularity >= 80) {
            imageView.setImageResource(R.drawable.eighty_percent);
        } else if (popularity >= 60) {
            imageView.setImageResource(R.drawable.sixty_percent);
        } else {
            imageView.setImageResource(R.drawable.forty_percent);
        }
    }
}
