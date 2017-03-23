package com.intelligencencu.db;

import cn.bmob.v3.BmobUser;

/**
 * Created by liangzhan on 17-3-21.
 * 用户数据
 * BmobUser除了从BmobObject继承的属性外，还有几个特定的属性：
 * username: 用户的用户名（必需）。
 * password: 用户的密码（必需）。
 * email: 用户的电子邮件地址（可选）。
 * emailVerified:邮箱认证状态（可选）。
 * mobilePhoneNumber：手机号码（可选）。
 * mobilePhoneNumberVerified：手机号码的认证状态（可选）
 */

public class User extends BmobUser {
    //这是用户信息的javaBean
    //存放的数据表(性别、年龄、头像,个性名称)
    private Boolean sex;
    private String nick;
    private Integer age;
    private String chinesename;

    public String getChinesename() {
        return chinesename;
    }

    public void setChinesename(String chinesename) {
        this.chinesename = chinesename;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
