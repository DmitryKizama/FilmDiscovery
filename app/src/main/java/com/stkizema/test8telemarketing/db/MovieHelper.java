package com.stkizema.test8telemarketing.db;

import android.content.Context;

import com.stkizema.test8telemarketing.TopApp;
import com.stkizema.test8telemarketing.db.model.DaoSession;
import com.stkizema.test8telemarketing.db.model.Movie;
import com.stkizema.test8telemarketing.db.model.MovieDao;

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

    public static Movie create(String posterPath, boolean adult, String overview, String releaseDate, Integer id,
                               String originalTitle, String originalLanguage, String title, String backdropPath, Double popularity,
                               Integer voteCount, Boolean video, Double voteAverage) {
        Movie movie = getProductById(id);
        if (movie != null) {
            return movie;
        }
        movie = new Movie(posterPath, adult, overview, releaseDate, id,
                originalTitle, originalLanguage, title, backdropPath, popularity,
                voteCount, video, voteAverage);
        getMovieDao().insert(movie);
        return movie;
    }

    public static Movie getProductById(Integer id) {
        Query<Movie> query = getMovieDao().queryBuilder().where(MovieDao.Properties.Id.eq(id)).build();
        if (query.list().isEmpty()) {
            return null;
        }
        return query.list().get(0);
    }

    public static List<Movie> getTopRatedListMovies() {
        Query<Movie> query = getMovieDao().queryBuilder().orderAsc(MovieDao.Properties.IdMovie).build();
        if (query.list().isEmpty()) {
            return null;
        }
        return query.list();
    }

    public static MovieDao getMovieDao() {
        return daoSession.getMovieDao();
    }
}
