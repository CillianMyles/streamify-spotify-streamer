package com.example.android.streamify.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.streamify.R;
import com.example.android.streamify.StreamifyApplication;
import com.squareup.picasso.Picasso;

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

        if (track.album.images.size() > 0) {

            String firstChoicePicUrl = "";
            String secondChoicePicUrl = "";
            String lastChoicePicUrl = "";

            for (int i = 0; i < track.album.images.size(); i++) {

                String currentUrl = track.album.images.get(i).url;

                if (track.album.images.get(i).height >= 640) {
                    firstChoicePicUrl = currentUrl;
                } else if (track.album.images.get(i).height >= 300) {
                    secondChoicePicUrl = currentUrl;
                } else {
                    lastChoicePicUrl = currentUrl;
                }
            }

            String urlToUse = "";

            if (!firstChoicePicUrl.equals("")) {
                urlToUse = firstChoicePicUrl;
            } else if (!secondChoicePicUrl.equals("")) {
                urlToUse = secondChoicePicUrl;
            } else if (!lastChoicePicUrl.equals("")) {
                urlToUse = lastChoicePicUrl;
            }

            Picasso.with(StreamifyApplication.getContext())
                    .load(urlToUse)
                    .into(artistImage);
        }

        albumName.setText(track.album.name);
        songName.setText(track.name);

        return trackView;
    }
}