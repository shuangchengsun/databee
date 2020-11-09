package com.alan.databee.spider.exception;

/**
 * @ClassName ScriptException
 * @Author sunshuangcheng
 * @Date 2020/11/1 1:08 下午
 * @Version -V1.0
 */
public class ScriptException extends Throwable {
    public ScriptException(SpiderErrorEnum errorEnum) {
        super(errorEnum.msg);
    }

    public ScriptException(SpiderErrorEnum errorEnum, Throwable cause) {
        super(errorEnum.msg, cause);
    }

    public ScriptException(Throwable cause) {
        super(cause);
    }
}
