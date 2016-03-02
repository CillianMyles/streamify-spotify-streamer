package com.example.android.streamify.player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.android.streamify.StreamifyApplication;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Represents an asynchronous task used to get info on a particular track.
 */
public class PlayerTask extends AsyncTask<String, Void, String> {

    private final String TAG = PlayerTask.class.getSimpleName();

    private Boolean mPlaying;
    private MediaPlayer mMediaPlayer;

    private SpotifyService mSpotify;
    private TextView mArtistName;
    private TextView mAlbumName;
    private ImageView mAlbumCover;
    private TextView mSongName;
    private SeekBar mPlayTime;
    private ImageButton mPrevious;
    private ImageButton mPlayPause;
    private ImageButton mNext;

    public PlayerTask(SpotifyService spotifyService, TextView artistName, TextView albumName,
                      ImageView albumCover, TextView songName, SeekBar playTime,
                      ImageButton previous, ImageButton playPause, ImageButton next) {

        this.mSpotify = spotifyService;
        this.mArtistName = artistName;
        this.mAlbumName = albumName;
        this.mAlbumCover = albumCover;
        this.mSongName = songName;
        this.mPlayTime = playTime;
        this.mPrevious = previous;
        this.mPlayPause = playPause;
        this.mNext = next;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mPlaying = false;
    }

    @Override
    protected String doInBackground(String... params) {

        if (!params[0].equals("")) {

            mSpotify.getTrack(params[0], new Callback<Track>() {
                @Override
                public void success(Track track, Response response) {
                    Log.v(TAG, "Got response about song successfully.");

                    // Update UI.
                    mArtistName.setText(track.artists.get(0).name);
                    mAlbumName.setText(track.album.name);
                    Picasso.with(StreamifyApplication.getContext())
                            .load(track.album.images.get(0).url)
                            .into(mAlbumCover);
                    mSongName.setText(track.name);

                    // Begin music download.
                    prepareMediaStreamer(track.preview_url);
                    Log.v(TAG, "Song preview URL : " + track.preview_url);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "Request failure.", error);
                }
            });
        }
        return null;
    }

    @Override
    protected void onPostExecute(final String previewUrl) {
        initialisePlayPause();
    }

    private void prepareMediaStreamer(String url) {
        Log.v(TAG, "Prepping media streamer.");
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            Log.e(TAG, "Could not stream content.", e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Incorrect arguments.", e);
        }
    }

    private void initialisePlayPause() {
        int previewDuration = 30 * 1000;
        mPlayTime.setMax(previewDuration);
        mPlayPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!mPlaying) {
                    Log.v(TAG, "Playing!");
                    mPlaying = true;
                    mPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                    mMediaPlayer.start();
//                    startSeekBarUpdates();
                } else {
                    Log.v(TAG, "Pausing!");
                    mPlaying = false;
                    mPlayPause.setImageResource(android.R.drawable.ic_media_play);
                    mMediaPlayer.pause();
                }
            }
        });
    }

    private Handler mPlayTimeHandler = new Handler();

    public void startSeekBarUpdates() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mPlaying) {
                    try {
                        mPlayTimeHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // Update the UI.
                                if (mMediaPlayer != null) {
                                    int mCurrentPosition = mMediaPlayer.getCurrentPosition() / 1000;
                                    mPlayTime.setProgress(mCurrentPosition);
                                }
                                mPlayTimeHandler.postDelayed(this, 1000);
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "Error.", e);
                    }
                }
            }
        }).start();
    }
}
