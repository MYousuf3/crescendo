package com.example.crescendo;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;


@IgnoreExtraProperties
public class Wrap {
    ArrayList<Song> songs;
    ArrayList<Artist> artists;


    public Wrap(ArrayList<Song> songs, ArrayList<Artist> artists) {
        this.songs = songs;
        this.artists = artists;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }
}
