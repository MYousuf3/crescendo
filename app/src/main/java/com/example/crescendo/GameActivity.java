package com.example.crescendo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.Async;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class GameActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ImageView avatarImageView = findViewById(R.id.avatarImageView);
        TextView displayArtistNameTextView = findViewById(R.id.displayArtistNameTextView);
        Button showInputDialogButton = findViewById(R.id.showInputDialogButton);

        Glide.with(getApplicationContext()).load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkliO9xHJ0NrIYKvWrTl5Hfsfs0JRkSnsPFEQIKnNvNA&s").placeholder(R.drawable.baseline_image_24).into(avatarImageView);
        ArrayList<String> myList = new ArrayList<>();
        myList.add("Bruno Mars");

        displayArtistNameTextView.setText("Bruno Mars");
        showInputDialogButton.setOnClickListener(view -> {
            showInputDialog();
        });
    }

        private void showInputDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter Artist Name");

            final EditText input = new EditText(this);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String userInput = input.getText().toString().toLowerCase();
                    if ("Bruno Mars".toLowerCase().equals(userInput)) {
                        Toast.makeText(GameActivity.this, "YOU GUESSED CORRECT!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GameActivity.this, "WRONG!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
       /* displayArtistNameTextView = findViewById(R.id.displayArtistNameTextView);

        ArrayList<String> myList = new ArrayList<>();
        myList.add("Bruno Mars");

        String imageURL = "https://images.app.goo.gl/pcwGHQuK1Kxk65uw6";



        displayArtistNameTextView.setText(myList.get(0));


        }

        /*
                    JSONArray images = jsonObject.getJSONArray("images");
                    JSONObject imagesObj = images.getJSONObject(0);
                    String imageURL = imagesObj.getString("url");

                    String imageURL = "https://images.app.goo.gl/pcwGHQuK1Kxk65uw6";


                    URL url = new URL(imageURL);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bmp);
                            displayName.setText(nameString);
                        }


        */

    }




