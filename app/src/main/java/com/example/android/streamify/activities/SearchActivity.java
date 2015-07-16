package com.example.android.streamify.activities;

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

import com.example.android.streamify.R;
import com.example.android.streamify.StreamifyApplication;
import com.example.android.streamify.Utilities.SearchAdapter;

import java.util.ArrayList;

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

    private ArrayAdapter mSearchAdapter;

    private SpotifyService spotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initialiseUI();
        setUpSpotify();
    }

    private void initialiseUI() {
        initialiseSearchResultsList();
        initialiseSearchTextField();
    }

    private void initialiseSearchResultsList() {
        searchResults = (ListView) findViewById(R.id.search_list_artist_details);
    }

    private void initialiseSearchTextField() {
        searchText = (EditText) findViewById(R.id.search_et_artist_name);
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
        spotify = StreamifyApplication.getSpotifyService();
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
     * Represents an asynchronous search task used to find artists.
     */
    public class SearchTask extends AsyncTask<String, Void, ArrayList<Artist>> {

        private final String TAG = SearchTask.class.getSimpleName();

        private ArrayList<Artist> list;
        boolean hasResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hasResults = false;
            list = new ArrayList<>();
        }

        @Override
        protected ArrayList<Artist> doInBackground(String... params) {

            if (!params[0].equals("")) {

                spotify.searchArtists(params[0], new Callback<ArtistsPager>() {
                    @Override
                    public void success(ArtistsPager artistsPager, Response response) {
                        for (int i = 0; i < artistsPager.artists.items.size(); i++) {
                            list.add(artistsPager.artists.items.get(i));
                            Log.v(TAG, "ARTIST:" + artistsPager.artists.items.get(i).name);
                        }
                        mSearchAdapter = new SearchAdapter(SearchActivity.this, list);
                        searchResults.setAdapter(mSearchAdapter);
                        Log.v(TAG, "Request was successful");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "Search artists request failed", error);
                    }
                });

                return list;

            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final ArrayList<Artist> artists) {
            if (artists == null) {
                mSearchAdapter = new SearchAdapter(SearchActivity.this, new ArrayList<Artist>());
                searchResults.setAdapter(mSearchAdapter);
                Log.v(TAG, "Searched for nothing, no artists in response.");
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
}
