package com.example.android.streamify.player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.android.streamify.StreamifyApplication;
import com.example.android.streamify.tracks.TracksTask;
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

    private final String TAG = TracksTask.class.getSimpleName();

    private Track mTrack;
    private String mPreviewUrl;
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

    private void initialisePlayPause() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(mPreviewUrl);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            Log.e(TAG, "Could not stream content", e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Incorrect arguments", e);
        }
        mPlayPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!mPlaying) {
                    mMediaPlayer.start();
                    mPlaying = true;
                } else {
                    mMediaPlayer.pause();
                    mPlaying = false;
                }
            }
        });
    }

    @Override
    protected String doInBackground(String... params) {

        if (!params[0].equals("")) {

            mSpotify.getTrack(params[0], new Callback<Track>() {
                @Override
                public void success(Track track, Response response) {
                    mArtistName.setText(track.artists.get(0).name);
                    mAlbumName.setText(track.album.name);
                    Picasso.with(StreamifyApplication.getContext())
                            .load(track.album.images.get(0).url)
                            .into(mAlbumCover);
                    mSongName.setText(track.name);
                    mPreviewUrl = track.preview_url;
                    mTrack = track;
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

            return mPreviewUrl;

        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(final String previewUrl) {
        if (previewUrl != null) {
            initialisePlayPause();
        }
    }
}
