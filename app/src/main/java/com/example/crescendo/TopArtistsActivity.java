package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TopArtistsActivity extends AppCompatActivity {

    private List<Artist> topArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_artists);

        // Assuming topArtists is populated from somewhere, similar to topSongs
        topArtists = HomeActivity.topArtists;

        setupArtistDetails();

        findViewById(R.id.topArtistsLayout).setOnClickListener(view -> goToTransitionActivity());
    }

    private void setupArtistDetails() {
        if (topArtists == null || topArtists.size() < 5) {
            // Handle case where there are not enough artists
            return;
        }

        // Loop through the first five artists and set their details
        for (int i = 0; i < 5; i++) {
            Artist artist = topArtists.get(i);
            String artistTitleId = "artistTitle" + (i + 1);
            String artistThumbnailId = "artistThumbnail" + (i + 1);

            int titleResId = getResources().getIdentifier(artistTitleId, "id", getPackageName());
            int thumbnailResId = getResources().getIdentifier(artistThumbnailId, "id", getPackageName());

            TextView artistTitleView = findViewById(titleResId);
            ImageView artistThumbnailView = findViewById(thumbnailResId);

            artistTitleView.setText(artist.artistName);
            Picasso.get().load(artist.getImageURL()).into(artistThumbnailView);
        }
    }

    private void goToTransitionActivity() {
        Intent intent = new Intent(TopArtistsActivity.this, TransitionActivity.class);
        intent.putExtra(TransitionActivity.NEXT_ACTIVITY_KEY, "genre");
        startActivity(intent);
        finish(); // Finish TopArtistsActivity
    }
}
