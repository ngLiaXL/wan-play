package com.ngliaxl.play.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ldroid.kwei.UseCase


abstract class ResponseValuesWrapper<Data> : UseCase.ResponseValue {

    @SerializedName("errorCode")
    var errorCode: Int? = null
    @SerializedName("errorMsg")
    var errorMsg: String? = null
    @Expose
    @SerializedName("data")
    var data: Data? = null


    interface ResponseCode {
        companion object {
            const val SUCCESS : Int = 0
        }
    }

    override fun toString(): String {
        return "ResponseValuesWrapper(errorCode=$errorCode, errorMsg=$errorMsg, data=$data)"
    }


}