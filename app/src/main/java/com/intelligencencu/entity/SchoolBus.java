package com.intelligencencu.entity;

import java.util.List;

/**
 * Created by liangzhan on 17-3-26.
 * 校车类实体
 */

public class SchoolBus {
    private String name;
    private String note;
    private String pathway;
    private String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPathway() {
        return pathway;
    }

    public void setPathway(String pathway) {
        this.pathway = pathway;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
