package com.example.android.streamify.tracks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.streamify.Streamify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Represents an asynchronous search task used to find artists.
 */
@SuppressWarnings("WeakerAccess")
public class TracksTask extends AsyncTask<String, Void, ArrayList<Track>> {

    private final String TAG = TracksTask.class.getSimpleName();

    private ArrayList<Track> mList;
    private TracksAdapter mTracksAdapter;

    public TracksTask(TracksAdapter tracksAdapter) {
        this.mTracksAdapter = tracksAdapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mList = new ArrayList<>();
    }

    @Override
    protected ArrayList<Track> doInBackground(String... params) {

        if (!params[0].equals("")) {

            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("country", "IE");

            Streamify.getSpotifyService().getArtistTopTrack(params[0], queryMap, new Callback<Tracks>() {
                @Override
                public void success(Tracks tracks, Response response) {
                    Log.d(TAG, "Response: " + response.getBody());
                    for (int i = 0; i < tracks.tracks.size(); i++) {
                        mList.add(tracks.tracks.get(i));
                        Log.v(TAG, "TRACK: " + tracks.tracks.get(i).name);
                    }
                    mTracksAdapter.clear();
                    mTracksAdapter.addAll(mList);
                    Log.v(TAG, "Search top tracks request successful");
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "Search top tracks request failed", error);
                }
            });

            return mList;

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