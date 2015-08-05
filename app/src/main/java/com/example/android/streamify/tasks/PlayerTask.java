package com.example.android.streamify.tasks;

import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.streamify.StreamifyApplication;
import com.squareup.picasso.Picasso;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Represents an asynchronous task used to get info on a particular track.
 */
public class PlayerTask extends AsyncTask<String, Void, Track> {

    private final String TAG = TracksTask.class.getSimpleName();

    private Track mTrack;

    private SpotifyService mSpotify;
    private TextView mArtistName;
    private TextView mAlbumName;
    private ImageView mAlbumCover;
    private TextView mSongName;

    public PlayerTask(SpotifyService spotifyService, TextView artistName,
                      TextView albumName, ImageView albumCover, TextView songName) {
        this.mSpotify = spotifyService;
        this.mArtistName = artistName;
        this.mAlbumName = albumName;
        this.mAlbumCover = albumCover;
        this.mSongName = songName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Track doInBackground(String... params) {

        if (!params[0].equals("")) {

            mSpotify.getTrack(params[0], new Callback<Track>() {
                @Override
                public void success(Track track, Response response) {
                    mTrack = track;
                }

                @Override
                public void failure(RetrofitError error) {
                    mTrack = new Track();
                }
            });

            return mTrack;

        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(final Track track) {
        if (track.id != null) {
            mArtistName.setText(track.artists.get(0).name);
            mAlbumName.setText(track.album.name);
            if (track.album.images.size() > 0) {
                Picasso.with(StreamifyApplication.getContext())
                        .load(track.album.images.get(0).url)
                        .into(mAlbumCover);
            }
            mSongName.setText(track.name);
        }
    }
}