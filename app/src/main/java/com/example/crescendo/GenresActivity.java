package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class GenresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres); // Use the actual layout file name

        setupGenreDetails();

        // Set the entire view as clickable to navigate back to the HomeActivity
        findViewById(R.id.backgroundImage).setOnClickListener(view -> goToHomeActivity());
    }

    private void setupGenreDetails() {
        // Set the genre names
        ((TextView) findViewById(R.id.textView1)).setText("Genre 1");
        ((TextView) findViewById(R.id.textView2)).setText("Genre 2");
        ((TextView) findViewById(R.id.textView3)).setText("Genre 3");
        ((TextView) findViewById(R.id.textView4)).setText("Genre 4");
        ((TextView) findViewById(R.id.textView5)).setText("Genre 5");
    }

    private void goToHomeActivity() {
        Intent intent = new Intent(GenresActivity.this, Taste.class);
        startActivity(intent);
        finish(); // Finish TopGenresActivity
    }
}