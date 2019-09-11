package com.ngliaxl.play.network.usecase

import com.google.gson.annotations.Expose
import com.ldroid.kwei.UseCase
import com.ldroid.kwei.retrofit.ServiceGenerator
import com.ngliaxl.play.network.PlayService
import com.ngliaxl.play.network.RequestValuesWrapper
import com.ngliaxl.play.network.ResponseValuesWrapper
import io.reactivex.Observable

class ArticlesUseCase : UseCase<ArticlesUseCase.RequestValues, ArticlesUseCase.ResponseValue>() {

    class RequestValues(
        @field:Expose
        var page: Int
    ) : RequestValuesWrapper() {

        override fun checkInput(): Boolean {
            return true
        }
    }

    class ResponseValue : ResponseValuesWrapper<Articles>()


    data class Articles(
        val curPage: Int?,
        val offset: Int?,
        val over: Boolean?,
        val pageCount: Int?,
        val size: Int?,
        val total: Int?,
        val datas: List<Article>?
    )

    data class Article(
        val apkLink: String,
        val author: String,
        val chapterId: Int,
        val chapterName: String,
        var collect: Boolean,
        val courseId: Int,
        val desc: String,
        val envelopePic: String,
        val fresh: Boolean,
        val id: Int,
        val link: String,
        val niceDate: String,
        val origin: String,
        val prefix: String,
        val projectLink: String,
        val publishTime: Long,
        val superChapterId: Int,
        val superChapterName: String,
        val tags: List<ArticleTag>,
        val title: String,
        val type: Int,
        val userId: Int,
        val visible: Int,
        val zan: Int
    )

    data class ArticleTag(
        val name: String,
        val url: String
    )

    override fun buildObservable(values: RequestValues): Observable<ResponseValue> {
        val service = ServiceGenerator.getInstance().getService(PlayService::class.java)
        return service.getArticles(values.page)
    }


}