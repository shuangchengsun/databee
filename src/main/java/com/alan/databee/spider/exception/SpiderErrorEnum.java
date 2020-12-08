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
    Script_ClassType_Error("脚本格式错误，解析到了抽象的class"),
    Unexpect_Status_Code("不支持的网页状态码"),
    Component_Incomplete("组件检查未通过，组件不完整"),
    DataBee_Status_Error("DataBee 不再正确的状态")
    ;
    String msg;

    SpiderErrorEnum(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
