package com.stkizema.test8telemarketing.activites.controllers;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.stkizema.test8telemarketing.R;
import com.stkizema.test8telemarketing.db.CategoryHelper;
import com.stkizema.test8telemarketing.db.MovieHelper;
import com.stkizema.test8telemarketing.db.model.Category;
import com.stkizema.test8telemarketing.db.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class TopMainController {
    public static String TAG = "TopMainControllerLog";
    private View parent;
    private Context context;
    private AutoCompleteTextView tv;
    private TextView tvHint;
    private ImageView btnMenu, btnNextSearch;
    private boolean searchMovie = false;
    private ArrayAdapter<String> adapter;

    public TopMainController(View parent, final Context context) {
        this.parent = parent;
        this.context = context;
        onCreate();
    }

    public void keyboardOpen() {
    }

    public void keyboardClose() {
        if (tv.getText().toString().equals("")) {
            tvHint.setVisibility(View.VISIBLE);
        } else {
            tvHint.setVisibility(View.GONE);
        }
        tv.setFocusable(false);
        tv.setFocusableInTouchMode(false);
    }

    private void setLists(boolean smovie) {

        List<Movie> listMov = MovieHelper.getTopRatedListMovies();
        List<Category> listCat = CategoryHelper.getAllCategory();
        if (listMov == null || listCat == null) {
            Log.d("SERVICEPROBLMS", "list movies null ot list categories null");
            return;
        }
        List<String> listMovies = new ArrayList<>();
        for (Movie movie : listMov) {
            listMovies.add(movie.getTitle());
        }
        List<String> listCategories = new ArrayList<>();
        for (Category cat : listCat) {
            listCategories.add(cat.getName());
        }
        adapter.clear();
        if (smovie) {
            adapter.addAll(listMovies);
        } else {
            adapter.addAll(listCategories);
        }

    }

    private void onCreate() {
        tv = (AutoCompleteTextView) parent.findViewById(R.id.auto_tv);
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line);
        tv.setAdapter(adapter);
        tv.setFocusableInTouchMode(false);

        btnMenu = (ImageView) parent.findViewById(R.id.id_menu);

        tvHint = (TextView) parent.findViewById(R.id.tv_hint);
        tvHint.setText("Search by category");

        btnNextSearch = (ImageView) parent.findViewById(R.id.id_next);
        setLists(false);

        btnNextSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateTextView();
            }
        });

        tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                tv.setFocusableInTouchMode(true);
                if (!searchMovie) {
                    tv.showDropDown();

                }
                return false;
            }
        });

        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvHint.clearAnimation();
                if (tv.getText().toString().length() > 0) {
                    tvHint.setVisibility(View.GONE);
                } else {
                    tvHint.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.d(TAG, "onEditorAction");
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d(TAG, "Search");
                    return true;
                }
                return true;
            }
        });
    }

    private void animateTextView() {
        if (!tv.getText().toString().equals("")) {
            return;
        }
        setLists(!searchMovie);
        Animation animation_right = AnimationUtils.loadAnimation(context, R.anim.move_right);
        final Animation animation_left = AnimationUtils.loadAnimation(context, R.anim.move_left);
        tvHint.startAnimation(animation_right);
        animation_right.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (searchMovie) {
                    tvHint.setText("Search by category");
                    searchMovie = false;
                } else {
                    tvHint.setText("Search for movie");
                    searchMovie = true;
                }
                tvHint.startAnimation(animation_left);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.d(TAG, "repeat animation");
            }
        });
    }


}
