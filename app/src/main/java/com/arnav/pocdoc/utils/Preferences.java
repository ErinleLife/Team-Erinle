package com.arnav.pocdoc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.arnav.pocdoc.data.model.register.DataRegistration;
import com.arnav.pocdoc.data.model.register.ResponseRegistration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Preferences {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public Preferences(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public Integer getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void putInt(String key, Integer value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    public String getUserId() {
        return getString(Constants.user_id);
    }

    public void storeUserDetails(ResponseRegistration response) {
        putString(Constants.auth_token, response.getToken());
        putString(Constants.user_id, "" + response.getData().getId());
        putString(Constants.user_gson, new Gson().toJson(response));
    }

    public DataRegistration getUserResponse() {
        return new GsonBuilder().create().fromJson(getString(Constants.user_gson),
                DataRegistration.class);
    }

    public void clear() {
//        String fcm_registration_id = getString(Constants.fcm_registration_id);
        editor.clear().apply();
//        putString(Constants.fcm_registration_id, fcm_registration_id);
    }
}