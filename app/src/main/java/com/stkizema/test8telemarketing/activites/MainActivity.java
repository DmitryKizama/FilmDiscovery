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
import com.stkizema.test8telemarketing.adapters.RVAdapterMain;
import com.stkizema.test8telemarketing.db.MovieHelper;
import com.stkizema.test8telemarketing.db.model.Movie;
import com.stkizema.test8telemarketing.services.UpdateInfService;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {
    public static final String BROADCAST_ACTION_MOVIES = "BROADCASTACTION";

    private UpdateInfService updateInfService;
    private boolean upBound = false;
    private RecyclerView rvMain;
    private TextView tvNoItems;
    private RVAdapterMain rvAdapterMain;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BroadcastReceiver broadcastReceiver;
    private TopMainController topMainController;
    private FrameLayout frameTopLayout;
    private RelativeLayout rootLayout;

    private ServiceConnection upConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            updateInfService = ((UpdateInfService.LocalBinder) iBinder).getService();
            upBound = true;
            Log.d("SERVICEPROBLMS", "on service connected, make call");
            updateInfService.makeCallForRatedMovies();
            updateInfService.makeCallForCategores();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            upBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService(new Intent(this, UpdateInfService.class), upConnection, BIND_AUTO_CREATE);
        rootLayout = (RelativeLayout) findViewById(R.id.root_layout);
        rvMain = (RecyclerView) findViewById(R.id.rv_main);
        rvAdapterMain = new RVAdapterMain(this, null);
        rvMain.setHasFixedSize(true);
        rvMain.setAdapter(rvAdapterMain);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        rvMain.setLayoutManager(gridLayoutManager);

        tvNoItems = (TextView) findViewById(R.id.tv_no_items);
        rvVisible(true);
        broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                List<Movie> list = MovieHelper.getTopRatedListMovies();
                rvVisible(true);
                switch (intent.getIntExtra(UpdateInfService.ACTIONINSERVICE, 0)) {
                    case 200: //OK
                        Toast.makeText(MainActivity.this, "Good!", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                        rvAdapterMain.setList(list);
                        break;
                    case 408: //REQUEST TIMEOUT
                        Toast.makeText(MainActivity.this, "Request Timeout", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                        if (list == null) {
                            rvVisible(false);
                            return;
                        }
                        if (list.isEmpty()) {
                            rvVisible(false);
                            return;
                        }
                        rvAdapterMain.setList(list);
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
                        rvAdapterMain.setList(list);
                        break;
                    case 0: // ERROR
                        Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        };
        registerBroadcast();


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sw_refresh_layout_main);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (upBound) {
                    updateInfService.refresh();
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
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
        if (upBound) {
            unbindService(upConnection);
            upBound = false;
        }
    }
}
