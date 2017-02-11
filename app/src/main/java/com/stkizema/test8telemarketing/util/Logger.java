package com.stkizema.test8telemarketing.util;

import android.util.Log;

import com.stkizema.test8telemarketing.BuildConfig;

public class Logger{

    private static String currentTag = "CT";

    public static void setCurrentTag(String tag){
        currentTag = tag;
    }

    public static void logd(String tag, String msg){
        if (showLog()) {
            Log.d(tag, msg);
        }
    }

    public static void logd(String msg){
        if (showLog()) {
            Log.d(currentTag, msg);
        }
    }

    public static void loge(String tag, String msg){
        if (showLog()) {
            Log.e(tag, msg);
        }
    }

    public static void loge(String msg){
        if (showLog()) {
            Log.e(currentTag, msg);
        }
    }

    private static boolean showLog(){
        return BuildConfig.DEBUG;
    }
}
