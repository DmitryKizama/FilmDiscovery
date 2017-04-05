package com.stkizema.test8telemarketing.model.credit.actors;

import com.google.gson.annotations.SerializedName;

public class CreditCastClient {

    @SerializedName("cast_id")
    private Integer cast_id;

    @SerializedName("character")
    private String character;

    @SerializedName("credit_id")
    private String credit_id;

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("order")
    private Integer order;

    @SerializedName("profile_path")
    private String profile_path;

    public Integer getCast_id() {
        return cast_id;
    }

    public String getCharacter() {
        return character;
    }

    public String getCredit_id() {
        return credit_id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getOrder() {
        return order;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setCast_id(Integer cast_id) {
        this.cast_id = cast_id;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public void setCredit_id(String credit_id) {
        this.credit_id = credit_id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}
