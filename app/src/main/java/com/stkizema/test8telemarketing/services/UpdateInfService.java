package com.stkizema.test8telemarketing.services;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.stkizema.test8telemarketing.R;
import com.stkizema.test8telemarketing.TopApp;
import com.stkizema.test8telemarketing.activites.MainActivity;
import com.stkizema.test8telemarketing.db.CategoryHelper;
import com.stkizema.test8telemarketing.db.MovieHelper;
import com.stkizema.test8telemarketing.db.model.Category;
import com.stkizema.test8telemarketing.model.CategoryClient;
import com.stkizema.test8telemarketing.model.CategoryResponse;
import com.stkizema.test8telemarketing.model.MovieClient;
import com.stkizema.test8telemarketing.model.MoviesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateInfService extends android.app.Service {

    private final IBinder iBinder = new LocalBinder();
    private static String API_KEY;
    public static final String ACTIONINSERVICE = "ACTIONINSERVICE";

    public class LocalBinder extends Binder {
        public UpdateInfService getService() {
            return UpdateInfService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        API_KEY = TopApp.getContext().getResources().getString(R.string.api_key);
        return iBinder;
    }

    public void refresh() {
        waitForWifi();
    }

    private void waitForWifi() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    int i = 0;
                    while (!wifi.isConnected()) {
                        Thread.sleep(500);
                        i++;
                        if (i == 10) {
                            //Connection ERROR!
                            Intent intent = new Intent(MainActivity.BROADCAST_ACTION_MOVIES);
                            intent.putExtra(ACTIONINSERVICE, 408);
                            sendBroadcast(intent);
                            return;
                        }
                    }
                    makeCallForRatedMovies();
                } catch (Exception e) {

                }
            }
        };
        t.start();
    }

    public void makeCallForCategores() {
        Log.d("SERVICEPROBLMS", "make call");
        Call<CategoryResponse> call = TopApp.getApiClient().getCategoryFilms(API_KEY, "en-US");
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.body() == null) {
                    Log.d("SERVICEPROBLMS", "null body");
                    return;
                }
                for (CategoryClient item : response.body().getGenres()) {
                    CategoryHelper.create(item.getName(), item.getId());

                }
                List<Category> list = CategoryHelper.getAllCategory();
                int i = 0;
                for (Category c : list) {
                    Log.d("SERVICEPROBLMS", "Category number " + i + ") =  " + c.getName());
                    i++;
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Log.d("SERVICEPROBLMS", "fail");
            }
        });
    }

    public void makeCallForRatedMovies() {
        Log.d("SERVICEPROBLMS", "make call");
        Call<MoviesResponse> call = TopApp.getApiClient().getTopRatedFilms(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.body() == null) {
                    Log.d("SERVICEPROBLMS", "null body");
                    return;
                }
                for (MovieClient item : response.body().getListMovies()) {
                    MovieHelper.create(item.getPosterPath(), item.isAdult(), item.getOverview(), item.getReleaseDate(), item.getId(), item.getOriginalTitle(), item.getOriginalLanguage(),
                            item.getTitle(), item.getBackdropPath(), item.getPopularity(), item.getVoteCount(), item.getVideo(), item.getVoteAverage());
                }

                Intent intent = new Intent(MainActivity.BROADCAST_ACTION_MOVIES);
                intent.putExtra(ACTIONINSERVICE, 200);
                sendBroadcast(intent);
//                List<Movie> listMovies = response.body().getTopRatedListMovies();
//                rvAdapterMain.setList(listMovies);
//                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.d("SERVICEPROBLMS", "fail");
                Intent intent = new Intent(MainActivity.BROADCAST_ACTION_MOVIES);
                intent.putExtra(ACTIONINSERVICE, 400);
                sendBroadcast(intent);
            }
        });
    }

}
