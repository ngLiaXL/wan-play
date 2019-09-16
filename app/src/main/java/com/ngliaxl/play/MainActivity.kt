package com.ngliaxl.play

import android.content.Context
import android.os.Bundle
import android.text.Html
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ngliaxl.play.module.home.HomeContract
import com.ngliaxl.play.module.home.HomePresenter
import com.ngliaxl.play.network.usecase.ArticlesUseCase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

/**
 * Retrofit
 * OkHttp
 * RxJava
 * Gson
 *
 */
class MainActivity : BaseActivity(), HomeContract.View {


    private lateinit var presenter: HomePresenter


    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        presenter = HomePresenter(this)
        adapter = Adapter()
        recyclerView.adapter = adapter


        presenter.getArticles(0)
        presenter.getTopArticles()
    }

    override fun onRespArticles(articles: ArticlesUseCase.Articles?) {
        articles?.datas?.let { adapter.addData(it) }
    }

    override fun onRespTopArticles(articles: List<ArticlesUseCase.Article>?) {
        articles?.let { adapter.addData(0, it) }
    }

    override fun context(): Context {
        return this
    }

    override fun showLoading(message: String) {
    }

    override fun hideLoading() {
    }

    override fun onError(message: String) {
    }


    class Adapter : BaseQuickAdapter<ArticlesUseCase.Article, BaseViewHolder>(R.layout.layout_home_item) {
        override fun convert(helper: BaseViewHolder, item: ArticlesUseCase.Article?) {
            helper.setGone(R.id.top, item?.type == 1)
            helper.setGone(R.id.fresh, item?.fresh ?: false)
            helper.setText(R.id.title, Html.fromHtml(item?.title))
            helper.setText(R.id.author, "·${item?.author}")
            helper.setText(R.id.niceDate, item?.niceDate)
            helper.setText(R.id.type, "${item?.superChapterName}·${item?.chapterName}")
        }

    }


}