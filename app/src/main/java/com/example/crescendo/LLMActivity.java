package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LLMActivity extends AppCompatActivity {

    TextView simArtist;
    String prompt;
    String answer;
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llm);

        simArtist = findViewById(R.id.similarArtistsText);
        prompt = "";
        layout = findViewById(R.id.llmlayout);


        ArrayList<Artist> artists = HomeActivity.topArtists;
        for (int i = 0; i < 3; i++) {
            prompt+= artists.get(i).getArtistName() + ", ";
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                answer = LLM.chatGPT(prompt);
            }
        });
        thread.start();

        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeText();
            }
        }, 2500);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LLMActivity.this, ArtistActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    public void changeText(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println(answer);
                simArtist.setText(answer);
            }
        });
    }
}