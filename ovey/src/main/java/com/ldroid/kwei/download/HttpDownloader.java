package com.ldroid.kwei.download;


import com.ldroid.kwei.interceptor.ProgressResponseInterceptor;
import com.ldroid.kwei.progress.ProgressListener;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.reactivex.annotations.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;


public class HttpDownloader implements Downloader{

    public HttpDownloader() {
    }

    @Override
    public File download(@NonNull final Downloader.FileParam file) throws IOException {
        return execute(new HttpClientCallback<File>() {
            @Override
            public File doTransfer(OkHttpClient http) throws IOException {
                if (retrieveFile(http, file)) {
                    return file.localFile;
                } else {
                }
                return null;
            }
        });
    }

    @Override
    public List<File> download(List<FileParam> files) throws IOException {
        // TODO
        return null;
    }

    private boolean retrieveFile(OkHttpClient http, final Downloader.FileParam file) {
        if (file == null || file.localFile == null) {
            return false;
        }
        boolean success = false;
        Request request = new Request.Builder().url(file.remote).build();
        try {
            Response response = http.newCall(request).execute();
            BufferedSink sink = Okio.buffer(Okio.sink(file.localFile));
            sink.writeAll(response.body().source());
            sink.close();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }


    public <T> T execute(HttpClientCallback<T> callback) throws IOException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new ProgressResponseInterceptor(new ProgressListener() {
            @Override
            public void onProgress(long bytesWritten, long contentLength, boolean done) {
                // nothing to do
            }
        }));
        return callback.doTransfer(builder.build());
    }


    interface HttpClientCallback<T> {
        T doTransfer(OkHttpClient http) throws IOException;
    }
}
