package com.stkizema.test8telemarketing.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse {

    @SerializedName("id")
    private Integer id;

    @SerializedName("results")
    private List<VideoClient> results;

    public List<VideoClient> getResults() {
        return results;
    }

    public Integer getId() {
        return id;
    }
}
