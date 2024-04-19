package com.example.crescendo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ArtistActivity extends AppCompatActivity {

    private int artistNumber = 4; // Starting with the index of the 5th artist
    private TextView artistNameView;
    private TextView artistTaglineView;
    private ImageView artistImageView;

    private ImageView popularityImageView;
    private List<Artist> topArtists;
    private FrequencyTable<String, Integer> freqTable;
    private String topGenre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        artistNameView = findViewById(R.id.artistName);
        artistTaglineView = findViewById(R.id.artistTagline);
        artistImageView = findViewById(R.id.artistImage);
        popularityImageView = findViewById(R.id.popularityBar);

        // Assuming topArtists is populated from HomeActivity
        topArtists = HomeActivity.topArtists;

        findViewById(R.id.artistView).setOnClickListener(view -> {
            artistNumber--;
            if (artistNumber >= 0) {
                displayArtist(artistNumber);
            } else {
                Intent intent = new Intent(ArtistActivity.this, TopArtistsActivity.class);
                startActivity(intent);
                finish(); // Finish ArtistActivity
            }
        });

        if (!topArtists.isEmpty()) {
            displayArtist(artistNumber);
        }
    }

    private void displayArtist(int artistIndex) {
        if (artistIndex < 0 || artistIndex >= topArtists.size()) {
            return; // Index out of bounds, return early
        }

        Artist artist = topArtists.get(artistIndex);
        String artistName = artist.artistName;
        String tagline = artist.getTagline();
        String imageUrl = artist.getImageURL();

        artistNameView.setText(artistName);
        artistTaglineView.setText(tagline);

        Picasso.get()
                .load(imageUrl)
                .into(artistImageView);
        setPopularityImage(artist.popularity, popularityImageView);
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
