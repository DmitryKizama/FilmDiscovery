package com.stkizema.test8telemarketing.activites;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stkizema.test8telemarketing.R;
import com.stkizema.test8telemarketing.activites.controllers.TopMainController;
import com.stkizema.test8telemarketing.adapters.AdapterMainMovies;
import com.stkizema.test8telemarketing.db.MovieHelper;
import com.stkizema.test8telemarketing.db.model.Movie;
import com.stkizema.test8telemarketing.services.UpdateInfService;
import com.stkizema.test8telemarketing.util.Logger;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    public static final String BROADCAST_ACTION_MOVIES = "BROADCASTACTION";

    private UpdateInfService updateInfService;
    private RecyclerView rvMain;
    private TextView tvNoItems;
    private AdapterMainMovies adapterMainMovies;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BroadcastReceiver broadcastReceiver;
    private TopMainController topMainController;
    private FrameLayout frameTopLayout;
    private RelativeLayout rootLayout;

    private Bundle savedInstanceState;

    private ServiceConnection upConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            updateInfService = ((UpdateInfService.LocalBinder) iBinder).getService();

            Log.d("SERVICEPROBLMS", "on service connected, make call");
            if (savedInstanceState == null) {
                updateInfService.fetchMovies();
                updateInfService.fetchCategories();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            updateInfService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);
        bindService(new Intent(this, UpdateInfService.class), upConnection, BIND_AUTO_CREATE);

        rootLayout = (RelativeLayout) findViewById(R.id.root_layout);
        rvMain = (RecyclerView) findViewById(R.id.rv_main);
        adapterMainMovies = new AdapterMainMovies(this, null);
        rvMain.setHasFixedSize(true);
        rvMain.setAdapter(adapterMainMovies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false);
        rvMain.setLayoutManager(gridLayoutManager);

        tvNoItems = (TextView) findViewById(R.id.tv_no_items);
        rvVisible(true);
        List<Movie> list = MovieHelper.getTopRatedListMovies();
        adapterMainMovies.setList(list);

        broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                onReceivedMovieBroadcast(intent);
            }
        };

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sw_refresh_layout_main);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Logger.logd("Refresh", "on refresh called");
                if (updateInfService != null) {
                    updateInfService.fetchMovies();
                }
            }
        });
        frameTopLayout = (FrameLayout) findViewById(R.id.frame_main);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_top_controller, frameTopLayout, false);
        frameTopLayout.removeAllViews();
        frameTopLayout.addView(view);
        topMainController = new TopMainController(frameTopLayout, this);

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

    private void registerBroadcast() {
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION_MOVIES);
        registerReceiver(broadcastReceiver, intFilt);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadcast();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateInfService != null) {
            unbindService(upConnection);
            updateInfService = null;
        }
    }

    private void onReceivedMovieBroadcast(Intent intent){
        List<Movie> list = MovieHelper.getTopRatedListMovies();
        rvVisible(true);

        switch (intent.getIntExtra(UpdateInfService.ACTIONINSERVICE, 0)) {
            case 200: //OK
                Logger.logd("NETWORK", "Good");
                swipeRefreshLayout.setRefreshing(false);
                adapterMainMovies.setList(list);
                break;
            case 400: //NO NETWORK
                Toast.makeText(MainActivity.this, "No network", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
                if (list == null) {
                    rvVisible(false);
                    return;
                }
                if (list.isEmpty()) {
                    rvVisible(false);
                    return;
                }
                adapterMainMovies.setList(list);
                break;
            case 0: // ERROR
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }
}
