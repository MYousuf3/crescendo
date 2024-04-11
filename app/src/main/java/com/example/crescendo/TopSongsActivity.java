package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TopSongsActivity extends AppCompatActivity {

    private List<Song> topSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_songs);

        // Assume topSongs is populated from somewhere, like HomeActivity
        topSongs = HomeActivity.topSongs;

        setupSongDetails();

        findViewById(R.id.topSongsLayout).setOnClickListener(view -> goToTransitionActivity());
    }

    private void setupSongDetails() {
        if (topSongs == null || topSongs.size() < 5) {
            // Handle case where there are not enough songs
            return;
        }

        // Loop through the first five songs and set their details
        for (int i = 0; i < 5; i++) {
            Song song = topSongs.get(i);
            String songTitleId = "songTitle" + (i + 1);
            String songThumbnailId = "songThumbnail" + (i + 1);

            int titleResId = getResources().getIdentifier(songTitleId, "id", getPackageName());
            int thumbnailResId = getResources().getIdentifier(songThumbnailId, "id", getPackageName());

            TextView songTitleView = findViewById(titleResId);
            ImageView songThumbnailView = findViewById(thumbnailResId);

            songTitleView.setText(song.songName);
            Picasso.get().load(song.getImageURL()).into(songThumbnailView);
        }
    }

    private void goToTransitionActivity() {
        Intent intent = new Intent(TopSongsActivity.this, TransitionActivity.class);
        intent.putExtra(TransitionActivity.NEXT_ACTIVITY_KEY, "artist");
        startActivity(intent);
        finish(); // Finish TopSongsActivity
    }
}
