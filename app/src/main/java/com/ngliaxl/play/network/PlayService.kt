package com.ngliaxl.play.network

import com.ngliaxl.play.network.usecase.ArticlesUseCase
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface PlayService {


    // 首页文章
    @GET("/article/list/{page}/json")
    fun getArticles(@Path("page") page: Int): Observable<ArticlesUseCase.ResponseValue>

}
