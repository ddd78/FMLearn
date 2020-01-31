package com.a78.com.fmlearn.utils;

import android.util.Log;

/**
 * Created by home on 2020/1/16.
 */

public class LogUtil {

    public static String sTAG = "LogUtil";

    public static  boolean sIsRelease = false;

    public static void init (String baseTag, boolean isRealease){
        sTAG = baseTag;
        sIsRelease = isRealease;
    }

    public static void d(String TAG, String content) {
        if (!sIsRelease) {
            Log.d("[" + sTAG + "]" + TAG, content);
        }
    }

    public static void v(String TAG, String content) {
        if (!sIsRelease) {
            Log.d("[" + sTAG + "]" + TAG, content);
        }
    }

    public static void i(String TAG, String content) {
        if (!sIsRelease) {
            Log.d("[" + sTAG + "]" + TAG, content);
        }
    }

    public static void w(String TAG, String content) {
        if (!sIsRelease) {
            Log.d("[" + sTAG + "]" + TAG, content);
        }
    }

    public static void e(String TAG, String content) {
        if (!sIsRelease) {
            Log.d("[" + sTAG + "]" + TAG, content);
        }
    }
}
