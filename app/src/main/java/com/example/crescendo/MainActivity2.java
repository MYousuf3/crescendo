package com.example.crescendo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

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


public class MainActivity2 extends AppCompatActivity {

    public static final String CLIENT_ID = "1afb806ef1504ef4b8b84d4329a66932";
    public static final String REDIRECT_URI = "crescendo://auth";

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken, mAccessCode;
    private Call mCall;

    private TextView tokenTextView, codeTextView, profileTextView, itemTokenText, itemCodeText, trackText, displayName;
    private ArrayList<Song> songs;
    private ArrayList<Artist> artists;

    FirebaseAuth auth;
    FirebaseDatabase database;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Initialize the views
        tokenTextView = (TextView) findViewById(R.id.token_text_view);
        codeTextView = (TextView) findViewById(R.id.code_text_view);
        profileTextView = (TextView) findViewById(R.id.response_text_view);
        itemTokenText = (TextView) findViewById(R.id.itemToken);
        itemCodeText = (TextView) findViewById(R.id.itemCode);
        trackText = (TextView) findViewById(R.id.trackText);
        displayName = findViewById(R.id.displayName);
        songs = new ArrayList<>();
        artists = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            displayName.setText(currentUser.getDisplayName());
        }

        // Initialize the buttons
        Button tokenBtn = (Button) findViewById(R.id.token_btn);
        Button codeBtn = (Button) findViewById(R.id.code_btn);
        Button profileBtn = (Button) findViewById(R.id.profile_btn);

        Button itemTokenBtn = (Button) findViewById(R.id.itemTokenButton);
        Button itemCodeBtn = (Button) findViewById(R.id.itemCodeButton);
        Button getItemBtn = (Button) findViewById(R.id.getItemButton);
        Button getArtistsBtn = (Button) findViewById(R.id.getArtists);
        Button signOut = findViewById(R.id.signOut);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(CLIENT_ID)
                        .requestEmail().build();

                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(MainActivity2.this, gso);

                mGoogleSignInClient.signOut();
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

        // Set the click listeners for the buttons

        tokenBtn.setOnClickListener((v) -> {
            getToken();
        });

        codeBtn.setOnClickListener((v) -> {
            getCode();
        });

        profileBtn.setOnClickListener((v) -> {
            onGetUserProfileClicked();
        });

        itemTokenBtn.setOnClickListener((v) -> {
            getTokenItems();
        });

        itemCodeBtn.setOnClickListener((v) -> {
            getCodeItems();
        });

        getItemBtn.setOnClickListener((v) -> {
            onGetTopTracks();
        });

        getArtistsBtn.setOnClickListener((v) -> {
            onGetTopArtists();
        });

    }

    /**
     * Get token from Spotify
     * This method will open the Spotify login activity and get the token
     * What is token?
     * https://developer.spotify.com/documentation/general/guides/authorization-guide/
     */
    public void getToken() {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(null);
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN, 0);
        AuthorizationClient.openLoginActivity(MainActivity2.this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    public void getTokenItems() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN, 1);
        AuthorizationClient.openLoginActivity(MainActivity2.this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    /**
     * Get code from Spotify
     * This method will open the Spotify login activity and get the code
     * What is code?
     * https://developer.spotify.com/documentation/general/guides/authorization-guide/
     */
    public void getCode() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE, 0);
        AuthorizationClient.openLoginActivity(MainActivity2.this, AUTH_CODE_REQUEST_CODE, request);
    }
    public void getCodeItems() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE, 1);
        AuthorizationClient.openLoginActivity(MainActivity2.this, AUTH_CODE_REQUEST_CODE, request);
    }


    /**
     * When the app leaves this activity to momentarily get a token/code, this function
     * fetches the result of that external activity to get the response from Spotify
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

        // Check which request code is present (if any)
        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            mAccessToken = response.getAccessToken();
            setTextAsync(mAccessToken, tokenTextView);

        } else if (AUTH_CODE_REQUEST_CODE == requestCode) {
            mAccessCode = response.getCode();
            setTextAsync(mAccessCode, codeTextView);
        }
    }

    /**
     * Get user profile
     * This method will get the user profile using the token
     */
    public void onGetUserProfileClicked() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a request to get the user profile
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                Toast.makeText(MainActivity2.this, "Failed to fetch data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    //setTextAsync(jsonObject.toString(3), profileTextView);

                    String nameString = jsonObject.getString("display_name");
                    setTextAsync(nameString, displayName);
                    /*
                    JSONArray images = jsonObject.getJSONArray("images");
                    JSONObject imagesObj = images.getJSONObject(0);


                    String imageURL = imagesObj.getString("url");
                    URL url = new URL(imageURL);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            avatar.setImageBitmap(bmp);
                            displayName.setText(nameString);
                        }
                    });
                    */
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(MainActivity2.this, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onGetTopTracks() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a request to get the user profile
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/tracks?time_range=long_term&limit=10&offset=0")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                Toast.makeText(MainActivity2.this, "Failed to fetch data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    //setTextAsync(jsonObject.toString(3), profileTextView);
                    StringBuilder songText = new StringBuilder();
                    JSONArray items = jsonObject.getJSONArray("items");
                    for (int i = 0; i < items.length(); i++) {
                        songs.add(new Song((JSONObject) items.get(i)));
                        songText.append((i + 1)).append(". ").append(songs.get(i).toString()).append("\n");
                    }
                    setTextAsync(String.valueOf(songText), trackText);

                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(MainActivity2.this, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onGetTopArtists() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a request to get the user profile
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/artists?time_range=long_term&limit=10&offset=0")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                Toast.makeText(MainActivity2.this, "Failed to fetch data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    StringBuilder artistText = new StringBuilder();
                    JSONArray items = jsonObject.getJSONArray("items");
                    for (int i = 0; i < items.length(); i++) {
                        artists.add(new Artist((JSONObject) items.get(i)));
                        artistText.append((i + 1)).append(". ").append(artists.get(i).toString()).append("\n");
                    }
                    setTextAsync(String.valueOf(artistText), codeTextView);
                    /*
                    String nameString = jsonObject.getString("display_name");

                    JSONArray images = jsonObject.getJSONArray("images");
                    JSONObject imagesObj = images.getJSONObject(0);
                    String imageURL = imagesObj.getString("url");
                    URL url = new URL(imageURL);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            avatar.setImageBitmap(bmp);
                            displayName.setText(nameString);
                        }
                    });
                    */
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(MainActivity2.this, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Creates a UI thread to update a TextView in the background
     * Reduces UI latency and makes the system perform more consistently
     *
     * @param text the text to set
     * @param textView TextView object to update
     */
    private void setTextAsync(final String text, TextView textView) {
        runOnUiThread(() -> textView.setText(text));
    }

    /**
     * Get authentication request
     *
     * @param type the type of the request
     * @return the authentication request
     */
    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type, int scope) {
        if (scope == 0) {
            return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                    .setShowDialog(false)
                    .setScopes(new String[]{"user-read-email"}) // <--- Change the scope of your requested token here
                    .setCampaign("your-campaign-token")
                    .build();
        }
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[]{"user-top-read"}) // <--- Change the scope of your requested token here
                .setCampaign("your-campaign-token")
                .build();
    }

    /**
     * Gets the redirect Uri for Spotify
     *
     * @return redirect Uri object
     */
    private Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        cancelCall();
        super.onDestroy();
    }
}