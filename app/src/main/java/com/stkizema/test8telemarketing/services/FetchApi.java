package com.stkizema.test8telemarketing.services;

import android.content.Context;
import android.widget.Toast;

import com.stkizema.test8telemarketing.TopApp;
import com.stkizema.test8telemarketing.db.CategoryHelper;
import com.stkizema.test8telemarketing.db.MovieHelper;
import com.stkizema.test8telemarketing.db.model.Category;
import com.stkizema.test8telemarketing.db.model.Movie;
import com.stkizema.test8telemarketing.model.CategoryClient;
import com.stkizema.test8telemarketing.model.CategoryResponse;
import com.stkizema.test8telemarketing.model.MovieClient;
import com.stkizema.test8telemarketing.model.MoviesResponse;
import com.stkizema.test8telemarketing.util.Config;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchApi {
    public static final String ACTIONINSERVICE = "ACTIONINSERVICE";
    private Context context;
    private OnResponseListener listener;

    private enum Type {TOP_RATED, MOVIE_BY_NAME, MOVIE_BY_CATEGORY}

    private Type type;
    private String tvSearch;

    public FetchApi(Context context, OnResponseListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void refresh() {
        switch (type) {
            case TOP_RATED:
                fetchTopRatedMovies();
                break;
            case MOVIE_BY_CATEGORY:
                fetchMoviesByCategory(tvSearch);
                break;
            case MOVIE_BY_NAME:
                fetchMovieById(tvSearch);
                break;
        }
    }

    public void fetchCategories() {
        listener.onBeginFetch();
        Call<CategoryResponse> call = TopApp.getApiClient().getCategoryFilms(Config.API_KEY, Config.EN_US);
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.body() == null) {
                    return;
                }
                List<Category> list = new ArrayList<>();
                for (CategoryClient item : response.body().getGenres()) {
                    CategoryHelper.create(item.getName(), item.getId());
                    Category category = new Category();
                    category.setName(item.getName());
                    category.setId(item.getId());
                    list.add(category);
                }
                listener.onResponseCategory(list);
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                listener.onResponseCategory(CategoryHelper.getAllCategory());
            }
        });
    }

    public void fetchMovieById(final String text) {
        tvSearch = text;
        type = Type.MOVIE_BY_NAME;

        Call<MoviesResponse> call = TopApp.getApiClient().getMoviesByName(Config.API_KEY, text);
        call(call, null, text);

    }

    public void fetchMoviesByCategory(String text) {
        tvSearch = text;
        type = Type.MOVIE_BY_CATEGORY;
        Category category = CategoryHelper.getCategoryByName(text);
        if (category == null) {
            Toast.makeText(context, "We haven`t such category", Toast.LENGTH_SHORT).show();
            return;
        }
        Call<MoviesResponse> call = TopApp.getApiClient().getMoviesByCategory(category.getId(),
                Config.API_KEY, Config.EN_US, Config.INCLUDE_ADULT, Config.SORT_BY);
        call(call, category.getId(), null);
    }


    public void fetchTopRatedMovies() {
        type = Type.TOP_RATED;
        Call<MoviesResponse> call = TopApp.getApiClient().getTopRatedFilms(Config.API_KEY);
        call(call, null, null);
    }

    private void call(Call<MoviesResponse> call, final Integer idCategory, final String text) {
        listener.onBeginFetch();
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.body() == null) {
                    return;
                }
                List<Movie> list = new ArrayList<>();
                setList(list, response.body().getListMovies());
                listener.onResponseMovies(list, Config.OK);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                switch (type) {
                    case TOP_RATED:
                        listener.onResponseMovies(MovieHelper.getTopRatedListMovies(), Config.BAD_REQUEST);
                        break;
                    case MOVIE_BY_CATEGORY:
                        listener.onResponseMovies(MovieHelper.getMoviesByCategoryId(idCategory), Config.BAD_REQUEST);
                        break;
                    case MOVIE_BY_NAME:
                        List<Movie> listMovie = MovieHelper.getMovieByName(text);
                        if (listMovie == null) {
                            Toast.makeText(context, "We haven`t such movie", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        listener.onResponseMovies(listMovie, Config.BAD_REQUEST);
                        break;
                }

            }
        });
    }

    private void setList(List<Movie> list, List<MovieClient> response) {
        for (MovieClient m : response) {
            MovieHelper.create(m.getGenreIds(), m.getPosterPath(), m.isAdult(), m.getOverview(), m.getReleaseDate(), m.getId(), m.getOriginalTitle(), m.getOriginalLanguage(),
                    m.getTitle(), m.getBackdropPath(), m.getPopularity(), m.getVoteCount(), m.getVideo(), m.getVoteAverage());
            Movie mov = new Movie();
            mov.setVoteAverage(m.getVoteAverage());
            mov.setVideo(m.getVideo());
            mov.setVoteCount(m.getVoteCount());
            mov.setPopularity(m.getPopularity());
            mov.setAdult(m.isAdult());
            mov.setBackdropPath(m.getBackdropPath());
            mov.setId(m.getId());
            mov.setOriginalLanguage(m.getOriginalLanguage());
            mov.setOriginalTitle(m.getOriginalTitle());
            mov.setOverview(m.getOverview());
            mov.setTitle(m.getTitle());
            mov.setReleaseDate(m.getReleaseDate());
            mov.setPosterPath(m.getPosterPath());
            list.add(mov);
        }
    }


}
