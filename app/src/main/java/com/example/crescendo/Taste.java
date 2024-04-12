package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class Taste extends AppCompatActivity {

    TextView taste;
    String answer;
    ArrayList<Artist> artists;
    ConstraintLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taste);

        taste = findViewById(R.id.tasteText);
        layout = findViewById(R.id.tastelayout);

        artists = HomeActivity.topArtists;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                answer = LLM.chatGPTTaste(artists);
            }
        });
        thread.start();



        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeText();
            }
        }, 2000);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Taste.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
        public void changeText(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                taste.setText(answer);
            }
        });
    }
}