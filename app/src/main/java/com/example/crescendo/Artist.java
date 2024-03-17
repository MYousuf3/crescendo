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
        if (genres.size() > 0)
            return artistName + ", " + genres.get(0);
        return artistName;
    }

}
