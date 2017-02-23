package com.stkizema.test8telemarketing.activites.controllers;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.stkizema.test8telemarketing.R;
import com.stkizema.test8telemarketing.db.CategoryHelper;
import com.stkizema.test8telemarketing.db.MovieHelper;
import com.stkizema.test8telemarketing.db.model.Category;
import com.stkizema.test8telemarketing.db.model.Movie;
import com.stkizema.test8telemarketing.services.FetchApi;
import com.stkizema.test8telemarketing.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class TopMainController {

    public static final String TAG = "TopMainControllerLog";

    private View parent;
    private Context context;
    private AutoCompleteTextView tvAutocomplete;
    private ImageView btnMenu, btnNextSearch, btnDelete;
    private boolean searchMovie;
    private ArrayAdapter<String> adapter;

    private HintHelper hintHelper;
    private FetchApi fetchApi;

    public TopMainController(View parent, final Context context, FetchApi fetchApi) {
        this.parent = parent;
        this.context = context;
        this.fetchApi = fetchApi;
        onCreate();
    }

    public void keyboardOpen() {
    }

    public void keyboardClose() {
//        tvAutocomplete.setFocusable(false);
//        tvAutocomplete.setFocusableInTouchMode(false);
    }

    private void setContent(boolean isMovie) {
        List<Movie> listMov = MovieHelper.getTopRatedListMovies();
        List<Category> listCat = CategoryHelper.getAllCategory();
        if (listMov == null || listCat == null) {
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
        if (isMovie) {
            adapter.addAll(listMovies);
        } else {
            adapter.addAll(listCategories);
        }
        adapter.notifyDataSetChanged();
//        adapter.notifyDataSetInvalidated();

    }

    private void onCreate() {
        tvAutocomplete = (AutoCompleteTextView) parent.findViewById(R.id.auto_tv);
        btnDelete = (ImageView) parent.findViewById(R.id.id_delete);
        btnMenu = (ImageView) parent.findViewById(R.id.id_menu);
        btnNextSearch = (ImageView) parent.findViewById(R.id.id_next);
        TextView tvHint = (TextView) parent.findViewById(R.id.tv_hint);

        adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line);
        tvAutocomplete.setAdapter(adapter);

        hintHelper = new HintHelper(tvHint);

        setContent(false);
        btnNextVisibility(true);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvAutocomplete.setText("");
                btnNextVisibility(true);
            }
        });
        btnNextSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvAutocomplete.dismissDropDown();
                animateTextView();
            }
        });

        searchMovie = false;
//        tvAutocomplete.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                tvAutocomplete.setFocusableInTouchMode(true);
//                return false;
//            }
//        });

        tvAutocomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.logd("onClick");
                if (!searchMovie) {
                    Logger.logd("false");
                    tvAutocomplete.showDropDown();
                }
            }
        });

        tvAutocomplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hintHelper.setHintVisibility(tvAutocomplete.getText().toString().length() > 0);
                btnNextVisibility(!(tvAutocomplete.getText().toString().length() > 0));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        tvAutocomplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String text = tvAutocomplete.getText().toString();
                    if (searchMovie) {
                        fetchApi.fetchMovieById(text);
                    } else {
                        fetchApi.fetchMoviesByCategory(text);
                    }
                    tvAutocomplete.clearFocus();
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    return true;
                }
                return true;
            }
        });
    }

    private void btnNextVisibility(boolean isBtnNextVisible) {
        if (isBtnNextVisible) {
            btnDelete.setVisibility(View.GONE);
            btnNextSearch.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.VISIBLE);
            btnNextSearch.setVisibility(View.GONE);
        }
    }

    private void animateTextView() {
        if (!tvAutocomplete.getText().toString().equals("")) {
            return;
        }

        searchMovie = !searchMovie;
        setContent(searchMovie);

        hintHelper.runHintAnimation();
    }

    private class HintHelper {
        private TextView tvHint;

        private Animation animation_left;
        private Animation animation_right;

        public HintHelper(TextView tvHint) {
            this.tvHint = tvHint;

            tvHint.setText("Search by category");
            animation_right = AnimationUtils.loadAnimation(context, R.anim.move_right);
            animation_left = AnimationUtils.loadAnimation(context, R.anim.move_left);
        }

        private void setHintVisibility(boolean isTvEmpty) {
            tvHint.clearAnimation();

            if (isTvEmpty) {
                tvHint.setVisibility(View.GONE);
            } else {
                tvHint.setVisibility(View.VISIBLE);
            }
        }

        public void runHintAnimation() {
            tvHint.startAnimation(animation_right);
            animation_right.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!searchMovie) {
                        tvHint.setText("Search by category");
                    } else {
                        tvHint.setText("Search for movie");
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

}
