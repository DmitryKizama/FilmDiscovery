package com.stkizema.test8telemarketing.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private static String INTENT_EXTRA_TITLE = "EXTRAS";
    private static final int RECOVERY_REQUEST = 1;

    private float x1, x2;
    static final int MIN_DISTANCE = 350;

    private List<Video> list;
    private ArrayList<String> idMovies = null;
    private YouTubePlayerView youTubeView;
    private int showVideoId;
    private YouTubePlayer youTubePlayer;
    private LinearLayout ll_counter;
    private Animation scaleGrow;
    private Animation scaleReduce;

    public static Intent getLaunchingIntent(Context ctx, ArrayList<String> list) {
        Intent i = new Intent(ctx, MovieActivity.class);
        i.putStringArrayListExtra(INTENT_EXTRA_TITLE, list);
        return i;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_activity);

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Config.YOU_TUBE_API_KEY, this);

        ll_counter = (LinearLayout) findViewById(R.id.ll_round_counters);

        scaleGrow = AnimationUtils.loadAnimation(this, R.anim.scale_grow);
        scaleReduce = AnimationUtils.loadAnimation(this, R.anim.scale_reduce);

        if (getIntent().hasExtra(INTENT_EXTRA_TITLE)) {
            idMovies = getIntent().getStringArrayListExtra(INTENT_EXTRA_TITLE);
            NUM_THREILERS = idMovies.size();
            if (NUM_THREILERS != 0) {
                showVideoId = 0;
            }
            addViewsToLlCounters(showVideoId);
        }
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        Logger.logd("MOVIETAGSDWDWDS", "onInitializationSuccess 1");
        if (!b) {
            Logger.logd("MOVIETAGSDWDWDS", "onInitializationSuccess 2");
            if (idMovies != null) {
                Logger.logd("MOVIETAGSDWDWDS", "onInitializationSuccess 3");
                if (!idMovies.isEmpty()) {
                    Logger.logd("MOVIETAGSDWDWDS", "onInitializationSuccess 4");
                    this.youTubePlayer = youTubePlayer;
                    youTubePlayer.cueVideo(idMovies.get(showVideoId));
                } else {
                    youTubeView.setVisibility(View.GONE);
                }
            } else {
                youTubeView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Logger.logd("MOVIETAGSDWDWDS", "onInitializationFailure 1");
        if (youTubeInitializationResult.isUserRecoverableError()) {
            Logger.logd("MOVIETAGSDWDWDS", "onInitializationFailure if");
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            Logger.logd("MOVIETAGSDWDWDS", "onInitializationFailure else");
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
//                        Toast.makeText(this, "Left to Right swipe [Previous]", Toast.LENGTH_SHORT).show();
                        nextPreviousTrailer(false);
                    }

                    // Right to left swipe action
                    else {
//                        Toast.makeText(this, "Right to Left swipe [Next]", Toast.LENGTH_SHORT).show();
                        nextPreviousTrailer(true);
                    }

                } else {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void nextPreviousTrailer(boolean next) {
        if (NUM_THREILERS == 0) {
            Toast.makeText(this, "No any trailers", Toast.LENGTH_SHORT).show();
            return;
        }
        if (next) {
            if (showVideoId == NUM_THREILERS - 1) {
                Toast.makeText(this, "Last one", Toast.LENGTH_SHORT).show();
                return;
            }
            setLlCountersPrevious(showVideoId);
            showVideoId++;
            youTubePlayer.cueVideo(idMovies.get(showVideoId));
        } else {
            if (showVideoId == 0) {
                Toast.makeText(this, "First one", Toast.LENGTH_SHORT).show();
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
