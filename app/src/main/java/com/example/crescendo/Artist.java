package com.example.crescendo;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Artist {

    String artistName;
    int popularity;
    ArrayList<String> genres;
    String imageURL;
    int followers;

    public Artist(JSONObject jsonObject) throws JSONException {
        genres = new ArrayList<>();
        artistName = jsonObject.getString("name");

        JSONArray images = jsonObject.getJSONArray("images");
        if (images.length() > 0) {
            JSONObject imageOne = images.getJSONObject(0);
            imageURL = imageOne.getString("url");
        } else {
            imageURL = null; // No image available
        }

        JSONArray genreList = jsonObject.getJSONArray("genres");
        for (int i = 0; i < genreList.length(); i++) {
            genres.add(genreList.getString(i));
        }

        popularity = jsonObject.getInt("popularity");
        followers = jsonObject.getJSONObject("followers").getInt("total");
    }

    @NonNull
    @Override
    public String toString() {
        return artistName + (genres.size() > 0 ? ", " + genres.get(0) : "");
    }

    public String getArtistName() {
        return artistName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getTagline() {
        String tagline;
        if (popularity >= 80) {
            tagline = "A Hit Artist!";
        } else if (popularity >= 50) {
            tagline = "Trending Artist!";
        } else {
            tagline = "A Hidden Gem Artist!";
        }
        return tagline;
    }
}
