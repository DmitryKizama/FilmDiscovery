package com.stkizema.test8telemarketing.events;

import com.stkizema.test8telemarketing.model.credit.actors.CreditCastClient;
import com.stkizema.test8telemarketing.model.credit.actors.CreditCrewClient;

import java.util.List;

public class CreditByMovieIdEvent {

    private List<CreditCastClient> listCast;
    private List<CreditCrewClient> listCrew;
    private Integer id;

    public CreditByMovieIdEvent(List<CreditCastClient> listCast, List<CreditCrewClient> listCrew, Integer id) {
        this.id = id;
        this.listCrew = listCrew;
        this.listCast = listCast;
    }

    public List<CreditCastClient> getListCast() {
        return listCast;
    }

    public List<CreditCrewClient> getListCrew() {
        return listCrew;
    }

    public Integer getId() {
        return id;
    }
}
