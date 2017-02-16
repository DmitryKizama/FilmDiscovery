package com.stkizema.test8telemarketing.model;

import com.google.gson.annotations.SerializedName;
import com.stkizema.test8telemarketing.util.Config;

import java.util.List;

public class MoviesResponse {
    @SerializedName(Config.PAGE)
    private int page;
    @SerializedName(Config.RESULTS)
    private List<MovieClient> results;
    @SerializedName(Config.TOTAL_RESULTS)
    private int totalResults;
    @SerializedName(Config.TOTAL_PAGES)
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieClient> getListMovies() {
        return results;
    }

    public void setResults(List<MovieClient> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
