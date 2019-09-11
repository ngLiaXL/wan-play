package com.ldroid.kwei.exception;

public class ResponeThrowable extends Exception {
    public int code;
    public String message;

    public ResponeThrowable(Throwable throwable, String message, int code) {
        super(throwable);
        this.message = message;
        this.code = code;
    }

    @Override
    public String toString() {
        return message + "{" + code + "}";
    }
}