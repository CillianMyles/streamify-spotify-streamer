package com.example.android.streamify.tracks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.streamify.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;

@SuppressWarnings({"WeakerAccess", "ConstantConditions", "FieldCanBeLocal"})
public class TracksAdapter extends ArrayAdapter<Track> {

    private static final String TAG = TracksAdapter.class.getSimpleName();

    private LayoutInflater mInflater;
    private ImageView mArtistImage;
    private TextView mAlbumName;
    private TextView mSongName;

    public TracksAdapter(Context context, ArrayList<Track> tracks) {
        super(context, R.layout.activity_tracks_list_view, tracks);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View trackView = convertView;

        if (convertView == null) {
            trackView = mInflater.inflate(R.layout.activity_tracks_list_view, parent, false);
        }

        mArtistImage = (ImageView) trackView.findViewById(R.id.tracks_img_album_cover);
        mAlbumName = (TextView) trackView.findViewById(R.id.tracks_artist_info_tv_album_name);
        mSongName = (TextView) trackView.findViewById(R.id.tracks_artist_info_tv_song_name);

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

            Picasso.with(getContext())
                    .load(urlToUse)
                    .into(mArtistImage);
        }

        mAlbumName.setText(track.album.name);
        mSongName.setText(track.name);

        return trackView;
    }
}
