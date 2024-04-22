package com.example.crescendo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView artistNameView;
    private ImageView artistImageView;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
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
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        submitButton = findViewById(R.id.submitBtn);

        artists = HomeActivity.topArtists;  // Assuming this list is populated correctly

        if (!artists.isEmpty()) {
            setupGame();
        }

        setupCheckBoxes();

        submitButton.setOnClickListener(v -> checkAnswer());
    }

    private void setupGame() {
        Random random = new Random();
        // Ensure the correct answer is always included
        int correctIndex = random.nextInt(4);  // There are four options
        currentArtist = artists.get(random.nextInt(artists.size())); // Random artist for the game
        Picasso.get().load(currentArtist.getImageURL()).into(artistImageView);
        artistNameView.setVisibility(View.INVISIBLE); // Initially hide the artist name

        CheckBox[] checkBoxes = {checkBox1, checkBox2, checkBox3, checkBox4};
        TextView[] textViews = {answer1, answer2, answer3, answer4};

        // Fill array with names ensuring no duplicates and correct answer included
        String[] options = new String[4];
        options[correctIndex] = currentArtist.artistName; // Place correct answer

        for (int i = 0; i < options.length; i++) {
            if (i != correctIndex) { // Fill other options
                Artist randomArtist;
                do {
                    randomArtist = artists.get(random.nextInt(artists.size()));
                } while (randomArtist.artistName.equals(currentArtist.artistName) || contains(options, randomArtist.artistName));
                options[i] = randomArtist.artistName;
            }
        }

        // Set the text for each TextView associated with each CheckBox
        for (int i = 0; i < checkBoxes.length; i++) {
            textViews[i].setText(options[i]);
        }
    }

    // Helper method to check if the array already contains the artist name
    private boolean contains(String[] array, String value) {
        for (String element : array) {
            if (element != null && element.equals(value)) {
                return true;
            }
        }
        return false;
    }


    private void setupCheckBoxes() {
        CheckBox[] checkBoxes = {checkBox1, checkBox2, checkBox3, checkBox4};
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    for (CheckBox otherCheckBox : checkBoxes) {
                        if (otherCheckBox != checkBox) otherCheckBox.setChecked(false);
                    }
                }
            });
        }
    }

    private void checkAnswer() {
        String selectedArtistName = "";
        CheckBox[] checkBoxes = {checkBox1, checkBox2, checkBox3, checkBox4};
        TextView[] textViews = {answer1, answer2, answer3, answer4};
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) {
                selectedArtistName = textViews[i].getText().toString();
                break;
            }
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
