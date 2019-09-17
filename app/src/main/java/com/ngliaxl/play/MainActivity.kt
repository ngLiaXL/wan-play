package com.ngliaxl.play

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
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
            helper.setText(R.id.author, "· ${item?.author}")
            helper.setText(R.id.niceDate, item?.niceDate)
            helper.setText(R.id.type, "${item?.superChapterName} · ${item?.chapterName}")

            setTagsView(helper, item)
        }

        private fun setTagsView(
            helper: BaseViewHolder,
            item: ArticlesUseCase.Article?
        ) {
            val view = helper.getView<LinearLayout>(R.id.tagsLayout)
            if (item?.tags != null && item.tags.isNotEmpty()) {
                if (view.childCount == 0) {
                    for (tag in item.tags) {
                        view.addView(generateTagTextView(mContext))
                    }
                }
            } else {
                if (view.childCount > 0) view.removeAllViews()
            }

            if (view.childCount > 0 && item?.tags != null && item.tags.isNotEmpty()) {
                for ((index, value) in item.tags.withIndex()) {
                    val textView = view.getChildAt(index) as TextView
                    textView.text = value.name
                }
            }
        }

        private fun generateTagTextView(ctx: Context): TextView {
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.gravity = Gravity.CENTER
            params.rightMargin = 10
            val textView = TextView(ctx)
            textView.setPadding(6, 0, 6, 0)
            textView.layoutParams = params
            textView.setBackgroundResource(R.drawable.bg_article_tag)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11f)
            textView.setTextColor(Color.parseColor("#4282f4"))

            return textView
        }

    }


}