package com.arnav.pocdoc.data.network;

import android.app.Activity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RedirectInterceptor implements Interceptor {

    private final Activity context;

    public RedirectInterceptor(Activity context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request newRequest = chain.request().newBuilder()
                .build();
        Response response = chain.proceed(newRequest);
        if (response.code() == 401) {
//            Intent intent = new Intent(context, LoginActivity.class);
//            context.startActivity(intent);
//            ((Activity) context).finish();
            return response;
        }
        return chain.proceed(newRequest);
    }
}