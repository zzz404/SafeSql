package zzz404.safesql.helper;

import zzz404.safesql.dynamic.PrimaryKey;

public class Category {
    @PrimaryKey
    private Integer id;
    @PrimaryKey
    private String name;
    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}