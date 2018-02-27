package com.udacity.and.popularmovies.data;

import com.udacity.and.popularmovies.utilities.MovieRequestUtility;

/**
 * Created by Musap Kahraman on 27.02.2018.
 */

public class MoviesPreferences {

    private static boolean isSortingByRating;

    public static int getSortingType() {
        if (isSortingByRating)
            return MovieRequestUtility.TYPE_HIGHEST_RATED;
        else
            return MovieRequestUtility.TYPE_MOST_POPULAR;
    }

    public static void toggleSortingType() {
        isSortingByRating = !isSortingByRating;
    }
}
