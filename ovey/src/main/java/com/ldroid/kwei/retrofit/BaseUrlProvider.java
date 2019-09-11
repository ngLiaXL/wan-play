package com.ldroid.kwei.retrofit;


import io.reactivex.annotations.NonNull;

public class BaseUrlProvider {

    @NonNull
    private static BaseUrlFactory sUrlFactory;

    private BaseUrlProvider() {
    }


    public static void setUrlFactory(BaseUrlFactory urlFactory) {
        sUrlFactory = urlFactory;
    }

    public static BaseUrlFactory getUrlFactory() {
        if (sUrlFactory == null) throw new RuntimeException("Url Factory can not be null");
        return sUrlFactory;
    }

    public static void put(String key, String value) {
        sUrlFactory.put(key, value);
    }

    public static String remove(String key) {
        return sUrlFactory.remove(key);
    }

}
