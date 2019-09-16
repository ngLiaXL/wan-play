package com.ngliaxl.play

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.ldroid.kwei.UseCase
import com.ldroid.kwei.UseCaseHandler
import com.ldroid.kwei.exception.ResponeThrowable
import com.ngliaxl.play.network.usecase.ArticlesUseCase
import com.ngliaxl.play.network.usecase.BannersUseCase

class TestActivity : BaseActivity() {


    val handler = UseCaseHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }


    private fun result(response: UseCase.ResponseValue?) {
        findViewById<TextView>(R.id.result).text = response.toString()
    }

    fun onClickArticle(view: View) {
        val request = ArticlesUseCase.HomeArticlesUseCase.RequestValues(0)
        if (!request.checkInput()) {
            return
        }
        handler.execute(
            ArticlesUseCase.HomeArticlesUseCase(),
            request,
            object : UseCase.UseCaseCallback<ArticlesUseCase.HomeArticlesUseCase.ResponseValue> {
                override fun onSuccess(response: ArticlesUseCase.HomeArticlesUseCase.ResponseValue?) {
                    result(response)
                }

                override fun onError(exception: ResponeThrowable?) {
                }
            })

    }

    fun onClickBanner(view: View) {
        val request = BannersUseCase.RequestValues()
        if (!request.checkInput()) {
            return
        }
        handler.execute(BannersUseCase(), request, object : UseCase.UseCaseCallback<BannersUseCase.ResponseValue> {
            override fun onSuccess(response: BannersUseCase.ResponseValue?) {
                result(response)
            }

            override fun onError(exception: ResponeThrowable?) {
            }
        })

    }

}