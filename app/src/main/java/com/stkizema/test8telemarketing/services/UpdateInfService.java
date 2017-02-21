package com.stkizema.test8telemarketing.services;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.stkizema.test8telemarketing.R;
import com.stkizema.test8telemarketing.TopApp;
import com.stkizema.test8telemarketing.activites.MainActivity;
import com.stkizema.test8telemarketing.db.CategoryHelper;
import com.stkizema.test8telemarketing.db.MovieHelper;
import com.stkizema.test8telemarketing.model.CategoryClient;
import com.stkizema.test8telemarketing.model.CategoryResponse;
import com.stkizema.test8telemarketing.model.MovieClient;
import com.stkizema.test8telemarketing.model.MoviesResponse;
import com.stkizema.test8telemarketing.util.Config;
import com.stkizema.test8telemarketing.util.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateInfService extends android.app.Service {


    public static final String ACTIONINSERVICE = "ACTIONINSERVICE";

    private final IBinder iBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public UpdateInfService getService() {
            return UpdateInfService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public void fetchCategories() {
        Call<CategoryResponse> call = TopApp.getApiClient().getCategoryFilms(Config.API_KEY, Config.EN_US);
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.body() == null) {
                    return;
                }
                for (CategoryClient item : response.body().getGenres()) {
                    CategoryHelper.create(item.getName(), item.getId());
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
            }
        });
    }

    public void fetchMovies() {
        Call<MoviesResponse> call = TopApp.getApiClient().getTopRatedFilms(Config.API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.body() == null) {
                    return;
                }
                for (MovieClient item : response.body().getListMovies()) {
                    MovieHelper.create(item.getGenreIds(), item.getPosterPath(), item.isAdult(), item.getOverview(), item.getReleaseDate(), item.getId(), item.getOriginalTitle(), item.getOriginalLanguage(),
                            item.getTitle(), item.getBackdropPath(), item.getPopularity(), item.getVoteCount(), item.getVideo(), item.getVoteAverage());
                }

                Intent intent = new Intent(MainActivity.BROADCAST_ACTION_MOVIES);
                intent.putExtra(ACTIONINSERVICE, Config.OK);
                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Intent intent = new Intent(MainActivity.BROADCAST_ACTION_MOVIES);
                intent.putExtra(ACTIONINSERVICE, Config.BAD_REQUEST);
                sendBroadcast(intent);
            }
        });
    }

}
