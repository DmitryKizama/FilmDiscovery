package com.stkizema.test8telemarketing.db;

import android.content.Context;
import android.util.Log;

import com.stkizema.test8telemarketing.TopApp;
import com.stkizema.test8telemarketing.db.model.DaoSession;
import com.stkizema.test8telemarketing.db.model.MovieDb;
import com.stkizema.test8telemarketing.db.model.MovieDbDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;

public class MovieHelper {
    private static MovieHelper instance;
    private static DaoSession daoSession;

    private MovieHelper(Context context) {
        daoSession = TopApp.getDaoSession();
    }

    public static synchronized MovieHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MovieHelper(context);
        }

        return instance;
    }

    public static MovieDb create(String posterPath, boolean adult, String overview, String releaseDate, Integer id,
                                 String originalTitle, String originalLanguage, String title, String backdropPath, Double popularity,
                                 Integer voteCount, Boolean video, Double voteAverage) {
        MovieDb movieDb = getProductById(id);
        if (movieDb != null) {
            return movieDb;
        }
        movieDb = new MovieDb(posterPath, adult, overview, releaseDate, id,
                originalTitle, originalLanguage, title, backdropPath, popularity,
                voteCount, video, voteAverage);
        getMovieDao().insert(movieDb);
        return movieDb;
    }

    public static MovieDb getProductById(Integer id) {
        Query<MovieDb> query = getMovieDao().queryBuilder().where(MovieDbDao.Properties.Id.eq(id)).build();
        if (query.list().isEmpty()) {
            return null;
        }
        return query.list().get(0);
    }

    public static List<MovieDb> getListMovies() {
        Log.d("SERVICEPROBLMS", "get list");
        Query<MovieDb> query = getMovieDao().queryBuilder().orderAsc(MovieDbDao.Properties.IdMovie).build();
        if (query.list().isEmpty()) {
            return null;
        }
        return query.list();
    }

    public static MovieDbDao getMovieDao() {
        return daoSession.getMovieDbDao();
    }
}
