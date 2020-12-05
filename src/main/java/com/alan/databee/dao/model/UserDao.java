package com.alan.databee.dao.model;

/**
 * @ClassName UserDao
 * @Author sunshuangcheng
 * @Date 2020/10/31 9:22 下午
 * @Version -V1.0
 */
public class UserDao {
    private int id;
    private String userName;
    private String bizLine;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBizLine() {
        return bizLine;
    }

    public void setBizLine(String bizLine) {
        this.bizLine = bizLine;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
