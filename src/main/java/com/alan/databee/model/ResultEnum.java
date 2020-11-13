package com.alan.databee.model;

public enum ResultEnum {
    Component_Missing(0x01,"组件不完善"),
    Result_Undefined(0x10,"状态未知"),
    Component_Undefined(0x02,"使用了未定义的通用组件"),
    Component_Compiler_Error(0x03,"组件编译错误")
        ;
    int statCode;
    String statMsg;

    public int getStatCode() {
        return statCode;
    }

    public String getStatMsg() {
        return statMsg;
    }

    ResultEnum(int statCode, String statMsg) {
        this.statCode = statCode;
        this.statMsg = statMsg;
    }
}
