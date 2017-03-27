package com.intelligencencu.entity;

/**
 * Created by liangzhan on 17-3-27.
 * 学校概况实体
 */

public class SchoolOverView {
    private String id;
    private String backgroundUrl;
    private String contentUrl;
    private String imgTitleUrl;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getImgTitleUrl() {
        return imgTitleUrl;
    }

    public void setImgTitleUrl(String imgTitleUrl) {
        this.imgTitleUrl = imgTitleUrl;
    }
}
