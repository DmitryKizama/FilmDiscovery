package com.stkizema.test8telemarketing.util;

public class Config {

    public static final String URL_GET_TOP_MOVIE_RATED = "movie/top_rated";
    public static final String URL_GET_CATEGOTY = "genre/movie/list";

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
    public static final int OK = 200;
    public static final int BAD_REQUEST = 400;
}
