package com.alan.databee.spider.exception;

public class ClassServiceException extends Throwable {
    public ClassServiceException(String message) {
        super(message);
    }

    public ClassServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassServiceException(SpiderErrorEnum errorEnum, Throwable cause) {
        super(errorEnum.msg, cause);
    }
}
