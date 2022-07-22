package com.arnav.pocdoc.data.network;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.arnav.pocdoc.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public static Retrofit getClient(Activity activity) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.readTimeout(60, TimeUnit.SECONDS);
        client.connectTimeout(60, TimeUnit.SECONDS);
        client.addInterceptor(new HeaderInterceptor(activity));

        //TODO - FOR LIVE APP
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
            client.addInterceptor(loggingInterceptor);
        }
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client.build())
                .build();
    }

    public static String USER_AGENT = "jubielee1503";
    public static String APP_SECRET = "jubielee1503";
    public static String VERSION_NAME = BuildConfig.VERSION_NAME;
    public static String DEVICE_TYPE_ANDROID = "android";
    public static String CONTENT_TYPE = "application/json";
    public static String baseUrl = "http://165.22.45.58/";

    /**
     * MULTIPLE IMAGE UPLOAD WITH MULTIPART
     */
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";

    public static RequestBody createRequestBody(@NonNull String s) {
        return RequestBody.create(s, MediaType.parse(MULTIPART_FORM_DATA));
    }

    public static RequestBody createRequestBody(@NonNull File file) {
        return RequestBody.create(file, MediaType.parse(MULTIPART_FORM_DATA));
    }

    public static String USER_PROFILE = baseUrl + "storage/users-avatar/";
}