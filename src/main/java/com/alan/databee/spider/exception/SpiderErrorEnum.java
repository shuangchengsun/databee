package com.alan.databee.spider.exception;

public enum SpiderErrorEnum {
    Component_Not_Fount("pageModel或PageProcessor组件缺失"),
    Script_Compiler_Error("脚本文件编译错误"),
    Download_Error("资源下载错误"),
    Unsupported_Operation("不支持的操作"),
    Class_Instance_Error("类实例化错误"),
    Class_Init_Error("类初始化错误，注入参数错误"),
    Component_Is_Empty("组件内容为空"),
    Script_ClassNum_Error("脚本格式错误，解析到的class多于一个"),
    Script_ClassType_Error("脚本格式错误，解析到了抽象的class")
    ;
    String msg;

    SpiderErrorEnum(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "error info: "+msg;
    }
}
