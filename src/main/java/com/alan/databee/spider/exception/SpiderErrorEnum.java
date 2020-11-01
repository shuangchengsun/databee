package com.alan.databee.spider.exception;

public enum SpiderErrorEnum {
    Component_Not_Fount("pageModel或PageProcessor组件缺失"),
    Script_Compiler_Error("脚本文件编译错误")
    ;
    String msg;

    SpiderErrorEnum(String msg) {
        this.msg = msg;
    }
}
