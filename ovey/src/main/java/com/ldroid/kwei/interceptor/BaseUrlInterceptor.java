package com.ldroid.kwei.interceptor;

import android.text.TextUtils;

import com.ldroid.kwei.retrofit.BaseUrlFactory;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class BaseUrlInterceptor implements Interceptor {

    private final BaseUrlFactory urlFactory;
    private final String urlHeaderName;

    public BaseUrlInterceptor(BaseUrlFactory urlFactory) {
        urlHeaderName = urlFactory.getUrlHeaderName();
        this.urlFactory = urlFactory;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(processRequest(chain.request()));
    }

    private Request processRequest(Request request) {
        Request.Builder newBuilder = request.newBuilder();
        HttpUrl httpUrl;
        String headerName = obtainUrlHeaderNameFromHeaders(request);
        if (!TextUtils.isEmpty(headerName)) {
            httpUrl = getUrl(headerName);
            newBuilder.removeHeader(urlHeaderName);
        } else {
            httpUrl = getBaseUrl();
        }
        if (null != httpUrl) {
            HttpUrl newUrl = parseUrl(httpUrl, request.url());
            return newBuilder
                    .url(newUrl)
                    .build();
        }
        return newBuilder.build();
    }


    private HttpUrl parseUrl(HttpUrl headerUrl, HttpUrl url) {
        if (null == headerUrl) return url;
        return url.newBuilder()
                .scheme(headerUrl.scheme())
                .host(headerUrl.host())
                .port(headerUrl.port())
                .build();
    }


    private String obtainUrlHeaderNameFromHeaders(Request request) {
        if(urlHeaderName == null){
            return null ;
        }
        List<String> headers = request.headers(urlHeaderName);
        if (headers == null || headers.size() == 0)
            return null;
        if (headers.size() > 1)
            throw new IllegalArgumentException("Only one Url-Key in the headers");
        return request.header(urlHeaderName);
    }


    public HttpUrl getUrl(String key) {
        return Utils.checkUrl(urlFactory.get(key));
    }

    public HttpUrl getBaseUrl() {
        return Utils.checkUrl(urlFactory.getBaseUrl());
    }
}
