package com.jiateng.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @Description:
 * @Title: HistoryOrder
 * @ProjectName: orderFood
 * @date: 2023/1/11 12:56
 * @author: 骆家腾
 */
@Data
public class Order {
    private String userId;
    private String shopId;
    private String orderId;
    private String shopName;
    private String status;
    private List<ShoppingCart> shoppingCartList;
    private Double money;
    private String startTimeOfOrder;
    private String finishTimeOfOrder;
    private String address;
    private String remark;
    private String paidMethod;
    private User user;

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getShopName() {
        return shopName == null ? "" : shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ShoppingCart> getShoppingCartList() {
        if (shoppingCartList == null) {
            return new ArrayList<>();
        }
        return shoppingCartList;
    }

    public void setShoppingCartList(List<ShoppingCart> shoppingCartList) {
        this.shoppingCartList = shoppingCartList;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getStartTimeOfOrder() {
        return startTimeOfOrder == null ? "" : startTimeOfOrder;
    }

    public void setStartTimeOfOrder(String startTimeOfOrder) {
        this.startTimeOfOrder = startTimeOfOrder;
    }

    public String getFinishTimeOfOrder() {
        return finishTimeOfOrder == null ? "" : finishTimeOfOrder;
    }

    public void setFinishTimeOfOrder(String finishTimeOfOrder) {
        this.finishTimeOfOrder = finishTimeOfOrder;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPaidMethod() {
        return paidMethod == null ? "" : paidMethod;
    }

    public void setPaidMethod(String paidMethod) {
        this.paidMethod = paidMethod;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
