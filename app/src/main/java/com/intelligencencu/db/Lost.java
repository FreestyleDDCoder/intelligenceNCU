package com.intelligencencu.db;

import cn.bmob.v3.BmobObject;

/**
 * Created by liangzhan on 17-4-9.
 * 丢失表
 */

public class Lost extends BmobObject {
    private String title;//标题
    private String describe;//描述
    private String phone;//联系手机
    private String flag;

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