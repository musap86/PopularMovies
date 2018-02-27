package com.udacity.and.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.and.popularmovies.utilities.MovieRequestUtility;

public class MoviePostersAdapter extends RecyclerView.Adapter<MoviePostersAdapter.MoviePostersViewHolder> {

    private int mMoviePosterCount;
    private String[] mImagePaths;

    private ListItemClickListener mOnClickListener;

    /**
     * Constructor for MoviePostersAdapter that accepts an array of poster paths to display
     *
     * @param imagePaths Paths of posters to display
     */
    public MoviePostersAdapter(String[] imagePaths, ListItemClickListener listener) {
        if (imagePaths == null)
            return;
        mMoviePosterCount = imagePaths.length;
        mImagePaths = imagePaths;
        mOnClickListener = listener;
    }

    @Override
    public MoviePostersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_poster_item, parent, false);
        return new MoviePostersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviePostersViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mMoviePosterCount;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    /**
     * Cache of the children views for a movie poster item.
     */
    class MoviePostersViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        ImageView moviePosterImageView;

        public MoviePostersViewHolder(View itemView) {
            super(itemView);
            moviePosterImageView = itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        void bind(int pos) {
            String posterImagePath = MovieRequestUtility.buildUrlString(mImagePaths[pos]);
            Picasso.with(itemView.getContext()).load(posterImagePath).into(moviePosterImageView);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
