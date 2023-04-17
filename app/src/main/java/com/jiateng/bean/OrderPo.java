package com.jiateng.bean;

import java.io.Serializable;




public class OrderPo implements Serializable {
    private int orderPrice;
    private int orderNum;
    private int subjectPrice;
    private int discountAmount;
    private int subjectClassHours;
    private String subjectName;
    private String category;

    private String token;

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getSubjectPrice() {
        return subjectPrice;
    }

    public void setSubjectPrice(int subjectPrice) {
        this.subjectPrice = subjectPrice;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getSubjectClassHours() {
        return subjectClassHours;
    }

    public void setSubjectClassHours(int subjectClassHours) {
        this.subjectClassHours = subjectClassHours;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
