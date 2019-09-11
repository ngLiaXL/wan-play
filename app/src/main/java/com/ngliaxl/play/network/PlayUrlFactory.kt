package com.ngliaxl.play.network

import com.ldroid.kwei.retrofit.BaseUrlFactory


class PlayUrlFactory : BaseUrlFactory() {

    interface UrlKeys {
        companion object {
            val WEB = "pos_web"
            val RETAIL = "retail"
        }
    }

    init {
        put(UrlKeys.WEB, Env.get().baseUrl)
        put(UrlKeys.RETAIL, Env.get().resUrl)
    }

    override fun getUrlHeaderName(): String {
        return MAIN_DOMAIN
    }


    override fun getBaseUrl(): String {
        return Env.get().baseUrl
    }

    companion object {
        val MAIN_DOMAIN = "pos-domain"
    }

}
