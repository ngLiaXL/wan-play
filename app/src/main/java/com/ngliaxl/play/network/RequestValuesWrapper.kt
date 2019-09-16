package com.ngliaxl.play.network

import com.google.gson.annotations.Expose
import com.ldroid.kwei.UseCase
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.GzipSink
import okio.Okio
import java.io.IOException
import java.util.*


abstract class RequestValuesWrapper : UseCase.RequestValues {


    var errors: List<String> = ArrayList()

    val params: Map<String, Any>
        get() {
            val params = HashMap<String, Any>()
            for (field in javaClass.fields) {
                if (!field.isAnnotationPresent(Expose::class.java)) {
                    continue
                }
                var value: Any? = null
                try {
                    value = field.get(this)
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

                if (value != null) {
                    params[field.name] = value
                }
            }
            return params
        }

    val multipartBuilder: MultipartBody.Builder
        get() = addFormData(addFilesData(MultipartBody.Builder().setType(MultipartBody.FORM)))

    fun addFormData(builder: MultipartBody.Builder): MultipartBody.Builder {
        val textMaps = params
        for ((key, value) in textMaps) {
            builder.addFormDataPart(key, value.toString())
        }
        return builder
    }

    fun addFilesData(builder: MultipartBody.Builder): MultipartBody.Builder {
        return builder
    }

    /**
     * gzip压缩的byte数组
     * 响应压缩过程
     * request 中添加 Accept-Encoding: gzip, deflate 表示客户端支持gzip，deflate
     * response 服务端收到客户端可接收gzip，deflate ，增加Content-Encodin:gzip表示响应已被压缩，客户端处理需解压
     *
     * @param body
     * @return
     */
    fun getGzipRequest(body: String): RequestBody {
        return gzip(RequestBody.create(MediaType.parse("application/octet-stream"), body))
    }


    private fun gzip(body: RequestBody): RequestBody {
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return body.contentType()
            }

            override fun contentLength(): Long {
                return -1 // We don't know the compressed length in advance!
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                val gzipSink = Okio.buffer(GzipSink(sink))
                body.writeTo(gzipSink)
                gzipSink.close()
            }
        }
    }

    open fun checkInput(): Boolean {
        return true
    }
}
