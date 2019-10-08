package com.ngliaxl.play.module.home


import com.ngliaxl.play.BasePresenter
import com.ngliaxl.play.BaseView
import com.ngliaxl.play.network.usecase.ArticlesUseCase

interface HomeContract {

    interface View : BaseView {

        fun onRespArticles(articles: ArticlesUseCase.Articles?)
        fun onRespTopArticles(articles: List<ArticlesUseCase.Article>?)
        fun onRespArticlesWithTop(articles : MutableList<ArticlesUseCase.Article>?)

    }

    interface Presenter : BasePresenter
}
