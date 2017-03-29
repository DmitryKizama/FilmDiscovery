package com.stkizema.test8telemarketing.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class UiHelper {
    public static final int HIDE_KEYBOARD_TIME = 200;

    private static int height;
    private static int width;
    private static DisplayMetrics metrics;

    private static final int WIDTH_HD = 1080;
    private static final int HEIGHT_HD = 1920;
    private static Context appContext;

    public static int keyboardHeight;

    public static void init(Context c) {
        appContext = c;

        height = ((WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        width = ((WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();

        metrics = appContext.getResources().getDisplayMetrics();

        keyboardHeight = getPixel(215);
    }

    public static int getW() {
        return width;
    }

    public static int getH() {
        return height;
    }

    public static int getRelativeW() {
        return width / WIDTH_HD;
    }

    public static int getRelativeH() {
        return height / HEIGHT_HD;
    }

    public static int getPixel(float dpi) {
        return (int) (metrics.density * dpi);
    }

    public static float getPixelF(float dpi) {
        return metrics.density * dpi;
    }

    public static float getPixelFHacked(float dpi) {
        if (metrics.density <= 1) {
            return dpi;
        }

        if (metrics.density <= 2) {
            return 2 * dpi;
        }

        if (metrics.density <= 3) {
            return 3 * dpi;
        }

        return 4 * dpi;
    }


    public static int getDPI(int px) {
        return (int) (px / metrics.density);
    }

}
