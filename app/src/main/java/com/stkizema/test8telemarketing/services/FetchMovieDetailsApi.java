package com.stkizema.test8telemarketing.services;

import android.content.Context;
import android.os.Handler;

import com.stkizema.test8telemarketing.TopApp;
import com.stkizema.test8telemarketing.activities.controllers.BottomMovieController;
import com.stkizema.test8telemarketing.events.CreditByMovieIdEvent;
import com.stkizema.test8telemarketing.model.credit.actors.CreditResponse;
import com.stkizema.test8telemarketing.util.Config;
import com.stkizema.test8telemarketing.util.Logger;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchMovieDetailsApi {

    private static final int POST_DELAY = 100;

    private Context context;
    private OnBeginFetchListener listener;

    public FetchMovieDetailsApi(Context context, OnBeginFetchListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void fetchCreditsByMovieId(final Integer id) {
        Logger.logd(BottomMovieController.TAG, "fetchCreditsByMovieId");
        final Call<CreditResponse> call = TopApp.getApiClient().getCreditByMovieId(id, Config.API_KEY);

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                call.enqueue(new Callback<CreditResponse>() {
                    @Override
                    public void onResponse(Call<CreditResponse> call, Response<CreditResponse> response) {
                        if (response.body() == null) {
                            Logger.logd("body = null");
                            return;
                        }
                        Logger.logd(BottomMovieController.TAG, "Ok");
                        EventBus.getDefault().post(new CreditByMovieIdEvent(response.body().getResultsCast(), response.body().getResultsCrew(), response.body().getId()));
                    }

                    @Override
                    public void onFailure(Call<CreditResponse> call, Throwable t) {
                        Logger.logd(BottomMovieController.TAG, "fail t = " + t.toString());
//                        EventBus.getDefault().post();
                    }
                });
            }
        }, POST_DELAY);
    }

}
