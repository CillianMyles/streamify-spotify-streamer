package com.example.android.streamify.player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.android.streamify.R;
import com.example.android.streamify.utilities.Constants;

public class PlayerActivity extends AppCompatActivity {

    private static final String TAG = PlayerActivity.class.getSimpleName();

    private String mTrackId;

    private TextView mArtistName;
    private TextView mAlbumName;
    private ImageView mAlbumCover;
    private TextView mSongName;
    private SeekBar mPlayTime;
    private ImageButton mPrevious;
    private ImageButton mPlayPause;
    private ImageButton mNext;

    private String mPreviewUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getIntentExtras();
        initialiseUI();
        populatePlayerUI();

        // TODO - abstract player logic into Fragment.
        // TODO - pass list of track id's into this Activity.
        // TODO - re-initialise Fragment with next/previous track id next/previous button click.
    }

    private void getIntentExtras() {
        Intent intent = PlayerActivity.this.getIntent();
        if (intent.hasExtra(Constants.TRACK_ID_TAG)) {
            mTrackId = intent.getExtras().getString(Constants.TRACK_ID_TAG);
            Log.d(TAG, "Track ID: " + mTrackId);
        }
    }

    private void initialiseUI() {
        mArtistName = (TextView) findViewById(R.id.player_tv_artist_name);
        mAlbumName = (TextView) findViewById(R.id.player_tv_album_name);
        mSongName = (TextView) findViewById(R.id.player_tv_song_name);
        mAlbumCover = (ImageView) findViewById(R.id.player_img_album_cover);
        mPlayTime = (SeekBar) findViewById(R.id.player_seek_bar_play_time);
        mPrevious = (ImageButton) findViewById(R.id.player_btn_previous);
        mPlayPause = (ImageButton) findViewById(R.id.player_btn_play_pause);
        mNext = (ImageButton) findViewById(R.id.player_btn_next);
    }

    private void populatePlayerUI() {
        PlayerTask task = new PlayerTask(mArtistName, mAlbumName,
                mAlbumCover, mSongName, mPlayTime, mPrevious, mPlayPause, mNext);
        task.execute(mTrackId);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // To do - make some settings!
            return true;
        }

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
