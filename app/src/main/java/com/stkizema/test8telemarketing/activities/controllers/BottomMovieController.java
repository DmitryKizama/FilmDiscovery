package com.stkizema.test8telemarketing.activities.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stkizema.test8telemarketing.R;

import butterknife.BindView;

public class BottomMovieController {

    private Context context;
    private View parent;
    private Integer movieId;

    @BindView(R.id.img_movie)
    private ImageView imgMovie;

    @BindView(R.id.tv_desc_movie)
    private TextView tvDesc;

    @BindView(R.id.tv_name)
    private TextView tvTitle;

    @BindView(R.id.rv_actors)
    private RecyclerView rvActors;

    public BottomMovieController(View parent, final Context context, Integer movieId) {
        this.parent = parent;
        this.context = context;
        this.movieId = movieId;
        onCreate();
    }

    private void onCreate(){
        //TODO: fetch movie credits

        //TODO: fetch movie details

        //TODO: parse all that shit to text view...
    }

}
