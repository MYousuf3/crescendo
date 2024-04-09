package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.playWrappedButton).setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, TransitionActivity.class);
            intent.putExtra(TransitionActivity.NEXT_ACTIVITY_KEY, "song");
            startActivity(intent);
        });
    }
}