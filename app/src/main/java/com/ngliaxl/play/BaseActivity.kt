package com.ngliaxl.play

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    fun startAnimActivity(intent: Intent) {
        startActivity(intent)
    }

    fun startAnimActivity(cla: Class<*>) {
        startActivity(Intent(this, cla))
    }


}