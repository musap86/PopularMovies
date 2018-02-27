package com.udacity.and.popularmovies.data;

import com.udacity.and.popularmovies.utilities.NetworkUtils;

/**
 * Caches the preference to view posters by popularity or rating.
 */
public class MoviesPreferences {

    private static boolean isSortByPopularity;

    /**
     * Changes the opted type of sorting.
     */
    public static void toggleSortingType() {
        isSortByPopularity = !isSortByPopularity;
    }

    /**
     * Returns the already opted type of sorting.
     */
    public static int getSortingType() {
        if (isSortByPopularity)
            return NetworkUtils.MOST_POPULAR;
        else
            return NetworkUtils.TOP_RATED;
    }
}
