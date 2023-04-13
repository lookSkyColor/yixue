package com.jiateng.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Subject {

    @SerializedName("id")
    private int id;
    @SerializedName("subjectName")
    private String subjectName;
    @SerializedName("subjectImgUrl")
    private String subjectImgUrl;
    @SerializedName("subjectClassHour")
    private int subjectClassHour;
    @SerializedName("subjectPrice")
    private int subjectPrice;
    @SerializedName("subjectNumber")
    private int subjectNumber;
    @SerializedName("subjectEffective")
    private int subjectEffective;
    @SerializedName("createTime")
    private Date createTime;
    @SerializedName("updateTime")
    private Date updateTime;
    @SerializedName("category")
    private String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getSubjectClassHour() {
        return subjectClassHour;
    }

    public void setSubjectClassHour(int subjectClassHour) {
        this.subjectClassHour = subjectClassHour;
    }

    public int getSubjectPrice() {
        return subjectPrice;
    }

    public void setSubjectPrice(int subjectPrice) {
        this.subjectPrice = subjectPrice;
    }

    public String getSubjectImgUrl() {
        return subjectImgUrl;
    }

    public void setSubjectImgUrl(String subjectImgUrl) {
        this.subjectImgUrl = subjectImgUrl;
    }

    public int getSubjectNumber() {
        return subjectNumber;
    }

    public void setSubjectNumber(int subjectNumber) {
        this.subjectNumber = subjectNumber;
    }

    public int getSubjectEffective() {
        return subjectEffective;
    }

    public void setSubjectEffective(int subjectEffective) {
        this.subjectEffective = subjectEffective;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
