package com.example.android.streamify.search;

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

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

public class SearchAdapter extends ArrayAdapter<Artist> {

    private static final String TAG = SearchAdapter.class.getSimpleName();

    private ImageView artistImage;

    public SearchAdapter(Context context, List<Artist> artists) {
        super(context, R.layout.activity_search_artist_list_view, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View artistView = inflater.inflate(R.layout.activity_search_artist_list_view,
                parent, false);

        artistImage = (ImageView) artistView
                .findViewById(R.id.search_artist_list_view_img_artist_pic);
        TextView artistName = (TextView) artistView
                .findViewById(R.id.search_artist_list_view_tv_artist_name);

        Artist artist = getItem(position);

        if (artist.images.size() > 0) {
            Picasso.with(StreamifyApplication.getContext())
                    .load(artist.images.get(0).url)
                    .into(artistImage);
        }

        artistName.setText(artist.name);

        return artistView;
    }
}