package com.ngliaxl.play

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.ldroid.kwei.UseCase
import com.ldroid.kwei.UseCaseHandler
import com.ldroid.kwei.exception.ResponeThrowable
import com.ngliaxl.play.network.usecase.ArticlesUseCase

class TestActivity : BaseActivity() {

    val handler = UseCaseHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }


    private fun result(response: ArticlesUseCase.ResponseValue?) {
        findViewById<TextView>(R.id.result).text = response.toString()
    }

    fun onClickArticle(view: View) {
        val request = ArticlesUseCase.RequestValues(0)
        if (!request.checkInput()) {
            return
        }
        handler.execute(ArticlesUseCase(), request, object : UseCase.UseCaseCallback<ArticlesUseCase.ResponseValue> {
            override fun onSuccess(response: ArticlesUseCase.ResponseValue?) {
                result(response)
            }

            override fun onError(exception: ResponeThrowable?) {
            }
        })

    }

}