package com.ldroid.kwei.exception;

/**
 * ServerException发生后，将自动转换为ResponeThrowable 返回在onError(e)中
 */
public class ServerException extends RuntimeException {
    int code;
    String message;
}