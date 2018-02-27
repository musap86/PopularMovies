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
 * Utility class for retrieving data from the server.
 * It's important to provide the api key below. To fetch popular movies,
 * you will use the API from themoviedb.org. If you don’t already have an account,
 * you will need to create one in order to request an API Key.
 */
public class NetworkUtils {

    public static final int MOST_POPULAR = 300;
    public static final int TOP_RATED = 301;
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String BASE_URL = "http://api.themoviedb.org/3";
    private static final String PATH_POPULAR = "movie/popular";
    private static final String PATH_TOP_RATED = "movie/top_rated";
    private static final String QUERY_API_KEY = "api_key";
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w342";
    //TODO Paste your own themoviedb.org API key below.
    private static final String PARAM_API_KEY = "";

    /**
     * Builds the URL used to talk to the movieDB server using a sorting type.
     *
     * @param sortingType The type of sorting that will be queried for.
     * @return The URL to query from the movieDB server.
     */
    public static URL getListingUrl(int sortingType) {
        String path = "";
        switch (sortingType) {
            case MOST_POPULAR:
                path = PATH_POPULAR;
                break;
            case TOP_RATED:
                path = PATH_TOP_RATED;
                break;
        }
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(path)
                .appendQueryParameter(QUERY_API_KEY, PARAM_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Query URL " + url);
        return url;
    }

    /**
     * Builds the URL used to talk to the movieDB server using image path for a movie poster.
     *
     * @param imagePath The path for a movie poster.
     * @return The Url in String format to use to fetch the image file.
     */
    public static String getImageUrlString(String imagePath) {
        StringBuilder stringBuilder = new StringBuilder(BASE_IMAGE_URL).append(imagePath);
        Log.v(TAG, "Image URL " + stringBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * Returns the entire result from the HTTP response.
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

    /**
     * Returns if the user is online or not.
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (manager != null) {
            netInfo = manager.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
