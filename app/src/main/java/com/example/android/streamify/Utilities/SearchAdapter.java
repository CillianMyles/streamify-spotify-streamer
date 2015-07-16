package com.example.android.streamify.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
        BitmapTask bitmap = new BitmapTask();

        if (artist.images.size() > 0) {
            bitmap.execute(artist.images.get(0).url);
        }
        artistName.setText(artist.name);

        return artistView;
    }

    /**
     * Retrieve image from URL.
     */
    public class BitmapTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap bm = null;
            try {
                URL aURL = new URL(params[0]);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "Error getting artist img bitmap", e);
            }

            return bm;
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap) {
            if (bitmap != null) {
                artistImage.setImageBitmap(bitmap);
            }
        }
    }
}