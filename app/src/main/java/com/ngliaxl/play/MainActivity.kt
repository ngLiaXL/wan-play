package com.ngliaxl.play

import android.os.Bundle

/**
 * Retrofit
 * OkHttp
 * RxJava
 * Gson
 *
 */
class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startAnimActivity(TestActivity::class.java)
    }


}
