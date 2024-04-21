package com.example.crescendo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView artistNameView;
    private ImageView artistImageView;
    private TextInputEditText guessInput;
    private Button submitButton;
    private List<Artist> artists;
    private Artist currentArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        artistNameView = findViewById(R.id.ArtistName);
        artistImageView = findViewById(R.id.avatarImageView);
        guessInput = findViewById(R.id.answer);
        submitButton = findViewById(R.id.submitBtn);

        // Assume this is populated and passed correctly; otherwise, fetch it as needed
        artists = HomeActivity.topArtists;

        if (!artists.isEmpty()) {
            loadRandomArtist();
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkArtistGuess();
            }
        });
    }

    private void loadRandomArtist() {
        Random random = new Random();
        // Ensure we get an index starting from 5 (6th position) to the size of the list
        if (artists.size() > 5) {
            int randomIndex = 5 + random.nextInt(artists.size() - 5);
            currentArtist = artists.get(randomIndex);
            Picasso.get().load(currentArtist.getImageURL()).into(artistImageView);
            artistNameView.setVisibility(View.INVISIBLE); // Hide the artist name initially
        }
    }

    private void checkArtistGuess() {
        String guess = guessInput.getText().toString().trim();
        boolean isCorrect = guess.equalsIgnoreCase(currentArtist.artistName);
        artistNameView.setText(currentArtist.artistName); // Reveal the artist name
        artistNameView.setVisibility(View.VISIBLE);
        artistNameView.append(isCorrect ? " - Correct!" : " - Nice try!");

        // Delay for 2 seconds then go back to HomeActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(GameActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}
