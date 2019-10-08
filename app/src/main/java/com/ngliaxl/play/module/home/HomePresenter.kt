package com.ngliaxl.play.module.home

import com.ldroid.kwei.UseCase
import com.ldroid.kwei.UseCaseHandler
import com.ldroid.kwei.exception.ResponeThrowable
import com.ngliaxl.play.BasePresenter.Companion.NETWORK_ERROR
import com.ngliaxl.play.network.RequestValuesWrapper
import com.ngliaxl.play.network.ResponseValuesWrapper
import com.ngliaxl.play.network.usecase.ArticlesUseCase

internal class HomePresenter(val view: HomeContract.View) : HomeContract.Presenter {

    private val useCaseHandler: UseCaseHandler = UseCaseHandler()


    internal fun getAllArticles() {
        val topArticlesUseCase =
            ArticlesUseCase.TopArticlesUseCase(object : RequestValuesWrapper() {})
        val homeArticlesUseCase =
            ArticlesUseCase.HomeArticlesUseCase(ArticlesUseCase.HomeArticlesUseCase.RequestValues(0))
        useCaseHandler.execute2(topArticlesUseCase, homeArticlesUseCase,
            object : UseCaseHandler.ZipCallback<ArticlesUseCase.TopArticlesUseCase.ResponseValue,
                    ArticlesUseCase.HomeArticlesUseCase.ResponseValue, MutableList<ArticlesUseCase.Article>> {
                override fun apply(
                    r1: ArticlesUseCase.TopArticlesUseCase.ResponseValue?,
                    r2: ArticlesUseCase.HomeArticlesUseCase.ResponseValue?
                ): MutableList<ArticlesUseCase.Article>? {
                    if (r1 == null && r2 == null) {
                        return null
                    }
                    if (r1 == null) return r2?.data?.datas as MutableList<ArticlesUseCase.Article>

                    val re1 = r1.data as MutableList<ArticlesUseCase.Article>
                    val re2 = r2?.data?.datas as MutableList<ArticlesUseCase.Article>

                    re1.addAll(re2)
                    return re1
                }

            },
            object : UseCase.UseCaseCallback<MutableList<ArticlesUseCase.Article>> {
                override fun onError(exception: ResponeThrowable?) {
                    view.hideLoading()
                    view.onError(exception.toString())
                }

                override fun onSuccess(response: MutableList<ArticlesUseCase.Article>?) {
                    view.hideLoading()
                    view.onRespArticlesWithTop(response)
                }

            }

        )

    }


    internal fun getArticles(page: Int) {
        val request = ArticlesUseCase.HomeArticlesUseCase.RequestValues(page)
        if (!request.checkInput()) {
            view.onError("page error")
            return
        }
        useCaseHandler.execute(
            ArticlesUseCase.HomeArticlesUseCase(request),
            object : UseCase.UseCaseCallback<ArticlesUseCase.HomeArticlesUseCase.ResponseValue> {
                override fun onSuccess(response: ArticlesUseCase.HomeArticlesUseCase.ResponseValue?) {
                    view.hideLoading()
                    when {
                        response == null -> view.onError(NETWORK_ERROR)
                        ResponseValuesWrapper.ResponseCode.SUCCESS == response.errorCode -> view.onRespArticles(
                            response.data
                        )
                        else -> view.onError(response.errorMsg ?: NETWORK_ERROR)
                    }
                }

                override fun onError(exception: ResponeThrowable?) {
                    view.hideLoading()
                    view.onError(exception.toString())
                }
            })
    }

    internal fun getTopArticles() {
        val request = object : RequestValuesWrapper() {}
        useCaseHandler.execute(
            ArticlesUseCase.TopArticlesUseCase(request),
            object : UseCase.UseCaseCallback<ArticlesUseCase.TopArticlesUseCase.ResponseValue> {
                override fun onSuccess(response: ArticlesUseCase.TopArticlesUseCase.ResponseValue?) {
                    view.hideLoading()
                    when {
                        response == null -> view.onError(NETWORK_ERROR)
                        ResponseValuesWrapper.ResponseCode.SUCCESS == response.errorCode -> view.onRespTopArticles(
                            response.data
                        )
                        else -> view.onError(response.errorMsg ?: NETWORK_ERROR)
                    }
                }

                override fun onError(exception: ResponeThrowable?) {
                    view.hideLoading()
                    view.onError(exception.toString())
                }
            })
    }


    override fun resume() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pause() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun destroy() {
        useCaseHandler.dispose()
    }

}