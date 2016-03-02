package com.example.android.streamify.tracks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.streamify.R;
import com.example.android.streamify.StreamifyApplication;
import com.example.android.streamify.player.PlayerActivity;
import com.example.android.streamify.utilities.Constants;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;

public class TracksActivity extends AppCompatActivity {

    private static final String TAG = TracksActivity.class.getSimpleName();

    private String mArtistId;
    private ListView mTopTracks;
    private TracksAdapter mTracksAdapter;
    private SpotifyService mSpotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

        getIntentExtras();
        initialiseUI();
        setUpSpotify();
        populateTracksList();
    }

    private void getIntentExtras() {
        Intent intent = TracksActivity.this.getIntent();
        if (intent.hasExtra(Constants.ARTIST_ID_TAG)) {
            mArtistId = intent.getExtras().getString(Constants.ARTIST_ID_TAG);
        }
    }

    private void initialiseUI() {
        mTopTracks = (ListView) findViewById(R.id.tracks_list);
        mTracksAdapter = new TracksAdapter(TracksActivity.this, new ArrayList<Track>());
        mTopTracks.setAdapter(mTracksAdapter);
        mTopTracks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = mTracksAdapter.getItem(position);
                Intent intent = new Intent(TracksActivity.this, PlayerActivity.class);
                intent.putExtra(Constants.TRACK_ID_TAG, track.id);
                startActivity(intent);
            }
        });
    }

    private void setUpSpotify() {
        mSpotify = StreamifyApplication.getSpotifyService();
    }

    private void populateTracksList() {
        TracksTask tracks = new TracksTask(mSpotify, mTracksAdapter);
        tracks.execute(mArtistId);
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
}