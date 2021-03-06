package com.example.android.streamify.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.streamify.R;
import com.example.android.streamify.tracks.TracksActivity;
import com.example.android.streamify.utilities.Constants;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Artist;

@SuppressWarnings({"FieldCanBeLocal", "ConstantConditions"})
public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private EditText mSearchText;
    private ListView mSearchResults;
    private SearchAdapter mSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initialiseUI();
    }

    private void initialiseUI() {
        initialiseSearchResultsList();
        initialiseSearchTextField();
    }

    private void initialiseSearchResultsList() {
        mSearchResults = (ListView) findViewById(R.id.search_list_artists);
        mSearchAdapter = new SearchAdapter(SearchActivity.this, new ArrayList<Artist>());
        mSearchResults.setAdapter(mSearchAdapter);
        mSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = mSearchAdapter.getItem(position);
                Intent intent = new Intent(SearchActivity.this, TracksActivity.class);
                intent.putExtra(Constants.ARTIST_ID_TAG, artist.id);
                startActivity(intent);
            }
        });
    }

    private void initialiseSearchTextField() {
        mSearchText = (EditText) findViewById(R.id.search_et_artist_name);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.action_search_ime || id == EditorInfo.IME_NULL) {
                    SearchTask search = new SearchTask(mSearchAdapter);
                    search.execute(mSearchText.getText().toString());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // To do - make some settings!
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
