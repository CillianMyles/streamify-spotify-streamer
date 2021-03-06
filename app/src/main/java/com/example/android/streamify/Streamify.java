package com.example.android.streamify;

import android.app.Application;
import android.content.Context;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

@SuppressWarnings("FieldCanBeLocal")
public class Streamify extends Application {

    private static Streamify instance;

    private static SpotifyApi spotifyApi;
    private static SpotifyService spotifyService;

    public static Streamify getInstance() {
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
        spotifyApi.setAccessToken("XYZ"); // TODO - actually use correct access key!?
        spotifyService = spotifyApi.getService();

        super.onCreate();
    }
}
