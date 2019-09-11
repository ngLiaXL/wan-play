package com.ngliaxl.play.network


import com.ngliaxl.play.BuildConfig

enum class Env(val baseUrl: String, val resUrl: String) {

    debug(
        "https://www.wanandroid.com/",
        "https://www.wanandroid.com/"
    ),

    release(
        "https://www.wanandroid.com/",
        "https://www.wanandroid.com/"
    );

    companion object {
        private val env: Env = valueOf(BuildConfig.BUILD_TYPE)
        fun get(): Env {
            return env
        }
    }
}