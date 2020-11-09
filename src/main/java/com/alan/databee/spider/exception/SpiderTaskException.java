package com.alan.databee.spider.exception;

/**
 * @ClassName SpiderTaskException
 * @Author sunshuangcheng
 * @Date 2020/10/31 10:57 下午
 * @Version -V1.0
 */
public class SpiderTaskException extends RuntimeException {

    public SpiderTaskException(SpiderErrorEnum errorEnum) {
        super(errorEnum.msg);
    }

    public SpiderTaskException(SpiderErrorEnum errorEnum, Throwable cause) {
        super(errorEnum.msg, cause);
    }

    public SpiderTaskException(SpiderErrorEnum errorEnum, String msg){
        super(errorEnum.msg+", " +msg);
    }
}
