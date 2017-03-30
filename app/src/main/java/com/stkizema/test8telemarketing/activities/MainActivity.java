package com.stkizema.test8telemarketing.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.stkizema.test8telemarketing.R;
import com.stkizema.test8telemarketing.activities.controllers.TopMainController;
import com.stkizema.test8telemarketing.adapters.MoviesAdapter;
import com.stkizema.test8telemarketing.db.model.Category;
import com.stkizema.test8telemarketing.db.model.Movie;
import com.stkizema.test8telemarketing.db.model.Video;
import com.stkizema.test8telemarketing.events.CategoryEvent;
import com.stkizema.test8telemarketing.events.MovieEvent;
import com.stkizema.test8telemarketing.events.VideoByMovieIdEvent;
import com.stkizema.test8telemarketing.services.FetchApi;
import com.stkizema.test8telemarketing.services.OnResponseListener;
import com.stkizema.test8telemarketing.util.Config;
import com.stkizema.test8telemarketing.util.Logger;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements OnResponseListener, MoviesAdapter.ItemListener {

    private static final int COLUMN_NUMBER = 1;
    private static int REFRESH_VALUE_PAGE = 1;
    private int totalPages;

    private FetchApi fetchApi;
    private RecyclerView rvMain;
    private TextView tvNoItems;
    private MoviesAdapter moviesAdapter;
    private TopMainController topMainController;
    private FrameLayout frameTopLayout;
    private SwipyRefreshLayout swipyRefreshLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.main_activity);

        rvMain = (RecyclerView) findViewById(R.id.rv_main);

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.progress_bar_swipe);

        frameTopLayout = (FrameLayout) findViewById(R.id.frame_main);

        moviesAdapter = new MoviesAdapter(this, this, null);
        rvMain.setHasFixedSize(true);
        rvMain.setAdapter(moviesAdapter);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, COLUMN_NUMBER, LinearLayoutManager.VERTICAL, false);
        rvMain.setLayoutManager(gridLayoutManager);

        tvNoItems = (TextView) findViewById(R.id.tv_no_items);
        fetchApi = new FetchApi(this, this);

        rvVisible(true);

        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (fetchApi != null) {
                    fetchApi.refresh(1);
                }
            }
        });

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                Logger.logd("BOOLSHITTAG", "onRefresh");
                if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                    if (REFRESH_VALUE_PAGE <= totalPages) {
                        if (fetchApi != null) {
                            fetchApi.refresh(++REFRESH_VALUE_PAGE);
                        }
                    } else {
                        swipyRefreshLayout.setRefreshing(false);
                    }
                } else {
                    if (fetchApi != null) {
                        fetchApi.refresh(1);
                    }
                    Logger.logd("WATAFUCKA", "TOP");
                }
            }
        });

        View view = LayoutInflater.from(this).inflate(R.layout.layout_top_controller, frameTopLayout, false);

        frameTopLayout.removeAllViews();
        frameTopLayout.addView(view);
        topMainController = new TopMainController(frameTopLayout, this, fetchApi);

        fetchApi.fetchCategories();

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if (isOpen) {
                    topMainController.keyboardOpen();
                } else {
                    topMainController.keyboardClose();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        REFRESH_VALUE_PAGE = 1;
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        topMainController.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBeginFetch() {
        //TODO: DISABLE SCREEN
        Logger.logd("BOOLSHITTAG", "onBeginFetch");
        if (swipeRefreshLayout.isEnabled()){
            swipeRefreshLayout.setRefreshing(true);
        }
        swipyRefreshLayout.setRefreshing(true);
    }

    private void rvVisible(boolean rvVisible) {
        if (rvVisible) {
            tvNoItems.setVisibility(GONE);
            rvMain.setVisibility(VISIBLE);
            swipyRefreshLayout.setVisibility(VISIBLE);
            swipeRefreshLayout.setEnabled(false);
        } else {
            tvNoItems.setVisibility(VISIBLE);
            rvMain.setVisibility(GONE);
            swipyRefreshLayout.setVisibility(GONE);
            swipeRefreshLayout.setEnabled(true);
        }
    }

    public void onEvent(VideoByMovieIdEvent event) {
        onResponseVideo(event.getList(), event.getIdMovie());
    }

    public void onEvent(CategoryEvent event) {
        onResponseCategory(event.getList());
    }

    public void onEvent(MovieEvent event) {
        onResponseMovies(event.getList(), event.getMsg(), event.getTotalPages(), event.getCurrentPage());
    }

    public void onResponseMovies(List<Movie> list, String msg, int totalPages, int currentPage) {
        swipyRefreshLayout.setRefreshing(false);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (list == null) {
            rvVisible(false);
            return;
        }
        if (list.isEmpty()) {
            rvVisible(false);
            return;
        }
        if (!msg.equals(Config.OK)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
        this.totalPages = totalPages;
        rvVisible(true);
        if (currentPage <= 1) {
            moviesAdapter.setList(list);
        } else {
            moviesAdapter.addList(list);
        }
    }

    public void onResponseCategory(List<Category> list) {
        fetchApi.fetchTopRatedMovies(REFRESH_VALUE_PAGE);
    }

    public void onResponseVideo(List<Video> list, Integer movieId) {
        swipyRefreshLayout.setRefreshing(false);
        if (list == null) {
            Toast.makeText(this, "Downloading error", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<String> listTrailers = new ArrayList<>();
        for (Video video : list) {
            if (video.getSite().equals("YouTube")) {
                listTrailers.add(video.getKey());
            }
        }
        startActivity(MovieActivity.getLaunchingIntent(this, listTrailers, movieId));
    }


    @Override
    public void onItemClick(Movie movie) {
        fetchApi.fetchVideoByMovieId(movie.getId());
    }

}
