package com.ngliaxl.play.network.usecase

import com.ldroid.kwei.UseCase
import com.ldroid.kwei.retrofit.ServiceGenerator
import com.ngliaxl.play.network.PlayService
import com.ngliaxl.play.network.RequestValuesWrapper
import com.ngliaxl.play.network.ResponseValuesWrapper
import io.reactivex.Observable

class BannersUseCase : UseCase<BannersUseCase.RequestValues, BannersUseCase.ResponseValue>() {

    class RequestValues : RequestValuesWrapper() {
        override fun checkInput(): Boolean {
            return true
        }
    }

    class ResponseValue : ResponseValuesWrapper<List<Banner>>()

    data class Banner(
        val desc: String,
        val id: Int,
        val imagePath: String,
        val isVisible: Int,
        val order: Int,
        val title: String,
        val type: Int,
        val url: String
    )


    override fun buildObservable(values: RequestValues): Observable<ResponseValue> {
        val service = ServiceGenerator.getInstance().getService(PlayService::class.java)
        return service.getBanner()
    }


}