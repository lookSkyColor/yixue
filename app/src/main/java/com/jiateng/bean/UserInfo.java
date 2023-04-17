package com.jiateng.bean;

import com.jiateng.common.utils.PayResult;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Title: UserInfo
 * @ProjectName: orderFood
 * @date: 2023/1/15 17:17
 * @author: 骆家腾
 */
@Data

public class UserInfo {
    private long id;
    private String name;
    private String password;
    private String iphone;
    private int sex;
    private String address;
    private int roleId;
    private int corporationId;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIphone() {
        return iphone;
    }

    public void setIphone(String iphone) {
        this.iphone = iphone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getCorporationId() {
        return corporationId;
    }

    public void setCorporationId(int corporationId) {
        this.corporationId = corporationId;
    }
}
