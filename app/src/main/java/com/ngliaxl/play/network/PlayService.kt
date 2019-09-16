package com.ngliaxl.play.network

import com.ngliaxl.play.network.usecase.ArticlesUseCase
import com.ngliaxl.play.network.usecase.BannersUseCase
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface PlayService {


    @GET("/article/list/{page}/json")
    fun getArticles(@Path("page") page: Int): Observable<ArticlesUseCase.HomeArticlesUseCase.ResponseValue>

    @GET("/article/top/json")
    fun getTopArticles(): Observable<ArticlesUseCase.TopArticlesUseCase.ResponseValue>

    @GET("/banner/json")
    fun getBanner(): Observable<BannersUseCase.ResponseValue>

}
