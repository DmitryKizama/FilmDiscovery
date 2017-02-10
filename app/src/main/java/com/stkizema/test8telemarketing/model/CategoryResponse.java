package com.stkizema.test8telemarketing.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse {

    @SerializedName("genres")
    private List<CategoryClient> genres;

    public List<CategoryClient> getGenres() {
        return genres;
    }
}
