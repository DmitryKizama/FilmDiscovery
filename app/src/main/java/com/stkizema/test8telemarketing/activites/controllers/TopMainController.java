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

import java.util.ArrayList;
import java.util.List;

public class TopMainController {

    public static final String TAG = "TopMainControllerLog";

    private View parent;
    private Context context;
    private AutoCompleteTextView tvAutocomplete;
    private ImageView btnMenu, btnNextSearch, btnDelete, btnSearch, imgSearch;
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
        btnDelete = (ImageView) parent.findViewById(R.id.img_delete);
        btnMenu = (ImageView) parent.findViewById(R.id.id_menu);
        btnNextSearch = (ImageView) parent.findViewById(R.id.id_next);
        btnSearch = (ImageView) parent.findViewById(R.id.id_search);
        imgSearch = (ImageView) parent.findViewById(R.id.img_search);
        TextView tvHint = (TextView) parent.findViewById(R.id.tv_hint);

        adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line);
        tvAutocomplete.setAdapter(adapter);

        hintHelper = new HintHelper(tvHint);

        setContent(false);
        btnNextVisibility(true);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(view);
            }
        });
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
        tvAutocomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!searchMovie) {
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
                    search(textView);
                    return true;
                }
                return true;
            }
        });
    }

    private void search(View textView) {
        String text = tvAutocomplete.getText().toString();
        if (searchMovie) {
            fetchApi.fetchMovieByName(text, 1);
        } else {
            fetchApi.fetchMoviesByCategory(text, 1);
        }
        tvAutocomplete.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
    }

    private void btnNextVisibility(boolean isBtnNextVisible) {
        if (isBtnNextVisible) {
            btnDelete.setVisibility(View.GONE);
            btnNextSearch.setVisibility(View.VISIBLE);
            imgSearch.setVisibility(View.VISIBLE);
            btnSearch.setVisibility(View.GONE);
        } else {
            btnDelete.setVisibility(View.VISIBLE);
            btnNextSearch.setVisibility(View.GONE);
            imgSearch.setVisibility(View.GONE);
            btnSearch.setVisibility(View.VISIBLE);
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
