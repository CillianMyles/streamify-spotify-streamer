package com.example.android.streamify.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.android.streamify.R;
import com.example.android.streamify.StreamifyApplication;
import com.example.android.streamify.utilities.TracksAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TracksActivity extends AppCompatActivity {

    private static final String TAG = TracksActivity.class.getSimpleName();

    private String artistId;

    private ListView topTracks;

    private TracksAdapter mTracksAdapter;

    private SpotifyService spotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

        getIntentExtras();
        initialiseUI();
        setUpSpotify();
        poulateTracksList();
    }

    private void getIntentExtras() {
        Intent intent = TracksActivity.this.getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            artistId = intent.getExtras().getString(Intent.EXTRA_TEXT);
        }
    }

    private void initialiseUI() {
        topTracks = (ListView) findViewById(R.id.tracks_list);
        mTracksAdapter = new TracksAdapter(TracksActivity.this, new ArrayList<Track>());
        topTracks.setAdapter(mTracksAdapter);
    }

    private void setUpSpotify() {
        spotify = StreamifyApplication.getSpotifyService();
    }

    private void poulateTracksList() {
        TracksTask tracks = new TracksTask();
        tracks.execute(artistId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Represents an asynchronous search task used to find artists.
     */
    public class TracksTask extends AsyncTask<String, Void, ArrayList<Track>> {

        private final String TAG = TracksTask.class.getSimpleName();

        private ArrayList<Track> list;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<>();
        }

        @Override
        protected ArrayList<Track> doInBackground(String... params) {

            if (!params[0].equals("")) {

                Map<String, Object> queryMap = new HashMap<>();
                queryMap.put("country", "IE");

                spotify.getArtistTopTrack(params[0], queryMap, new Callback<Tracks>() {
                    @Override
                    public void success(Tracks tracks, Response response) {
                        Log.d(TAG, "Response : " + response.getBody());
                        for (int i = 0; i < tracks.tracks.size(); i++) {
                            list.add(tracks.tracks.get(i));
                            Log.v(TAG, "TRACK:" + tracks.tracks.get(i).name);
                        }
                        mTracksAdapter.clear();
                        mTracksAdapter.addAll(list);
                        Log.v(TAG, "Search top tracks request successful");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "Search top tracks request failed", error);
                    }
                });

                return list;

            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final ArrayList<Track> tracks) {
            if (tracks == null) {
                mTracksAdapter.clear();
                mTracksAdapter.addAll(new ArrayList<Track>());
                Log.v(TAG, "Searched for nothing, no artists in response.");
            }
        }
    }
}
