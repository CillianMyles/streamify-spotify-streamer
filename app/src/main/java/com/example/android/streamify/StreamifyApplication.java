package com.example.android.streamify;

import android.app.Application;
import android.content.Context;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

public class StreamifyApplication extends Application {

    private static StreamifyApplication instance;

    private static SpotifyApi spotifyApi;
    private static SpotifyService spotifyService;


    public static StreamifyApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    public static SpotifyService getSpotifyService() {
        return spotifyService;
    }

    @Override
    public void onCreate() {
        instance = this;

        spotifyApi = new SpotifyApi();
        //spotifyApi.setAccessToken("");
        spotifyService = spotifyApi.getService();

        super.onCreate();
    }
}
