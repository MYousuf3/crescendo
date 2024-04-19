package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity implements pastAdapter.ItemClickListener{

    private static final String CLIENT_ID = "1afb806ef1504ef4b8b84d4329a66932";
    private static final String REDIRECT_URI = "crescendo://auth";
    private static final int AUTH_TOKEN_REQUEST_CODE = 0;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken;
    private Call mCall;
    Spinner spinner;

    private TextView title;
    JSONObject songJSON;
    JSONObject artistJSON;
    JSONArray newJSON;
    JSONArray wrapsArray;
    String oldWraps;
    public static ArrayList<Song> topSongs;
    public static ArrayList<Artist> topArtists;

    ImageView settings;
    ImageView cd;
    String term;

    private FirebaseAuth auth;
    private FirebaseFirestore database;

    pastAdapter adapter;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        settings = findViewById(R.id.settingsIcon);

        title = findViewById(R.id.title);
        spinner = findViewById(R.id.spinner);
        cd = findViewById(R.id.playButton);
        term = "long_term";

        Button pastWrapBtn = findViewById(R.id.pastWrapBtn);

        Animation rotation = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.rotate);
        rotation.setFillAfter(true);
        cd.startAnimation(rotation);

        pastWrapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, pastWrapsActivity.class);
                startActivity(intent);
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    term = "long_term";
                    System.out.println(term);
                } else if (position == 1) {
                    term = "medium_term";
                    System.out.println(term);
                } else {
                    term = "short_term";
                    System.out.println(term);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            title.setText("Welcome " + currentUser.getDisplayName());
        }
        findViewById(R.id.playButton).setOnClickListener(view -> authenticateSpotify());

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void authenticateSpotify() {
        AuthorizationRequest request = new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(new String[]{"user-read-email", "user-top-read"})
                .build();
        AuthorizationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTH_TOKEN_REQUEST_CODE) {
            final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);
            mAccessToken = response.getAccessToken();

            if (mAccessToken != null) {
                getTopTracks();
                getTopArtists();
            } else {
                Log.d("SpotifyAuth", "Error: Could not get access token");
            }
        }
    }

    public void updateDoc(DocumentReference docRef){
        docRef.update("wraps", newJSON.toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                        System.out.println(task.isSuccessful());
                    }
                });
    }

    private void getTopTracks() {
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/tracks?time_range="+ term + "&limit=10&offset=0")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("SpotifyTopTracks", "Error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    songJSON = jsonObject;
                    topSongs = parseSongs(jsonObject);
                    checkDataAndProceed();
                } catch (Exception e) {
                    Log.e("SpotifyTopTracks", "Error parsing top tracks: " + e.getMessage());
                }
            }
        });
    }

    private void getTopArtists() {
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/artists?time_range=" + term + "&limit=10&offset=0")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("SpotifyTopArtists", "Error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());

                    artistJSON = jsonObject;
                    topArtists = parseArtists(jsonObject);
                    Log.d("SpotifyTopArtists", "Top artists: " + topArtists.toString());
                    checkDataAndProceed();
                } catch (Exception e) {
                    Log.e("SpotifyTopArtists", "Error parsing top artists: " + e.getMessage());
                }
            }
        });



    }

    private void checkDataAndProceed() {
        if (topSongs != null && topArtists != null) {
            JSONObject wrap = new JSONObject();
            try {
                wrap.put("artists", artistJSON);
                wrap.put("songs", songJSON);

            } catch (JSONException e) {
                System.out.println(e.getMessage());
            }
            FirebaseUser newUser = auth.getCurrentUser();

            database.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.get("id").equals(newUser.getUid())) {
                                        String strJson = (String) document.get("wraps");
                                        System.out.println(strJson);
                                        try {
                                            newJSON = new JSONArray(strJson);
                                            newJSON.put(wrap);
                                            System.out.println(newJSON.toString());
                                            updateDoc(document.getReference());
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                            } else {
                                System.out.println("Error getting documents." + task.getException());
                            }
                        }
                    });
            Intent intent = new Intent(HomeActivity.this, TransitionActivity.class);
            intent.putExtra(TransitionActivity.NEXT_ACTIVITY_KEY, "song");
            startActivity(intent);
            finish();
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


    @Override
    public void onItemClick(View view, int position) {
        try {
            JSONObject wrap = wrapsArray.getJSONObject(position);
            JSONObject artistObj = wrap.getJSONObject("artists");
            JSONObject songObj = wrap.getJSONObject("songs");
            topArtists = parseArtists(artistObj);
            topSongs = parseSongs(songObj);


            Intent intent = new Intent(HomeActivity.this, TransitionActivity.class);
            intent.putExtra(TransitionActivity.NEXT_ACTIVITY_KEY, "song");
            startActivity(intent);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
}
