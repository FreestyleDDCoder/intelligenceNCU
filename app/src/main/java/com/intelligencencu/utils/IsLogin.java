package com.intelligencencu.utils;

import com.intelligencencu.db.User;

import cn.bmob.v3.BmobUser;

/**
 * Created by liangzhan on 17-3-24.
 * 判断用户是否登录类
 */
public class IsLogin {
    private User currentUser;

    public IsLogin() {
        currentUser = BmobUser.getCurrentUser(User.class);
    }

    public boolean isLogin() {
        if (currentUser != null) {
            return true;
        } else {
            return false;
        }
    }

    public User getUser() {
        return this.currentUser;
    }
}
