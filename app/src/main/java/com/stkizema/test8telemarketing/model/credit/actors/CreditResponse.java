package com.stkizema.test8telemarketing.model.credit.actors;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreditResponse {

    @SerializedName("id")
    private Integer id;

    @SerializedName("cast")
    private List<CreditCastClient> resultsCast;

    @SerializedName("crew")
    private List<CreditCrewClient> resultsCrew;

    public List<CreditCrewClient> getResultsCrew() {
        return resultsCrew;
    }

    public List<CreditCastClient> getResultsCast() {
        return resultsCast;
    }

    public Integer getId() {
        return id;
    }

}
