package com.intelligencencu.db;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

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
    //头像图片
    private BmobFile image;

    private String signature;

    private BmobRelation favorite;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public BmobRelation getFavorite() {
        return favorite;
    }

    public void setFavorite(BmobRelation favorite) {
        this.favorite = favorite;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }
}
