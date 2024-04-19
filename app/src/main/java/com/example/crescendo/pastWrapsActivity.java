package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class pastWrapsActivity extends AppCompatActivity implements pastAdapter.ItemClickListener{

    private FirebaseAuth auth;
    private FirebaseFirestore database;
    String oldWraps;

    JSONArray wrapsArray;

    pastAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_wraps);
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        Button button = findViewById(R.id.returnBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pastWrapsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            database.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.get("id").equals(currentUser.getUid())) {
                                        oldWraps = (String) document.get("wraps");
                                        System.out.println(oldWraps);
                                        ArrayList<String> pastWrap = new ArrayList<>();
                                        try {
                                            wrapsArray = new JSONArray(oldWraps);
                                            for (int i = 0; i < wrapsArray.length(); i++) {
                                                pastWrap.add("Wrap #" + (i + 1));
                                            }
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }

                                        RecyclerView recyclerView = findViewById(R.id.previousWrap);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(pastWrapsActivity.this));
                                        adapter = new pastAdapter(pastWrapsActivity.this, pastWrap);
                                        adapter.setClickListener(pastWrapsActivity.this);
                                        recyclerView.setAdapter(adapter);
                                    }
                                }
                            } else {
                                System.out.println("Error getting documents." + task.getException());
                            }
                        }
                    });
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        try {
            JSONObject wrap = wrapsArray.getJSONObject(position);
            JSONObject artistObj = wrap.getJSONObject("artists");
            JSONObject songObj = wrap.getJSONObject("songs");
            HomeActivity.topArtists = parseArtists(artistObj);
            HomeActivity.topSongs = parseSongs(songObj);


            Intent intent = new Intent(pastWrapsActivity.this, TransitionActivity.class);
            intent.putExtra(TransitionActivity.NEXT_ACTIVITY_KEY, "song");
            startActivity(intent);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Song> parseSongs(JSONObject jsonObject) throws JSONException {
        ArrayList<Song> songs = new ArrayList<>();
        JSONArray items = jsonObject.getJSONArray("items");
        for (int i = 0; i < items.length(); i++) {
            songs.add(new Song(items.getJSONObject(i)));
        }
        return songs;
    }

    private ArrayList<Artist> parseArtists(JSONObject jsonObject) throws JSONException {
        ArrayList<Artist> artists = new ArrayList<>();
        JSONArray items = jsonObject.getJSONArray("items");
        for (int i = 0; i < items.length(); i++) {
            artists.add(new Artist(items.getJSONObject(i)));
        }
        return artists;
    }
}