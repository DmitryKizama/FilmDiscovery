package com.stkizema.test8telemarketing.api;

import com.stkizema.test8telemarketing.model.CategoryResponse;
import com.stkizema.test8telemarketing.model.MoviesResponse;
import com.stkizema.test8telemarketing.util.Config;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FilmApiInterface {

    //Get top rated movies from server by api_key
    @GET(Config.URL_GET_TOP_MOVIE_RATED)
    Call<MoviesResponse> getTopRatedFilms(@Query("api_key") String api_key);

    //Get categories from server by api_key
    @GET(Config.URL_GET_CATEGOTY)
    Call<CategoryResponse> getCategoryFilms(@Query("api_key") String api_key, @Query("language") String language);


}
