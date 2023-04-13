package com.jiateng.bean;

import lombok.Data;

/**
 * @Description:
 * @Title: Appraise
 * @ProjectName: orderFood
 * @date: 2023/2/8 14:21
 * @author: 骆家腾
 */
@Data
public class Appraise {
    private String appraiseId;
    private String shopId;
    private String orderId;
    private String avatarUrl;
    private String userName;
    private String time;
    private String context;
    /**
     * good 1
     * bad 0
     */
    private Integer type;
    /**
     * 四舍五入
     */
    private Double serveScore;
    private Double foodScore;
    private Integer postTime;

    public String getAppraiseId() {
        return appraiseId == null ? "" : appraiseId;
    }

    public void setAppraiseId(String appraiseId) {
        this.appraiseId = appraiseId;
    }

    public String getShopId() {
        return shopId == null ? "" : shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getOrderId() {
        return orderId == null ? "" : orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAvatarUrl() {
        return avatarUrl == null ? "" : avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUserName() {
        return userName == null ? "" : userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContext() {
        return context == null ? "" : context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getServeScore() {
        return serveScore;
    }

    public void setServeScore(Double serveScore) {
        this.serveScore = serveScore;
    }

    public Double getFoodScore() {
        return foodScore;
    }

    public void setFoodScore(Double foodScore) {
        this.foodScore = foodScore;
    }

    public Integer getPostTime() {
        return postTime;
    }

    public void setPostTime(Integer postTime) {
        this.postTime = postTime;
    }
}
