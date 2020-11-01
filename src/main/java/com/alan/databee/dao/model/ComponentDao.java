package com.alan.databee.dao.model;

/**
 * @ClassName Component
 * @Author sunshuangcheng
 * @Date 2020/10/31 9:13 下午
 * @Version -V1.0
 */
public class ComponentDao {
    private int id;

    private String name;

    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
