package com.ldroid.kwei.interceptor;

import com.ldroid.kwei.progress.ProgressListener;
import com.ldroid.kwei.progress.ProgressResponseBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;


public class ProgressResponseInterceptor implements Interceptor {

    private ProgressListener mProgressListener;

    public ProgressResponseInterceptor(ProgressListener progressListener) {
        mProgressListener = progressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body(), mProgressListener))
                .build();
    }
}
