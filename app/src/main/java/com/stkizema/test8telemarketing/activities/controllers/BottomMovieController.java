package com.stkizema.test8telemarketing.activities.controllers;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stkizema.test8telemarketing.R;
import com.stkizema.test8telemarketing.adapters.ActorsAdapter;
import com.stkizema.test8telemarketing.events.CreditByMovieIdEvent;
import com.stkizema.test8telemarketing.services.FetchMovieDetailsApi;
import com.stkizema.test8telemarketing.services.OnBeginFetchListener;
import com.stkizema.test8telemarketing.util.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class BottomMovieController implements OnBeginFetchListener {

    public static final String TAG = "BottomMovieControllerTag";

    private Context context;
    private View parent;
    private Integer movieId;

    private ActorsAdapter adapter;

    private FetchMovieDetailsApi fetch;

    @BindView(R.id.img_movie)
    protected ImageView imgMovie;

    @BindView(R.id.tv_desc_movie)
    protected TextView tvDesc;

    @BindView(R.id.tv_name)
    protected TextView tvTitle;

    @BindView(R.id.rv_actors)
    protected RecyclerView rvActors;

    public BottomMovieController(View parent, final Context context, Integer movieId) {
        this.parent = parent;
        this.context = context;
        this.movieId = movieId;
        onCreate();
    }

    public void onResume() {
        EventBus.getDefault().register(this);
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    private void onCreate() {
        ButterKnife.bind(this, parent);
        fetch = new FetchMovieDetailsApi(context, this);
        fetch.fetchCreditsByMovieId(movieId);

        rvActors.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        adapter = new ActorsAdapter(context);
        rvActors.setAdapter(adapter);

        //TODO: fetch movie details

        //TODO: parse all that shit to text view...
    }

    public void onEvent(CreditByMovieIdEvent event) {
        Logger.logd(TAG, "onEvent = " + event.getId());
        adapter.setList(event.getListCast());
    }

    @Override
    public void onBeginFetch() {

    }
}
