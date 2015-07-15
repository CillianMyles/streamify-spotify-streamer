package com.example.android.streamify;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.streamify.Utilities.SearchAdapter;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private EditText searchText;
    private ListView searchResults;

    SpotifyApi spotifyApi;
    SpotifyService spotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initialiseUI();
        setUpSpotify();
    }

    private void initialiseUI() {
        searchText = (EditText) findViewById(R.id.search_et_artist_name);
        searchResults = (ListView) findViewById(R.id.search_list_artist_details);

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.action_search_ime || id == EditorInfo.IME_NULL) {
                    SearchTask search = new SearchTask();
                    search.execute(searchText.getText().toString());
                    View view = SearchActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void setUpSpotify() {
        spotifyApi = new SpotifyApi();
        spotify = spotifyApi.getService();
//        spotifyApi.setAccessToken("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Represents an asynchronous login task used to authenticate mUser.
     */
    public class SearchTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            boolean hasResults = false;

            spotify.searchArtists(params[0], new Callback<ArtistsPager>() {
                @Override
                public void success(ArtistsPager artistsPager, Response response) {
                    ArrayList<Artist> list = new ArrayList<>();
                    for (int i = 0; i < artistsPager.artists.items.size(); i++) {
                        list.add(artistsPager.artists.items.get(i));
                        Log.v(TAG, "ARTIST:" + artistsPager.artists.items.get(i).name);
                    }
                    ArrayAdapter adapter = new SearchAdapter(SearchActivity.this, list);
                    searchResults.setAdapter(adapter);
                    Log.v(TAG, "Request was successful");
//                    if (list.size() > 0) {
//                        hasResults = true;
//                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "Search artists request failed", error);
                }
            });

            return hasResults;
        }

        @Override
        protected void onPostExecute(Boolean success) {
//            mAuthTask = null;
//            showProgress(false);

            if (success) {

            }
        }

        @Override
        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
        }
    }
}
