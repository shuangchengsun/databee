package com.alan.databee.model;

public class User {
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
