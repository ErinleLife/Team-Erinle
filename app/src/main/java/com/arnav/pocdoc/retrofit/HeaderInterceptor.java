package com.arnav.pocdoc.retrofit;

import android.content.Context;

import com.arnav.pocdoc.base.BaseApplication;
import com.arnav.pocdoc.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {

    public HeaderInterceptor(Context context) {
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder()
                .header("Authorization", BaseApplication.preferences.getString(Constants.auth_token))
                .method(original.method(), original.body());
        Request request = builder.build();
        return chain.proceed(request);
    }
}