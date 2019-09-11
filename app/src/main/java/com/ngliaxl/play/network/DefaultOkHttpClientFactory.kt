package com.ngliaxl.play.network

import com.ldroid.kwei.App
import com.ldroid.kwei.cookie.PersistentCookieJar
import com.ldroid.kwei.cookie.cache.SetCookieCache
import com.ldroid.kwei.cookie.persistence.SharedPrefsCookiePersistor
import com.ldroid.kwei.interceptor.BaseUrlInterceptor
import com.ldroid.kwei.interceptor.HttpLoggingInterceptor
import com.ldroid.kwei.retrofit.BaseUrlProvider
import com.ldroid.kwei.retrofit.OkHttpClientFactory
import okhttp3.OkHttpClient

import java.util.concurrent.TimeUnit


class DefaultOkHttpClientFactory : OkHttpClientFactory {

    override fun createNewNetworkModuleClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(60000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .writeTimeout(60000, TimeUnit.MILLISECONDS)
            .addInterceptor(BaseUrlInterceptor(BaseUrlProvider.getUrlFactory()))
            .cookieJar(PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(App.INSTANCE))).build()
    }
}
