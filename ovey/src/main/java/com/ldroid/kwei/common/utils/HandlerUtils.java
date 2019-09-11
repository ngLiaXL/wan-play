package com.ldroid.kwei.common.utils;

import android.os.Handler;
import android.os.Looper;

import com.ldroid.kwei.common.SimpleRxJava2;
import com.ldroid.kwei.common.callback.SimpleCallback;

/**
 * Created by ngliaxl on 2018/4/10.
 */
public class HandlerUtils {


    public static final Handler HANDLER = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(Runnable runnable) {
        HANDLER.post(runnable);
    }

    public static void runOnUiThreadDelay(Runnable runnable, long delayMillis) {
        HANDLER.postDelayed(runnable, delayMillis);
    }

    public static void removeRunable(Runnable runnable) {
        HANDLER.removeCallbacks(runnable);
    }

    public static boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void converToMainThread(final SimpleCallback<Void> callback) {
        if (HandlerUtils.isInMainThread()) {
            callback.onCallback(null);
        } else {
            HandlerUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callback.onCallback(null);
                }
            });
        }
    }

    public static void converToWorkerThread(final SimpleCallback<Void> callback) {
        if (HandlerUtils.isInMainThread()) {
            SimpleRxJava2.getFromFuture(new SimpleRxJava2.FutureCallback<Boolean>() {
                @Override
                public Boolean call() {
                    callback.onCallback(null);
                    return Boolean.TRUE;
                }
            });
        } else {
            callback.onCallback(null);
        }
    }
}
