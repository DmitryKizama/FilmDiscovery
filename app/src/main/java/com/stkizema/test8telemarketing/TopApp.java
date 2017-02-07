package com.stkizema.test8telemarketing;

import android.app.Application;
import android.content.Context;

import com.stkizema.test8telemarketing.api.FilmApiInterface;
import com.stkizema.test8telemarketing.db.MovieHelper;
import com.stkizema.test8telemarketing.db.model.DaoMaster;
import com.stkizema.test8telemarketing.db.model.DaoSession;

import org.greenrobot.greendao.database.Database;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TopApp extends Application {

    private static FilmApiInterface filmApiInterface;
    private static String API_BASE_URL = "https://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;
    private static DaoSession daoSession;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "model", null);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        MovieHelper.getInstance(this);

        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        filmApiInterface = retrofit.create(FilmApiInterface.class);
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static Context getContext() {
        return context;
    }

    public static FilmApiInterface getApiClient() {
        return filmApiInterface;
    }
}
