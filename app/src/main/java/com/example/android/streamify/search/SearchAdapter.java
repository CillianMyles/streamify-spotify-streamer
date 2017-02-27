package com.example.android.streamify.search;

import android.content.Context;
import android.support.annotation.NonNull;
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

@SuppressWarnings({"WeakerAccess", "ConstantConditions", "FieldCanBeLocal"})
public class SearchAdapter extends ArrayAdapter<Artist> {

    private static final String TAG = SearchAdapter.class.getSimpleName();

    private LayoutInflater mInflater;
    private ImageView mArtistImage;
    private TextView mArtistName;

    public SearchAdapter(Context context, List<Artist> artists) {
        super(context, R.layout.activity_search_artist_list_view, artists);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View artistView = convertView;

        // Only inflate if not re-using convertView (expensive)!
        if (convertView == null) {
            artistView = mInflater.inflate(R.layout.activity_search_artist_list_view, parent, false);
        }

        mArtistImage = (ImageView) artistView.findViewById(R.id.search_artist_list_view_img_artist_pic);
        mArtistName = (TextView) artistView.findViewById(R.id.search_artist_list_view_tv_artist_name);

        Artist artist = getItem(position);

        if (artist.images.size() > 0) {
            Picasso.with(StreamifyApplication.getContext())
                    .load(artist.images.get(0).url)
                    .into(mArtistImage);
        }

        mArtistName.setText(artist.name);

        return artistView;
    }
}