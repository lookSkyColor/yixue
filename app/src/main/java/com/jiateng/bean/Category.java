package com.jiateng.bean;

import java.io.Serializable;

public class Category implements Serializable {
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Category(String category) {
        this.category = category;
    }

    public Category() {

    }
}
