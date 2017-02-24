package com.stkizema.test8telemarketing.services;

import com.stkizema.test8telemarketing.db.model.Category;
import com.stkizema.test8telemarketing.db.model.Movie;

import java.util.List;

public interface OnResponseListener {
    void onResponseMovies(List<Movie> list, String msg);
    void onResponseCategory(List<Category> list);
    void onBeginFetch();
}
