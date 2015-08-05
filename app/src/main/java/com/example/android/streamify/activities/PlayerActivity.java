package com.example.android.streamify.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.streamify.R;
import com.example.android.streamify.utilities.Constants;

public class PlayerActivity extends AppCompatActivity {

    private static final String TAG = PlayerActivity.class.getSimpleName();

    private String mTrackId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getIntentExtras();
        initialiseUI();
        setUpSpotify();
        populateTrackUI();
    }

    private void getIntentExtras() {
        if (getIntent().hasExtra(Constants.TRACK_ID_TAG)) {
            mTrackId = getIntent().getExtras().getString(Constants.TRACK_ID_TAG);
        }
    }

    private void initialiseUI() {

    }

    private void setUpSpotify() {

    }

    private void populateTrackUI() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
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
