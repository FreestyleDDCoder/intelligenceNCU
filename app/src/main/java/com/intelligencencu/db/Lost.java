package com.intelligencencu.db;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by liangzhan on 17-4-9.
 * 丢失表
 */

public class Lost extends BmobObject implements Serializable {
    private String title;//标题
    private String describe;//描述
    private String phone;//联系手机
    private String flag;//用于判断的丢失还是招领

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
