package com.example.crescendo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView artistNameView;
    private ImageView artistImageView;
    private RadioGroup answerChoices;
    private RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    private TextView answer1, answer2, answer3, answer4;
    private Button submitButton;
    private List<Artist> artists;
    private Artist currentArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        artistNameView = findViewById(R.id.ArtistName);
        artistImageView = findViewById(R.id.avatarImageView);
        answerChoices = findViewById(R.id.answerChoices);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        submitButton = findViewById(R.id.submitBtn);

        artists = HomeActivity.topArtists;  // Assuming this list is populated correctly

        if (!artists.isEmpty()) {
            setupGame();
        }

        submitButton.setOnClickListener(v -> checkAnswer());
    }

    private void setupGame() {
        Random random = new Random();
        int correctIndex = random.nextInt(4);  // There are four options
        currentArtist = artists.get(random.nextInt(artists.size())); // Random artist for the game
        Picasso.get().load(currentArtist.getImageURL()).into(artistImageView);
        artistNameView.setVisibility(View.INVISIBLE); // Initially hide the artist name

        // Set random names for the options, ensuring the correct answer is one of them
        String[] options = new String[4];
        for (int i = 0; i < 4; i++) {
            if (i == correctIndex) {
                options[i] = currentArtist.artistName; // Correct answer
            } else {
                Artist randomArtist;
                do {
                    randomArtist = artists.get(random.nextInt(artists.size()));
                } while (randomArtist == currentArtist);
                options[i] = randomArtist.artistName; // Random incorrect answers
            }
        }

        answer1.setText(options[0]);
        answer2.setText(options[1]);
        answer3.setText(options[2]);
        answer4.setText(options[3]);
    }

    private void checkAnswer() {
        int selectedId = answerChoices.getCheckedRadioButtonId();
        String selectedArtistName = "";

        if (selectedId == R.id.radioButton1) {
            selectedArtistName = answer1.getText().toString();
        } else if (selectedId == R.id.radioButton2) {
            selectedArtistName = answer2.getText().toString();
        } else if (selectedId == R.id.radioButton3) {
            selectedArtistName = answer3.getText().toString();
        } else if (selectedId == R.id.radioButton4) {
            selectedArtistName = answer4.getText().toString();
        }

        boolean isCorrect = selectedArtistName.equalsIgnoreCase(currentArtist.artistName);
        artistNameView.setText(currentArtist.artistName + (isCorrect ? " - Correct!" : " - Wrong!"));
        artistNameView.setVisibility(View.VISIBLE);

        // Delay for 2 seconds then go back to HomeActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(GameActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }


}
