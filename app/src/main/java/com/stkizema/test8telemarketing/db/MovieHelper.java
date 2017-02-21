package com.stkizema.test8telemarketing.db;

import android.content.Context;

import com.stkizema.test8telemarketing.TopApp;
import com.stkizema.test8telemarketing.db.model.DaoSession;
import com.stkizema.test8telemarketing.db.model.Movie;
import com.stkizema.test8telemarketing.db.model.MovieDao;
import com.stkizema.test8telemarketing.util.Logger;

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

    public static Movie create(List<Integer> listIdCategories, String posterPath, boolean adult, String overview, String releaseDate, Integer id,
                               String originalTitle, String originalLanguage, String title, String backdropPath, Double popularity,
                               Integer voteCount, Boolean video, Double voteAverage) {
        Movie movie = getMovieById(id);
        if (movie != null) {
            updateMovie(movie, posterPath, adult, overview, releaseDate,
                    originalTitle, originalLanguage, title, backdropPath, popularity,
                    voteCount, video, voteAverage);
            return movie;
        }
        for (Integer i : listIdCategories) {
            LinkHelper.createLink(id, i);
        }
        movie = new Movie(posterPath, adult, overview, releaseDate, id,
                originalTitle, originalLanguage, title, backdropPath, popularity,
                voteCount, video, voteAverage);
        getMovieDao().insert(movie);
        return movie;
    }

    public static Movie getMovieById(Integer id) {
        Query<Movie> query = getMovieDao().queryBuilder().where(MovieDao.Properties.Id.eq(id)).build();
        if (query.list().isEmpty()) {
            return null;
        }
        return query.list().get(0);
    }

    public static Movie getMovieByName(String name) {
        Query<Movie> query = getMovieDao().queryBuilder().where(MovieDao.Properties.Title.eq(name)).build();
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

    public static List<Movie> getMoviesByCategoryId(Integer id) {
        return LinkHelper.getMoviesByCategoryId(id);
    }

    public static MovieDao getMovieDao() {
        return daoSession.getMovieDao();
    }

    private static void updateMovie(Movie movie, String posterPath, boolean adult, String overview, String releaseDate,
                                    String originalTitle, String originalLanguage, String title, String backdropPath, Double popularity,
                                    Integer voteCount, Boolean video, Double voteAverage) {
        movie.setPosterPath(posterPath);
        movie.setAdult(adult);
        movie.setOverview(overview);
        movie.setReleaseDate(releaseDate);
        movie.setOriginalTitle(originalTitle);
        movie.setOriginalLanguage(originalLanguage);
        movie.setTitle(title);
        movie.setBackdropPath(backdropPath);
        movie.setPopularity(popularity);
        movie.setVoteCount(voteCount);
        movie.setVideo(video);
        movie.setVoteAverage(voteAverage);
        getMovieDao().update(movie);
    }
}
