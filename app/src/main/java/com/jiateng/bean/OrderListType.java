package com.jiateng.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class OrderListType implements Serializable {

    @SerializedName("id")
    private long id;
    @SerializedName("orderNo")
    private String orderNo;
    @SerializedName("userId")
    private int userId;
    @SerializedName("orderPrice")
    private int orderPrice;
    @SerializedName("orderNum")
    private int orderNum;
    @SerializedName("subjectPrice")
    private int subjectPrice;
    @SerializedName("discountAmount")
    private int discountAmount;
    @SerializedName("subjectClassHours")
    private int subjectClassHours;
    @SerializedName("orderType")
    private int orderType;

    @SerializedName("subjectName")
    private String subjectName;
    @SerializedName("category")
    private String category;
    @SerializedName("placeAnOrder")
    private Date placeAnOrder;
    @SerializedName("createTime")
    private Date createTime;
    @SerializedName("updateTime")
    private Date updateTime;
    @SerializedName("timeOfPayment")
    private Date timeOfPayment;
    @SerializedName("schoolName")
    private String schoolName;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

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

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
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

    public Date getPlaceAnOrder() {
        return placeAnOrder;
    }

    public void setPlaceAnOrder(Date placeAnOrder) {
        this.placeAnOrder = placeAnOrder;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getTimeOfPayment() {
        return timeOfPayment;
    }

    public void setTimeOfPayment(Date timeOfPayment) {
        this.timeOfPayment = timeOfPayment;
    }
}
