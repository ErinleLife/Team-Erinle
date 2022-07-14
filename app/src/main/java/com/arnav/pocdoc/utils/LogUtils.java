package com.arnav.pocdoc.utils;

import android.util.Log;

import com.arnav.pocdoc.BuildConfig;

public class LogUtils {
    public static void Print(String tag, String text) {
        //TODO - FOR LIVE APP
        if (BuildConfig.DEBUG)
            Log.e(tag, "==========" + text);
    }
}