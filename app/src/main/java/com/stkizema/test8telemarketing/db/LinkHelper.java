package com.stkizema.test8telemarketing.db;

import android.content.Context;

import com.stkizema.test8telemarketing.TopApp;
import com.stkizema.test8telemarketing.db.model.Category;
import com.stkizema.test8telemarketing.db.model.DaoSession;
import com.stkizema.test8telemarketing.db.model.LinkMovieCategory;
import com.stkizema.test8telemarketing.db.model.LinkMovieCategoryDao;
import com.stkizema.test8telemarketing.db.model.Movie;
import com.stkizema.test8telemarketing.util.Logger;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

public class LinkHelper {

    private static LinkHelper instance;
    private static DaoSession daoSession;

    private LinkHelper(Context context) {
        daoSession = TopApp.getDaoSession();
    }

    public static synchronized LinkHelper getInstance(Context context) {
        if (instance == null) {
            instance = new LinkHelper(context);
        }
        return instance;
    }

    static LinkMovieCategory createLink(Integer idMovie, Integer idCategory) {
        LinkMovieCategory link = new LinkMovieCategory();
        link.setIdCategory(idCategory);
        link.setIdMovie(idMovie);
        getLinkDao().save(link);
        return link;
    }

    static List<Category> getCategoriesByMovieId(Integer id) {
        List<LinkMovieCategory> listLinks = getListLinksByMovieId(id);
        if (listLinks == null) {
            return null;
        }
        List<Category> list = new ArrayList<>();
        for (LinkMovieCategory i : listLinks) {
            list.add(CategoryHelper.getCategoryById(i.getIdCategory()));
        }
        return list;
    }

    static List<Movie> getMoviesByCategoryId(Integer id) {
        List<LinkMovieCategory> listLinks = getListLinksByCategoryId(id);
        if (listLinks == null) {
            return null;
        }
        List<Movie> list = new ArrayList<>();
        for (LinkMovieCategory i : listLinks) {
            list.add(MovieHelper.getMovieById(i.getIdMovie()));
        }
        return list;
    }

    private static List<LinkMovieCategory> getListLinksByMovieId(Integer id) {
        Query<LinkMovieCategory> queryLinkMovie = getLinkDao().queryBuilder().where(LinkMovieCategoryDao.Properties.IdMovie.eq(id)).build();
        if (queryLinkMovie.list().isEmpty()) {
            return null;
        }
        return queryLinkMovie.list();
    }

    private static List<LinkMovieCategory> getListLinksByCategoryId(Integer id) {
        Query<LinkMovieCategory> queryLinkMovie = getLinkDao().queryBuilder().where(LinkMovieCategoryDao.Properties.IdCategory.eq(id)).build();
        if (queryLinkMovie.list().isEmpty()) {
            return null;
        }
        return queryLinkMovie.list();
    }

    static LinkMovieCategoryDao getLinkDao() {
        return daoSession.getLinkMovieCategoryDao();
    }

}
