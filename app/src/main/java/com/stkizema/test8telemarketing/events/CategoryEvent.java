package com.stkizema.test8telemarketing.events;

import com.stkizema.test8telemarketing.db.model.Category;

import java.util.List;

public class CategoryEvent {

    private List<Category> list;

    public CategoryEvent(List<Category> list) {
        this.list = list;
    }

    public List<Category> getList() {
        return list;
    }
}
