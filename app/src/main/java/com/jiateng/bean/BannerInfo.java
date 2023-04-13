package com.jiateng.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class BannerInfo {
    @SerializedName("url")
    private String  url;
    @SerializedName("weight")
    private int weight;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
