package com.intelligencencu.db;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by liangzhan on 4/20/17.
 * 这是论坛主表
 */

public class BBS extends BmobObject implements Serializable {
    /**
     * 这个表要包含用户名，头像，发表的内容，匿名条件，评论条数，点赞数，ACL权限等
     */
    private User username;//帖子发布者，体现一对一的关系
    private BmobFile image;//帖子图片
    private String desc;
    private boolean noname;
    private boolean sex;
    private int review;
    private int likes;//多对多关系：用于存储喜欢该帖子的所有用户

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public User getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username;
    }

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isNoname() {
        return noname;
    }

    public void setNoname(boolean noname) {
        this.noname = noname;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
