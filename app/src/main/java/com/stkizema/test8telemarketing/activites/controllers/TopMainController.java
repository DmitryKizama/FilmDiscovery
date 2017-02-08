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

public class TopMainController {
    public static String TAG = "TopMainControllerLog";
    private View parent;
    private Context context;
    private AutoCompleteTextView tv;
    private TextView tvHint;
    private ImageView btnMenu, btnNextSearch;
    private boolean searchMovie = false;

    private static final String[] COUNTRIES = new String[]{
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

    public void keyboardOpen() {
//        tvHint.clearAnimation();
//        tvHint.setVisibility(View.GONE);
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

    public TopMainController(View parent, final Context context) {
        this.parent = parent;
        this.context = context;
        tv = (AutoCompleteTextView) parent.findViewById(R.id.auto_tv);
        btnMenu = (ImageView) parent.findViewById(R.id.id_menu);
        btnNextSearch = (ImageView) parent.findViewById(R.id.id_next);
        tvHint = (TextView) parent.findViewById(R.id.tv_hint);
        tvHint.setText("Search by category");
        tv.setFocusableInTouchMode(false);
        btnNextSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateTextView();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        tv.setAdapter(adapter);
        tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                tv.setFocusableInTouchMode(true);
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
