package com.example.android.streamify.Utilities;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
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

    private static final String TAG = SearchTask.class.getSimpleName();

    private SpotifyApi spotifyApi;
    private SpotifyService spotify;
    private ArrayList<Artist> list;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        spotifyApi = new SpotifyApi();
        spotify = spotifyApi.getService();
        list = new ArrayList<>();
    }

    @Override
    protected ArrayList<Artist> doInBackground(String... params) {

        spotify.searchArtists(params[0], new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {
                for (int i = 0; i < artistsPager.artists.items.size(); i++) {
                    list.add(artistsPager.artists.items.get(i));
                    Log.v(TAG, "ARTIST:" + artistsPager.artists.items.get(i).name);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Search artists request failed", error);
            }
        });

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<Artist> artists) {

    }

    @Override
    protected void onCancelled() {

    }
}