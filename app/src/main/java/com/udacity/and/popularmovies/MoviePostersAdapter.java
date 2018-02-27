package com.udacity.and.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.and.popularmovies.utilities.JsonUtility;
import com.udacity.and.popularmovies.utilities.NetworkUtils;

public class MoviePostersAdapter extends RecyclerView.Adapter<MoviePostersAdapter.MoviePostersViewHolder> {

    private final ListItemClickListener mOnClickListener;

    public MoviePostersAdapter(ListItemClickListener listener) {
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
        return JsonUtility.getMovieDataLength();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    /**
     * Cache of the child image view for a movie poster list item.
     */
    class MoviePostersViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private final ImageView moviePosterImageView;

        public MoviePostersViewHolder(View itemView) {
            super(itemView);
            moviePosterImageView = itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        /**
         * Shows corresponding poster image for each movie item in recycler view unless no movie
         * poster image is provided.
         * For a few of the movies the server returns a json response in which there is a
         * "null" statement instead of a path for an image file.
         */
        void bind(int pos) {
            if (JsonUtility.getImagePath(pos).equals("null")) {
                moviePosterImageView.setImageResource(R.drawable.no_poster_image);
            } else {
                String imageUrlString = NetworkUtils.getImageUrlString(JsonUtility.getImagePath(pos));
                Picasso.with(itemView.getContext()).load(imageUrlString).into(moviePosterImageView);
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
