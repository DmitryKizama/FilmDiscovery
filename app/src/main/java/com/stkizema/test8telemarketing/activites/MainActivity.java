package com.stkizema.test8telemarketing.activites;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stkizema.test8telemarketing.R;
import com.stkizema.test8telemarketing.activites.controllers.TopMainController;
import com.stkizema.test8telemarketing.adapters.MoviesAdapter;
import com.stkizema.test8telemarketing.db.model.Category;
import com.stkizema.test8telemarketing.db.model.Movie;
import com.stkizema.test8telemarketing.services.FetchApi;
import com.stkizema.test8telemarketing.services.OnResponseListener;
import com.stkizema.test8telemarketing.util.Config;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements OnResponseListener {

    private static final int COLUMN_NUMBER = 1;

    private FetchApi fetchApi;
    private RecyclerView rvMain;
    private TextView tvNoItems;
    private MoviesAdapter moviesAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TopMainController topMainController;
    private FrameLayout frameTopLayout;
    private RelativeLayout rootLayout;

    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);

        rootLayout = (RelativeLayout) findViewById(R.id.root_layout);
        rvMain = (RecyclerView) findViewById(R.id.rv_main);
        moviesAdapter = new MoviesAdapter(this, null);
        rvMain.setHasFixedSize(true);
        rvMain.setAdapter(moviesAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, COLUMN_NUMBER, LinearLayoutManager.VERTICAL, false);
        rvMain.setLayoutManager(gridLayoutManager);

        tvNoItems = (TextView) findViewById(R.id.tv_no_items);
        fetchApi = new FetchApi(this, this);
        fetchApi.fetchCategories();

        rvVisible(false);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sw_refresh_layout_main);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (fetchApi != null) {
                    fetchApi.refresh();
                }
            }
        });
        frameTopLayout = (FrameLayout) findViewById(R.id.frame_main);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_top_controller, frameTopLayout, false);
        frameTopLayout.removeAllViews();
        frameTopLayout.addView(view);
        topMainController = new TopMainController(frameTopLayout, this, fetchApi);

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

    private void rvVisible(boolean rvVisible) {
        if (rvVisible) {
            tvNoItems.setVisibility(GONE);
            rvMain.setVisibility(VISIBLE);
        } else {
            tvNoItems.setVisibility(VISIBLE);
            rvMain.setVisibility(GONE);
        }
    }

    @Override
    public void onResponseMovies(List<Movie> list, String msg) {
        swipeRefreshLayout.setRefreshing(false);
        if (list == null) {
            rvVisible(false);
            return;
        }
        if (list.isEmpty()){
            rvVisible(false);
            return;
        }
        if (!msg.equals(Config.OK)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
        rvVisible(true);
        moviesAdapter.setList(list);
    }

    @Override
    public void onResponseCategory(List<Category> list) {
        fetchApi.fetchTopRatedMovies();
    }
}
