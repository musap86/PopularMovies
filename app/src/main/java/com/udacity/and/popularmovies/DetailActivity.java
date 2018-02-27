package com.udacity.and.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.and.popularmovies.utilities.JsonUtility;
import com.udacity.and.popularmovies.utilities.MovieRequestUtility;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int index = getIntent().getIntExtra("index", 0);

        ImageView poster = findViewById(R.id.iv_detail_movie_poster);

        String posterImagePath = MovieRequestUtility.buildUrlString(JsonUtility.getImagePath(index));
        Picasso.with(this).load(posterImagePath).into(poster);

        TextView textViewTitle = findViewById(R.id.tv_title);
        TextView textViewAvgVote = findViewById(R.id.tv_average_vote);
        TextView textViewRelDate = findViewById(R.id.tv_release_date);
        TextView textViewPlot = findViewById(R.id.tv_plot_synopsis);

        textViewTitle.setText(JsonUtility.getTitle(index));
        textViewAvgVote.setText(JsonUtility.getAverageVote(index));
        textViewRelDate.setText(JsonUtility.getReleaseDate(index));
        textViewPlot.setText(JsonUtility.getPlot(index));
    }
}
