package com.stkizema.test8telemarketing.db;

import android.content.Context;

import com.stkizema.test8telemarketing.TopApp;
import com.stkizema.test8telemarketing.db.model.Category;
import com.stkizema.test8telemarketing.db.model.CategoryDao;
import com.stkizema.test8telemarketing.db.model.DaoSession;
import com.stkizema.test8telemarketing.db.model.Movie;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

public class CategoryHelper {

    private static CategoryHelper instance;
    private static DaoSession daoSession;

    private CategoryHelper(Context context) {
        daoSession = TopApp.getDaoSession();
    }

    public static synchronized CategoryHelper getInstance(Context context) {
        if (instance == null) {
            instance = new CategoryHelper(context);
        }

        return instance;
    }

    public static Category create(String name, Integer id) {
        Category category = getCategoryById(id);
        if (category != null) {
            updateCategory(category, name);
            return category;
        }
        category = new Category();
        category.setId(id);
        category.setName(name);
        getCategoryDao().insert(category);
        return category;
    }

    public static List<Category> getCategoriesByString(String str) {
        Query<Category> query = getCategoryDao().queryBuilder().orderAsc(CategoryDao.Properties.IdCategory).build();
        List<Category> list = query.list();
        if (list.isEmpty()) {
            return null;
        }
        List<Category> resultList = new ArrayList<>();
        for (Category cat : list) {
            if (cat.getName().toLowerCase().startsWith(str.toLowerCase())) {
                resultList.add(cat);
            }
        }
        return resultList;
    }

    public static Category getCategoryById(Integer id) {
        Query<Category> query = getCategoryDao().queryBuilder().where(CategoryDao.Properties.Id.eq(id)).build();
        if (query.list().isEmpty()) {
            return null;
        }
        return query.list().get(0);
    }

    public static Category getCategoryByName(String name) {
        Query<Category> query = getCategoryDao().queryBuilder().where(CategoryDao.Properties.Name.eq(name)).build();
        if (query.list().isEmpty()) {
            return null;
        }
        return query.list().get(0);
    }

    public static List<Category> getAllCategory() {
        Query<Category> query = getCategoryDao().queryBuilder().orderAsc(CategoryDao.Properties.IdCategory).build();
        if (query.list().isEmpty()) {
            return null;
        }
        return query.list();
    }

    public static CategoryDao getCategoryDao() {
        return daoSession.getCategoryDao();
    }

    public static List<Category> getCategoriesByMovieId(Integer id) {
        return LinkHelper.getCategoriesByMovieId(id);
    }

    private static void updateCategory(Category cat, String name) {
        cat.setName(name);
        getCategoryDao().update(cat);
    }
}
