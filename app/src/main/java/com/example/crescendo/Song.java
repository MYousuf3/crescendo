package com.example.crescendo;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;

public class Song {
    String songName;
    String albumName;
    ArrayList<String> artists;
    int lengthMS;
    int popularity;
    String imageURL;

    public Song(JSONObject jsonObject) throws JSONException {
        artists = new ArrayList<>();

        JSONObject album = jsonObject.getJSONObject("album");
        albumName = album.getString("name");

        JSONArray images = album.getJSONArray("images");
        if (images.length() > 0) {
            JSONObject imageOne = images.getJSONObject(0);
            imageURL = imageOne.getString("url");
        } else {
            imageURL = null; // No image available
        }

        JSONArray artistList = jsonObject.getJSONArray("artists");
        for (int i = 0; i < artistList.length(); i++) {
            JSONObject artist = artistList.getJSONObject(i);
            artists.add(artist.getString("name"));
        }

        songName = jsonObject.getString("name");
        popularity = jsonObject.getInt("popularity");
        lengthMS = jsonObject.getInt("duration_ms");
    }

    @NonNull
    @Override
    public String toString() {
        return songName + " by " + (artists.isEmpty() ? "Unknown Artist" : artists.get(0));
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getTagline() {
        String tagline;
        if (popularity >= 80) {
            tagline = "A Hit Sensation!";
        } else if (popularity >= 50) {
            tagline = "Trending!";
        } else {
            tagline = "Discover Hidden Gems!";
        }
        return tagline;
    }
}
