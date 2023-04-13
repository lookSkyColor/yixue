package com.jiateng.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class School implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("schoolName")
    private String schoolName;
    @SerializedName("schoolAddress")
    private String schoolAddress;
    @SerializedName("schoolContent")
    private String schoolContent;
    @SerializedName("schoolIphone")
    private String schoolIphone;
    @SerializedName("schoolImage")
    private String schoolImage;
    @SerializedName("schoolSubject")
    private String schoolSubject;
    @SerializedName("createTime")
    private Date createTime;
    @SerializedName("updateTime")
    private Date updateTime;
    @SerializedName("goodsList")
    private List<Subject> goodsList;


    public List<Subject> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Subject> goodsList) {
        this.goodsList = goodsList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public String getSchoolContent() {
        return schoolContent;
    }

    public void setSchoolContent(String schoolContent) {
        this.schoolContent = schoolContent;
    }

    public String getSchoolIphone() {
        return schoolIphone;
    }

    public void setSchoolIphone(String schoolIphone) {
        this.schoolIphone = schoolIphone;
    }

    public String getSchoolImage() {
        return schoolImage;
    }

    public void setSchoolImage(String schoolImage) {
        this.schoolImage = schoolImage;
    }

    public String getSchoolSubject() {
        return schoolSubject;
    }

    public void setSchoolSubject(String schoolSubject) {
        this.schoolSubject = schoolSubject;
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
}
