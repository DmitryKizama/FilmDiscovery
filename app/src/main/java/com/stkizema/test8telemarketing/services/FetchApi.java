package com.stkizema.test8telemarketing.services;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.stkizema.test8telemarketing.TopApp;
import com.stkizema.test8telemarketing.db.CategoryHelper;
import com.stkizema.test8telemarketing.db.MovieHelper;
import com.stkizema.test8telemarketing.db.VideoHelper;
import com.stkizema.test8telemarketing.db.model.Category;
import com.stkizema.test8telemarketing.db.model.Movie;
import com.stkizema.test8telemarketing.db.model.Video;
import com.stkizema.test8telemarketing.model.CategoryClient;
import com.stkizema.test8telemarketing.model.CategoryResponse;
import com.stkizema.test8telemarketing.model.MovieClient;
import com.stkizema.test8telemarketing.model.MoviesResponse;
import com.stkizema.test8telemarketing.model.VideoClient;
import com.stkizema.test8telemarketing.model.VideoResponse;
import com.stkizema.test8telemarketing.util.Config;
import com.stkizema.test8telemarketing.util.Logger;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchApi {

    private static final int ZERO_PAGE = 0;

    private Context context;
    private OnResponseListener listener;

    private enum Type {TOP_RATED, MOVIE_BY_NAME, MOVIE_BY_CATEGORY}

    private Type type;
    private String tvSearch;

    public FetchApi(Context context, OnResponseListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void refresh(int page) {
        switch (type) {
            case TOP_RATED:
                fetchTopRatedMovies(page);
                break;
            case MOVIE_BY_CATEGORY:
                fetchMoviesByCategory(tvSearch, page);
                break;
            case MOVIE_BY_NAME:
                fetchMovieByName(tvSearch, page);
                break;
        }
    }

    public void fetchVideoByMovieId(final Integer id) {
        listener.onBeginFetch();
        Call<VideoResponse> call = TopApp.getApiClient().getVideoByMovieId(id, Config.API_KEY, Config.EN_US);
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.body() == null) {
                    Logger.logd("body = null");
                    return;
                }
                Logger.logd("OK");
                List<Video> list = new ArrayList<>();
                for (VideoClient item : response.body().getResults()) {
                    Video video = VideoHelper.create(response.body().getId(), item.getId(), item.getIso_639_1(), item.getIso_3166_1(),
                            item.getKey(), item.getName(), item.getSite(), item.getSize(), item.getType());
                    list.add(video);
                }
                listener.onResponseVideo(list, id);
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Logger.logd("fail");
                listener.onResponseVideo(VideoHelper.getVideosByMovieId(id), id);
            }
        });
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
                    Category category = CategoryHelper.create(item.getName(), item.getId());
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

    public void fetchMovieByName(final String text, int page) {
        tvSearch = text;
        type = Type.MOVIE_BY_NAME;
        Call<MoviesResponse> call = TopApp.getApiClient().getMoviesByName(Config.API_KEY, text);
        call(call, null, text);

    }

    public void fetchMoviesByCategory(String text, int page) {
        tvSearch = text;
        type = Type.MOVIE_BY_CATEGORY;
        Category category = CategoryHelper.getCategoryByName(text);
        if (category == null) {
            Toast.makeText(context, "We haven`t such category", Toast.LENGTH_SHORT).show();
            return;
        }
        Call<MoviesResponse> call = TopApp.getApiClient().getMoviesByCategory(Config.API_KEY, Config.SORT_BY, Integer.toString(page), category.getId());
        call(call, category.getId(), null);
    }


    public void fetchTopRatedMovies(int page) {
        type = Type.TOP_RATED;
        Call<MoviesResponse> call = TopApp.getApiClient().getTopRatedFilms(Config.API_KEY, Integer.toString(page));
        call(call, null, null);
    }

    private void call(final Call<MoviesResponse> call, final Integer idCategory, final String text) {
        listener.onBeginFetch();

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                call.enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        if (response.body() == null) {
                            return;
                        }
                        List<Movie> list = new ArrayList<>();
                        setList(list, response.body().getListMovies());
                        listener.onResponseMovies(list, Config.OK, response.body().getTotalPages(), response.body().getPage());
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        switch (type) {
                            case TOP_RATED:
                                listener.onResponseMovies(MovieHelper.getTopRatedListMovies(), Config.NO_NETWORK, ZERO_PAGE, ZERO_PAGE);
                                break;
                            case MOVIE_BY_CATEGORY:
                                listener.onResponseMovies(MovieHelper.getMoviesByCategoryId(idCategory), Config.NO_NETWORK, ZERO_PAGE, ZERO_PAGE);
                                break;
                            case MOVIE_BY_NAME:
                                List<Movie> listMovie = MovieHelper.getMovieByName(text);
                                if (listMovie == null) {
                                    Toast.makeText(context, "We haven`t such movie", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                listener.onResponseMovies(listMovie, Config.NO_NETWORK, ZERO_PAGE, ZERO_PAGE);
                                break;
                        }

                    }
                });

            }
        }, 1000);
    }

    private void setList(List<Movie> list, List<MovieClient> response) {
        for (MovieClient m : response) {
            Movie mov = MovieHelper.create(m.getGenreIds(), m.getPosterPath(), m.isAdult(), m.getOverview(), m.getReleaseDate(), m.getId(), m.getOriginalTitle(), m.getOriginalLanguage(),
                    m.getTitle(), m.getBackdropPath(), m.getPopularity(), m.getVoteCount(), m.getVideo(), m.getVoteAverage());
            list.add(mov);
        }
    }


}
