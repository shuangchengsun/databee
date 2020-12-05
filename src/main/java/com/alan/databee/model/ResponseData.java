package com.alan.databee.model;

import java.util.Map;

public class ResponseData {

    /**
     * 业务码
     * 0x01 用户登陆
     * 0x02 用户注册
     * 0x03 组件测试
     * 0x04 全链路测试
     * 0x05 任务提交
     * 0x06 结果获取
     */
    private int BusCode;

    /**
     * 处理结果
     */
    private boolean isSuccess;

    /**
     * 服务端传递给前端的数据，JSON格式
     */
    private String msg;

    /**
     * 额外的数据
     */
    private Map<Object, Object> extMsg;


    public int getBusCode() {
        return BusCode;
    }

    public void setBusCode(int busCode) {
        BusCode = busCode;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<Object, Object> getExtMsg() {
        return extMsg;
    }

    public void setExtMsg(Map<Object, Object> extMsg) {
        this.extMsg = extMsg;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "BusCode=" + BusCode +
                ", isSuccess=" + isSuccess +
                ", msg='" + msg + '\'' +
                ", extMsg=" + extMsg +
                '}';
    }
}
