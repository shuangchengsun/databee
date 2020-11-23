package com.alan.databee.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class RequestData implements Serializable {

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
     * 当前的实践
     */
    private Date time;

    /**
     * 序列号
     */
    private long serial;

    /**
     * Json格式的message
     */
    private String msg;

    /**
     * 额外的补充数据
     */
    private Map<Object, Object> extMsg;



    public int getBusCode() {
        return BusCode;
    }

    public void setBusCode(int busCode) {
        BusCode = busCode;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getSerial() {
        return serial;
    }

    public void setSerial(long serial) {
        this.serial = serial;
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
}
