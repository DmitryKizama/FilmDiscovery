package com.stkizema.test8telemarketing.events;

import com.stkizema.test8telemarketing.db.model.Movie;

import java.util.List;

public class MovieEvent {

    private List<Movie> list;
    private String msg;
    private int totalPages;
    private int currentPage;

    public MovieEvent(List<Movie> list, String msg, int totalPages, int currentPage){
        this.list = list;
        this.currentPage = currentPage;
        this.msg = msg;
        this.totalPages = totalPages;
    }

    public List<Movie> getList() {
        return list;
    }

    public String getMsg() {
        return msg;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
