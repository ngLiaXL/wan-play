package com.ldroid.kwei.exception;


import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

public class ExceptionHandler {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ResponeThrowable handleException(Throwable e) {
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    return new ResponeThrowable(e, "网络错误", httpException.code());
            }
        }
        if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            return new ResponeThrowable(e, resultException.message, resultException.code);
        }
        if (e instanceof JsonParseException || e instanceof JSONException) {
            return new ResponeThrowable(e, "数据解析失败", ERROR.PARSE_ERROR);
        }
        if (e instanceof ConnectException) {
            return new ResponeThrowable(e, "服务器或网络异常，请检查网络设置或稍后重试", ERROR.NETWORD_ERROR);
        }
        if (e instanceof SocketTimeoutException) {
            return new ResponeThrowable(e, "请求服务器超时，请检查网络设置或稍后重试", ERROR.TIME_OUT);
        }
        if (e instanceof UnknownHostException) {
            return new ResponeThrowable(e, "未连接网络", ERROR.NO_CONNECTION);
        }
        if (e instanceof javax.net.ssl.SSLHandshakeException) {
            return new ResponeThrowable(e, "证书验证失败", ERROR.SSL_ERROR);
        }

        return new ResponeThrowable(e, "未知错误", ERROR.UNKNOWN);
    }


    /**
     * 约定异常
     */
    interface ERROR {
        /**
         * 未知错误
         */
        int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        int NETWORD_ERROR = 1002;
        /**
         * 网络超时
         */
        int TIME_OUT = 1003;
        /**
         * 未连接
         */
        int NO_CONNECTION = 1004;
        /**
         * 协议出错
         */
        int HTTP_ERROR = 1005;

        /**
         * 证书出错
         */
        int SSL_ERROR = 1006;
    }


}