package com.alan.databee.model;

public enum  BusTypeEnum {
    Commit_Task(0,"提交一个配置好的任务")
    ;
    public int busyCode;
    public String describe;

    BusTypeEnum(int busyCode, String describe) {
        this.busyCode = busyCode;
        this.describe = describe;
    }

}
