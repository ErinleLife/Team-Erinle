package com.arnav.pocdoc.base;

import android.app.Application;

import com.arnav.pocdoc.utils.Preferences;

public class BaseApplication extends Application {

    public static Preferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = new Preferences(this);
    }
}
