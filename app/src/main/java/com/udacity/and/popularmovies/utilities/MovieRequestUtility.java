package com.udacity.and.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Musap Kahraman on 27.02.2018.
 */

public class MovieRequestUtility {

    public static final int TYPE_MOST_POPULAR = 300;
    public static final int TYPE_HIGHEST_RATED = 301;
    private static final String TAG = MovieRequestUtility.class.getSimpleName();
    private static final String MOVIE_DISCOVER_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w342";
    private static final String QUERY_SORT_BY = "sort_by";
    private static final String PARAM_POPULARITY_DESC = "popularity.desc";
    private static final String PARAM_RATING_DESC = "vote_average.desc";
    private static final String QUERY_API_KEY = "api_key";
    //TODO Paste your own themoviedb.org API key below.
    private static final String PARAM_API_KEY = "";

    /**
     * Builds the URL used to talk to the movieDB server using a sorting query. This query is based
     * on the query capabilities of the movie provider that we are using.
     *
     * @param sortingType The type of sorting that will be queried for.
     * @return The URL to use to query the movieDB server.
     */
    public static URL buildUrl(int sortingType) {
        String sortingParam = "";
        switch (sortingType) {
            case TYPE_MOST_POPULAR:
                sortingParam = PARAM_POPULARITY_DESC;
                break;
            case TYPE_HIGHEST_RATED:
                sortingParam = PARAM_RATING_DESC;
                break;
        }
        Uri builtUri = Uri.parse(MOVIE_DISCOVER_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_SORT_BY, sortingParam)
                .appendQueryParameter(QUERY_API_KEY, PARAM_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * Builds the URL used to talk to the movieDB server using image folder location
     * of an image.
     *
     * @param imageLocation The longitude of the location
     * @return The Url to use to query the weather server.
     */
    public static String buildUrlString(String imageLocation) {
        StringBuilder stringBuilder = new StringBuilder(IMAGE_BASE_URL).append(imageLocation);
        Log.v(TAG, "Built URI " + stringBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = manager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
