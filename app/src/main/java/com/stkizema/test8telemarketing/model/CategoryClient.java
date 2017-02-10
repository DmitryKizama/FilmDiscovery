package com.stkizema.test8telemarketing.model;

import com.google.gson.annotations.SerializedName;

public class CategoryClient {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
