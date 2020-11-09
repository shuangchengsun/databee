package com.alan.databee.spider.exception;

public class DaoException extends Throwable{

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(SpiderErrorEnum errorEnum , Throwable cause) {
        super(errorEnum.msg,cause);
    }

    public DaoException(SpiderErrorEnum errorEnum) {
        super(errorEnum.msg);
    }
}
