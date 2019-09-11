package com.ldroid.kwei.retrofit;




import java.util.HashMap;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public abstract class BaseUrlFactory extends HashMap<String, String> {

    @Nullable
    public abstract String getUrlHeaderName();

    @NonNull
    public abstract String getBaseUrl();
}
