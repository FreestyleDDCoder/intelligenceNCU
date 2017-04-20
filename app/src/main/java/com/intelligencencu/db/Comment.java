package com.intelligencencu.db;

import cn.bmob.v3.BmobObject;

/**
 * Created by liangzhan on 4/20/17.
 * 这是评论表
 */

public class Comment extends BmobObject {
    private String desc;
    private BBS bbs; //所评论的帖子，这里体现的是一对多的关系，一个评论只能属于一个微博
    private User username;//评论的用户，Pointer类型，一对一关系

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public BBS getBbs() {
        return bbs;
    }

    public void setBbs(BBS bbs) {
        this.bbs = bbs;
    }

    public User getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username;
    }
}
