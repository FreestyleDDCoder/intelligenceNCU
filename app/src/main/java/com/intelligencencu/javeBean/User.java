package com.intelligencencu.javeBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by liangzhan on 17-3-21.
 * 用户数据
 */

public class User extends BmobObject {
    //这是用户信息的javaBean
    private String UserName;
    private String Password;
    private int sex;
    private String mail;
    private String ImageUrl;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
