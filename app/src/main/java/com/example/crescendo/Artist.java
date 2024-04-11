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

    public Artist() {
        this.artistName = "name";
        this.popularity = 1;
        this.genres = new ArrayList<String>();
        genres.add("genre");
        this.imageURL = "image";
        this.followers = 2;
    }

    public Artist (JSONObject jsonObject) throws JSONException {
        genres = new ArrayList<>();
        artistName = jsonObject.getString("name");
        JSONArray images = jsonObject.getJSONArray("images");
        JSONObject imageOne = (JSONObject) images.get(0);
        imageURL = imageOne.getString("url");
        JSONArray genreList = jsonObject.getJSONArray("genres");
        for (int i = 0; i < genreList.length(); i++) {
            genres.add((String) genreList.get(i));
        }
        popularity = jsonObject.getInt("popularity");
        followers = jsonObject.getJSONObject("followers").getInt("total");
    }

    @NonNull
    public String toString() {
        return artistName;
    }

    public String getImageURL() {
        return imageURL;
    }

}
