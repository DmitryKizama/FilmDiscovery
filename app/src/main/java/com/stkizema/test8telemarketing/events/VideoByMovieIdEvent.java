package com.stkizema.test8telemarketing.events;

import com.stkizema.test8telemarketing.db.model.Video;

import java.util.List;

public class VideoByMovieIdEvent {

    private List<Video> list;
    private Integer idMovie;

    public VideoByMovieIdEvent(List<Video> list, Integer idMovie){
        this.list = list;
        this.idMovie = idMovie;
    }

    public List<Video> getList() {
        return list;
    }

    public Integer getIdMovie() {
        return idMovie;
    }
}
