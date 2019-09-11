package com.ngliaxl.play

import android.app.Application
import com.ldroid.kwei.retrofit.BaseUrlProvider
import com.ldroid.kwei.retrofit.OkHttpClientProvider
import com.ngliaxl.play.network.DefaultOkHttpClientFactory
import com.ngliaxl.play.network.PlayUrlFactory

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        BaseUrlProvider.setUrlFactory(PlayUrlFactory())
        OkHttpClientProvider.setOkHttpClientFactory(DefaultOkHttpClientFactory())

    }
}