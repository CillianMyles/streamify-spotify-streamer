package com.example.android.streamify.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.streamify.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;

public class TracksAdapter extends ArrayAdapter<Track> {

    private static final String TAG = TracksAdapter.class.getSimpleName();

    public TracksAdapter(Context context, ArrayList<Track> tracks) {
        super(context, R.layout.activity_tracks_list_view, tracks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View trackView = inflater.inflate(R.layout.activity_tracks_list_view,
                parent, false);

        ImageView artistImage = (ImageView) trackView
                .findViewById(R.id.tracks_img_album_cover);
        TextView albumName = (TextView) trackView
                .findViewById(R.id.tracks_artist_info_tv_album_name);
        TextView songName = (TextView) trackView
                .findViewById(R.id.tracks_artist_info_tv_song_name);

        Track track = getItem(position);
        artistImage.setImageBitmap(getImageBitmap(track.album.images.get(0).url));
        albumName.setText(track.album.name);
        songName.setText(track.name);

        return trackView;
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "Error getting album cover bitmap", e);
        }
        return bm;
    }
}