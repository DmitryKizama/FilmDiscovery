package com.stkizema.test8telemarketing.api;

import com.stkizema.test8telemarketing.model.CategoryResponse;
import com.stkizema.test8telemarketing.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FilmApiInterface {

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedFilms(@Query("api_key") String api_key);

    @GET("genre/movie/list")
    Call<CategoryResponse> getCategoryFilms(@Query("api_key") String api_key, @Query("language") String language);

}
