package com.stkizema.test8telemarketing.model.category;

import com.google.gson.annotations.SerializedName;
import com.stkizema.test8telemarketing.util.Config;

public class CategoryClient {

    @SerializedName(Config.NAME)
    private String name;

    @SerializedName(Config.ID_CATEGORY)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
