package com.alan.databee.spider.exception;

public class IllegalParamException extends Throwable{
    public IllegalParamException(SpiderErrorEnum errorEnum) {
        super(errorEnum.msg);
    }

    public IllegalParamException(SpiderErrorEnum errorEnum, Throwable cause) {
        super(errorEnum.msg, cause);
    }

    public IllegalParamException(Throwable cause) {
        super(cause);
    }
}
