package com.stkizema.test8telemarketing.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.stkizema.test8telemarketing.R;
import com.stkizema.test8telemarketing.db.model.Video;
import com.stkizema.test8telemarketing.util.Config;
import com.stkizema.test8telemarketing.util.Logger;
import com.stkizema.test8telemarketing.util.UiHelper;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static int NUM_THREILERS;
    private static String INTENT_EXTRA_LIST = "EXTRAS";
    private static String INTENT_EXTRA_ID_MOVIE = "EXTRAS_ID";
    private static final int RECOVERY_REQUEST = 1;

    private float x1, x2;
    static final int MIN_DISTANCE = 350;

    private List<Video> list;
    private ArrayList<String> idMovies = null;
    private Integer movieId = null;
    private YouTubePlayerView youTubeView;
    private int showVideoId;
    private YouTubePlayer youTubePlayer;
    private LinearLayout ll_counter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNoTrailers;
    private Animation scaleGrow;
    private Animation scaleReduce;

    public static Intent getLaunchingIntent(Context ctx, ArrayList<String> list, Integer movieId) {
        Intent i = new Intent(ctx, MovieActivity.class);
        i.putExtra(INTENT_EXTRA_ID_MOVIE, movieId);
        i.putStringArrayListExtra(INTENT_EXTRA_LIST, list);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_activity);

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                youTubeView.initialize(Config.YOU_TUBE_API_KEY, MovieActivity.this);
            }
        }, 1000);

        View v = youTubeView;
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Logger.logd("EDWDWWEWEWE", "onTouch");
                onTouchEvent(motionEvent);
                return false;
            }
        });

        ll_counter = (LinearLayout) findViewById(R.id.ll_round_counters);

        tvNoTrailers = (TextView) findViewById(R.id.tv_no_trailers);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sw_refresh_layout_movie);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(true);

        scaleGrow = AnimationUtils.loadAnimation(this, R.anim.scale_grow);
        scaleReduce = AnimationUtils.loadAnimation(this, R.anim.scale_reduce);

        youTubeView.setVisibility(View.GONE);
        tvNoTrailers.setVisibility(View.GONE);
        ll_counter.setVisibility(View.GONE);

        if (getIntent() != null) {
            idMovies = getIntent().getStringArrayListExtra(INTENT_EXTRA_LIST);
            NUM_THREILERS = idMovies.size();
            if (NUM_THREILERS != 0) {
                showVideoId = 0;
            }
            if (getIntent().hasExtra(INTENT_EXTRA_ID_MOVIE)) {
                movieId = getIntent().getIntExtra(INTENT_EXTRA_ID_MOVIE, 0);
            }
            addViewsToLlCounters(showVideoId);
        }

    }

    private void setVisibility(boolean isVideoVisible) {
        if (isVideoVisible) {
            ll_counter.setVisibility(View.VISIBLE);
            youTubeView.setVisibility(View.VISIBLE);
            tvNoTrailers.setVisibility(View.GONE);
        } else {
            ll_counter.setVisibility(View.GONE);
            youTubeView.setVisibility(View.GONE);
            tvNoTrailers.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        swipeRefreshLayout.setRefreshing(false);
        if (!b) {
            if (idMovies != null) {
                if (!idMovies.isEmpty()) {
                    setVisibility(true);
                    this.youTubePlayer = youTubePlayer;
                    youTubePlayer.cueVideo(idMovies.get(showVideoId));
                } else {
                    setVisibility(false);
                }
            } else {
                setVisibility(false);
            }
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        swipeRefreshLayout.setRefreshing(false);
        setVisibility(false);
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.YOU_TUBE_API_KEY, this);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (youTubePlayer == null) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // Left to Right swipe action
                    if (x2 > x1) {
                        nextTrailer(false);
                    }

                    // Right to left swipe action
                    else {
                        nextTrailer(true);
                    }

                } else {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void nextTrailer(boolean next) {
        if (NUM_THREILERS == 0) {
            return;
        }
        if (next) {
            if (showVideoId == NUM_THREILERS - 1) {
                return;
            }
            setLlCountersPrevious(showVideoId);
            showVideoId++;
            youTubePlayer.cueVideo(idMovies.get(showVideoId));
        } else {
            if (showVideoId == 0) {
                return;
            }
            setLlCountersPrevious(showVideoId);
            showVideoId--;
            youTubePlayer.cueVideo(idMovies.get(showVideoId));
        }
        setLlCountersNext(showVideoId);
    }

    private void addViewsToLlCounters(int positionBigOne) {
        int padding = UiHelper.getPixel(5);

        for (int i = 0; i < NUM_THREILERS; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setPadding(padding, padding, padding, padding);
            if (i == positionBigOne) {
                imageView.setImageResource(R.drawable.ic_point_big_2);
            } else {
                imageView.setImageResource(R.drawable.ic_point_big);
            }
            ll_counter.addView(imageView);
        }
    }

    private void setLlCountersPrevious(int position) {
        ImageView imageView = (ImageView) ll_counter.getChildAt(position);
        imageView.startAnimation(scaleReduce);
        imageView.setImageResource(R.drawable.ic_point_big);
        ll_counter.childDrawableStateChanged(imageView);
    }

    private void setLlCountersNext(int position) {
        ImageView imageView = (ImageView) ll_counter.getChildAt(position);
        imageView.startAnimation(scaleGrow);
        imageView.setImageResource(R.drawable.ic_point_big_2);
        ll_counter.childDrawableStateChanged(imageView);
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }


}
