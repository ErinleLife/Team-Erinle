package com.arnav.pocdoc.retrofit;

import android.util.Log;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class NetworkRequest {

    private static final String TAG = "NetworkRequest";

    private static final Action1<Throwable> mOnError = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            Log.e(TAG, throwable.getMessage());
            throwable.printStackTrace();
        }
    };

    public static <T> Subscription performAsyncRequest(Observable<T> observable, Action1<? super T> onAction) {
        return performAsyncRequest(observable, onAction, mOnError);
    }

    public static <T> Subscription performAsyncRequest(Observable<T> observable, Action1<? super T> onAction, Action1<Throwable> onError) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onAction, onError);
    }

}
