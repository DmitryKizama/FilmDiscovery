package com.stkizema.test8telemarketing.api;

import com.stkizema.test8telemarketing.model.CategoryResponse;
import com.stkizema.test8telemarketing.model.MoviesResponse;
import com.stkizema.test8telemarketing.model.VideoResponse;
import com.stkizema.test8telemarketing.util.Config;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FilmApiInterface {

    //Get top rated movies from server by api_key
    @GET(Config.URL_GET_TOP_MOVIE_RATED)
    Call<MoviesResponse> getTopRatedFilms(@Query("api_key") String api_key);

    //Get categories from server by api_key
    @GET(Config.URL_GET_CATEGORY)
    Call<CategoryResponse> getCategoryFilms(@Query("api_key") String api_key, @Query("language") String language);

    @GET(Config.URL_GET_MOVIES_BY_CATEGORY)
    Call<MoviesResponse> getMoviesByCategory(@Path("id") Integer idCategory, @Query("api_key") String api_key, @Query("language") String language,
                                             @Query("include_adult") String include_adult, @Query("sort_by") String sort);

    @GET(Config.URL_GET_MOVIE_BY_NAME)
    Call<MoviesResponse> getMoviesByName(@Query("api_key") String api_key, @Query("query") String nameMovie);

    @GET("movie/{id}/videos")
    Call<VideoResponse> getVideoByMovieId(@Path("id") Integer idMovie, @Query("api_key") String api_key, @Query("language") String language);
}
