package com.udacity.and.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.udacity.and.popularmovies.data.MoviesPreferences;
import com.udacity.and.popularmovies.utilities.JsonUtility;
import com.udacity.and.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class MainActivity extends AppCompatActivity implements MoviePostersAdapter.ListItemClickListener {

    private MoviePostersAdapter mAdapter;
    private RecyclerView mMoviePosters;
    private StringBuilder mStringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStringBuilder = new StringBuilder();
        mMoviePosters = findViewById(R.id.rv_movie_posters);

        int posterGridColCount = 2;
        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
            posterGridColCount = 3;
        GridLayoutManager layoutManager = new GridLayoutManager(this, posterGridColCount);

        mMoviePosters.setLayoutManager(layoutManager);
        mMoviePosters.setHasFixedSize(true);
        mAdapter = new MoviePostersAdapter(MainActivity.this);
        mMoviePosters.setAdapter(mAdapter);
        loadMoviesData();
    }

    public void loadMoviesData() {
        if (!NetworkUtils.isOnline(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        new FetchMoviesData().execute();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("index", clickedItemIndex);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort_by) {
            MoviesPreferences.toggleSortingType();
            mStringBuilder.delete(0, mStringBuilder.length()).append(getString(R.string.toggle_toast_message));
            if (MoviesPreferences.getSortingType() == NetworkUtils.MOST_POPULAR)
                mStringBuilder.append(" ").append(getString(R.string.most_popular));
            else
                mStringBuilder.append(" ").append(getString(R.string.top_rated));
            Toast.makeText(this, mStringBuilder.toString(), Toast.LENGTH_SHORT).show();
            loadMoviesData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchMoviesData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            URL movieRequestUrl = NetworkUtils.getListingUrl(MoviesPreferences.getSortingType());
            try {
                return NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String data) {
            if (data != null) {
                JsonUtility.extractMovieDataFromJson(data);
                mAdapter = new MoviePostersAdapter(MainActivity.this);
                mMoviePosters.setAdapter(mAdapter);
            }
        }
    }
}
