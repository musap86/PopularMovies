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
import com.udacity.and.popularmovies.utilities.MovieRequestUtility;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MoviePostersAdapter.ListItemClickListener {

    private static final int POSTERS_GRID_COLUMN_COUNT = 3;
    private MoviePostersAdapter mAdapter;
    private RecyclerView mMoviePosters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviePosters = findViewById(R.id.rv_movie_posters);

        GridLayoutManager layoutManager = new GridLayoutManager(this, POSTERS_GRID_COLUMN_COUNT);
        mMoviePosters.setLayoutManager(layoutManager);
        mMoviePosters.setHasFixedSize(true);
        mAdapter = new MoviePostersAdapter(null, MainActivity.this);
        mMoviePosters.setAdapter(mAdapter);

        loadMoviesData();
    }

    public void loadMoviesData() {
        if (!MovieRequestUtility.isOnline(this)) {
            Toast.makeText(this, R.string.no_intenet_connection, Toast.LENGTH_SHORT).show();
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
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_by) {
            MoviesPreferences.toggleSortingType();
            Toast.makeText(this, R.string.toggle_toast_message, Toast.LENGTH_SHORT).show();
            loadMoviesData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchMoviesData extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {

            URL movieRequestUrl = MovieRequestUtility.buildUrl(MoviesPreferences.getSortingType());

            try {
                String jsonResponse = MovieRequestUtility.getResponseFromHttpUrl(movieRequestUrl);
                return JsonUtility.getImagePathsFromJson(jsonResponse);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] data) {
            if (data != null) {
                mAdapter = new MoviePostersAdapter(data, MainActivity.this);
                mMoviePosters.setAdapter(mAdapter);
            }
        }
    }
}
