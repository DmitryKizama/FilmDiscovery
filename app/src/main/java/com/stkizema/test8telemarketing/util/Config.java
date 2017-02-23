package com.stkizema.test8telemarketing.util;

import com.stkizema.test8telemarketing.R;
import com.stkizema.test8telemarketing.TopApp;

public class Config {

    public static final String URL_GET_TOP_MOVIE_RATED = "movie/top_rated";
    public static final String URL_GET_CATEGORY = "genre/movie/list";
    public static final String URL_GET_MOVIES_BY_CATEGORY = "genre/{id}/movies";
    public static final String URL_GET_MOVIE_BY_NAME = "search/movie";

    // MovieClient constants
    public static final String POSTER_PATH = "poster_path";
    public static final String ADULT = "adult";
    public static final String OVERVIEW = "overview";
    public static final String RELEASE_DATE = "release_date";
    public static final String GENRE_IDS = "genre_ids";
    public static final String ID_MOVIE = "id";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String ORIGINAL_LANGUAGE = "original_language";
    public static final String TITLE = "title";
    public static final String BACKDROP_PATH = "backdrop_path";
    public static final String POPULARITY = "popularity";
    public static final String VOTE_COUNT = "vote_count";
    public static final String VIDEO = "video";
    public static final String VOTE_AVERAGE = "vote_average";

    //MovieResponse constants
    public static final String PAGE = "page";
    public static final String RESULTS = "results";
    public static final String TOTAL_RESULTS = "total_results";
    public static final String TOTAL_PAGES = "total_pages";

    //CategoryResponse constants
    public static final String GENRES = "genres";

    //CategoryClient constants
    public static final String ID_CATEGORY = "id";
    public static final String NAME = "name";

    //List of HTTP status codes
    public static final String OK = "OK";
    public static final String BAD_REQUEST = "NO NETWORK";

    public static final String API_KEY = TopApp.getContext().getResources().getString(R.string.api_key);
    public static final String EN_US = "en-US";
    public static final String INCLUDE_ADULT = "false";
    public static final String SORT_BY = "created_at.asc";
}
