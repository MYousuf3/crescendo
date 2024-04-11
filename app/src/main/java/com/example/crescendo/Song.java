package com.example.crescendo;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Song {
    String songName;
    String albumName;
    ArrayList<String> artists;
    int lengthMS;
    int popularity;
    String imageURL;

    public Song() {
        this.songName = "hi";
        this.albumName = "albumName";
        this.artists = new ArrayList<String>();
        artists.add("test artist");
        this.lengthMS = 1;
        this.popularity = 2;
        this.imageURL = "image";
    }

    public Song (JSONObject jsonObject) throws JSONException {
        artists = new ArrayList<>();
        JSONObject album = jsonObject.getJSONObject("album");
        albumName = album.getString("name");
        JSONArray images = album.getJSONArray("images");
        JSONObject imageOne = (JSONObject) images.get(0);
        imageURL = imageOne.getString("url");
        JSONArray artistList = jsonObject.getJSONArray("artists");
        for (int i = 0; i < artistList.length(); i++) {
            JSONObject artist = (JSONObject) artistList.get(i);
            artists.add(artist.getString("name"));
        }
        songName = jsonObject.getString("name");
        popularity = jsonObject.getInt("popularity");
        lengthMS = jsonObject.getInt("duration_ms");
    }

    @NonNull
    public String toString() {
        return songName + " by " + artists.get(0);
    }

}
