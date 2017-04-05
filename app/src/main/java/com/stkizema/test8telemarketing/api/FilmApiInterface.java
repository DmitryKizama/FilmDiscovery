package com.stkizema.test8telemarketing.api;

import com.stkizema.test8telemarketing.model.category.CategoryResponse;
import com.stkizema.test8telemarketing.model.credit.actors.CreditResponse;
import com.stkizema.test8telemarketing.model.movie.MoviesResponse;
import com.stkizema.test8telemarketing.model.video.VideoResponse;
import com.stkizema.test8telemarketing.util.Config;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FilmApiInterface {

    //Get top rated movies from server by api_key
    @GET(Config.URL_GET_TOP_MOVIE_RATED)
    Call<MoviesResponse> getTopRatedFilms(@Query("api_key") String api_key, @Query("page") String page);

    //Get categories from server by api_key
    @GET(Config.URL_GET_CATEGORY)
    Call<CategoryResponse> getCategoryFilms(@Query("api_key") String api_key, @Query("language") String language);

    @GET(Config.URL_GET_MOVIES_BY_CATEGORY_DISCOVER)
    Call<MoviesResponse> getMoviesByCategory(@Query("api_key") String api_key, @Query("sort_by") String sort, @Query("page") String page, @Query("with_genres") Integer idCategory);

    @GET(Config.URL_GET_MOVIE_BY_NAME)
    Call<MoviesResponse> getMoviesByName(@Query("api_key") String api_key, @Query("query") String nameMovie);

    @GET("movie/{id}/videos")
    Call<VideoResponse> getVideoByMovieId(@Path("id") Integer idMovie, @Query("api_key") String api_key, @Query("language") String language);

    @GET("movie/{id}/credits")
    Call<CreditResponse> getCreditByMovieId(@Path("id") Integer idMovie, @Query("api_key") String api_key);
}
