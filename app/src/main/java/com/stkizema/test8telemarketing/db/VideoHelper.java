package com.stkizema.test8telemarketing.db;

import android.content.Context;

import com.stkizema.test8telemarketing.TopApp;
import com.stkizema.test8telemarketing.db.model.DaoSession;
import com.stkizema.test8telemarketing.db.model.Video;
import com.stkizema.test8telemarketing.db.model.VideoDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;

public class VideoHelper {

    private static VideoHelper instance;
    private static DaoSession daoSession;

    private VideoHelper(Context context) {
        daoSession = TopApp.getDaoSession();
    }

    public static synchronized VideoHelper getInstance(Context context) {
        if (instance == null) {
            instance = new VideoHelper(context);
        }

        return instance;
    }

    public static Video create(Integer idMovie, String id, String iso_639_1,
                               String iso_3166_1, String key, String name, String site, Integer size,
                               String type) {
        Video video = getVideoById(id);
        if (video != null) {
            update(video, id, iso_639_1, iso_3166_1, key, name, site, size, type);
            return video;
        }
        video = new Video(idMovie, id, iso_639_1, iso_3166_1, key, name, site, size, type);
        getVideoDao().insert(video);
        return video;
    }

    public static Video getVideoById(String id) {
        Query<Video> query = getVideoDao().queryBuilder().where(VideoDao.Properties.Id.eq(id)).build();
        if (query.list().isEmpty()) {
            return null;
        }
        return query.list().get(0);
    }

    public static List<Video> getVideosByMovieId(Integer movieId) {
        Query<Video> query = getVideoDao().queryBuilder().where(VideoDao.Properties.IdMovie.eq(movieId)).build();
        if (query.list().isEmpty()) {
            return null;
        }
        return query.list();
    }

    public static VideoDao getVideoDao() {
        return daoSession.getVideoDao();
    }

    private static void update(Video video, String id, String iso_639_1,
                               String iso_3166_1, String key, String name, String site, Integer size,
                               String type) {
        video.setId(id);
        video.setIso_639_1(iso_639_1);
        video.setIso_3166_1(iso_3166_1);
        video.setKey(key);
        video.setType(type);
        video.setSize(size);
        video.setSite(site);
        video.setName(name);
        getVideoDao().update(video);
    }

}
