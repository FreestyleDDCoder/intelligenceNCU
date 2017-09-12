package com.intelligencencu.db;

import cn.bmob.v3.BmobObject;

/**
 * Created by liangzhan on 4/20/17.
 * 这是评论表
 */

public class Comment extends BmobObject {
    private String comment;//评论的内容
    private BBS post; //所评论的帖子，这里体现的是一对多的关系，一个评论只能属于一个帖子
    private User username;//评论的用户，Pointer类型，一对一关系

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BBS getPost() {
        return post;
    }

    public void setPost(BBS post) {
        this.post = post;
    }

    public User getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username;
    }
}
