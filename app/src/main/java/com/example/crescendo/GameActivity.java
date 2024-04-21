package com.example.crescendo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    private TextView displayArtistNameTextView;
    private Button showInputDialogButton;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ImageView avatarImageView = findViewById(R.id.avatarImageView);
        displayArtistNameTextView=findViewById(R.id.displayArtistNameTextView);
        showInputDialogButton=findViewById(R.id.showInputDialogButton);

        //Glide.with(getApplicationContext()).load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkliO9xHJ0NrIYKvWrTl5Hfsfs0JRkSnsPFEQIKnNvNA&s").placeholder(R.drawable.baseline_image_24).into(avatarImageView);
        ArrayList<String> myList=new ArrayList<>();
        myList.add("Bruno Mars");


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


}

