package com.jiateng.bean;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description:
 * @Title: CarGoods
 * @ProjectName: orderFood
 * @date: 2023/2/10 0:09
 * @author: 骆家腾
 */

@Getter
@Setter
public class ShoppingCart implements Serializable {
    private Integer shoppingCartId;
    private String userId;
    private String shopId;
    private String goodsId;
    private String goodsName;
    private int goodsPrice;
    private String goodsImgUrl;
    private Integer goodsCount;

    public ShoppingCart(Integer shoppingCartId, String userId, String shopId, String goodsId, String goodsName, int goodsPrice, String goodsImgUrl, Integer goodsCount) {
        this.shoppingCartId = shoppingCartId;
        this.userId = userId;
        this.shopId = shopId;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsImgUrl = goodsImgUrl;
        this.goodsCount = goodsCount;
    }

    public ShoppingCart(){}

    public Integer getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(Integer shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(int goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsImgUrl() {
        return goodsImgUrl;
    }

    public void setGoodsImgUrl(String goodsImgUrl) {
        this.goodsImgUrl = goodsImgUrl;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }
}
