package com.arnav.pocdoc.data.network;

import android.app.Activity;
import android.os.Build;

import com.arnav.pocdoc.utils.Preferences;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {

    private final Preferences preference;
    public Activity activity;

    public HeaderInterceptor(Activity activity) {
        preference = new Preferences(activity);
        this.activity = activity;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder()
                .header("User-Agent", APIClient.USER_AGENT)
                .header("App-Secret", APIClient.APP_SECRET)
                .header("App-Track-Version", APIClient.VERSION_NAME)
                .header("App-Device-Type", APIClient.DEVICE_TYPE_ANDROID)
//                .header("App-Store-Version", BuildConfig.VERSION_NAME)
//                .header("App-Device-Model", Utils.getDeviceName())
                .header("App-Os-Version", "" + Build.VERSION.SDK_INT)
//                .header("App-Store-Build-Number", "" + BuildConfig.VERSION_CODE)
                .header("Content-Type", APIClient.CONTENT_TYPE)
//                .header("Auth-Token", preference.getString(Constants.auth_token))
                .method(original.method(), original.body());
        Request request = builder.build();
        Response response = chain.proceed(request);
        if (response.code() == 403) {
//            logOutUser(activity);
            return response;
        }
        return response;
    }

//    private void logOutUser(Activity activity) {
//        activity.runOnUiThread(() -> {
//            Utils.makeToast(activity.getResources().getString(R.string.auth_token_expired));
//            FirebaseAuth.getInstance().signOut();
//            LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent(ChatHelper.BROADCAST_LOGOUT));
//            BaseApplication.preference.clear();
//            Intent intent = new Intent(activity, SignInActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            activity.startActivity(intent);
//        });
//    }
}