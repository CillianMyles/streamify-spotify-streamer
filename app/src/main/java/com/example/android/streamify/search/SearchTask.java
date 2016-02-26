package com.example.android.streamify.search;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Represents an asynchronous search task used to find artists.
 */
public class SearchTask extends AsyncTask<String, Void, ArrayList<Artist>> {

    private final String TAG = SearchTask.class.getSimpleName();

    private ArrayList<Artist> mList;

    private SpotifyService mSpotify;
    private SearchAdapter mSearchAdapter;

    public SearchTask(SpotifyService spotify, SearchAdapter searchAdapter) {
        this.mSpotify = spotify;
        this.mSearchAdapter = searchAdapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mList = new ArrayList<>();
    }

    @Override
    protected ArrayList<Artist> doInBackground(String... params) {

        if (!params[0].equals("")) {

            mSpotify.searchArtists(params[0], new Callback<ArtistsPager>() {
                @Override
                public void success(ArtistsPager artistsPager, Response response) {
                    Log.d(TAG, "Response : " + response.getBody());
                    for (int i = 0; i < artistsPager.artists.items.size(); i++) {
                        mList.add(artistsPager.artists.items.get(i));
                        Log.v(TAG, "ARTIST:" + artistsPager.artists.items.get(i).name);
                    }
                    mSearchAdapter.clear();
                    mSearchAdapter.addAll(mList);
                    Log.v(TAG, "Search artists request successful");
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "Search artists request failed", error);
                }
            });

            return mList;

        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(final ArrayList<Artist> artists) {
        if (artists == null) {
            mSearchAdapter.clear();
            mSearchAdapter.addAll(new ArrayList<Artist>());
            Log.v(TAG, "Searched for nothing, no artists in response.");
        }
    }

    @Override
    protected void onCancelled() {

    }
}
