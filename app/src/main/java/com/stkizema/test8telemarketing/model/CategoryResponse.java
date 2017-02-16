package com.stkizema.test8telemarketing.model;

import com.google.gson.annotations.SerializedName;
import com.stkizema.test8telemarketing.util.Config;

import java.util.List;

public class CategoryResponse {

    @SerializedName(Config.GENRES)
    private List<CategoryClient> genres;

    public List<CategoryClient> getGenres() {
        return genres;
    }
}
