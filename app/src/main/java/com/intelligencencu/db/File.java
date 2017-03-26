package com.intelligencencu.db;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by liangzhan on 17-3-26.
 * json文件存放的表
 */

public class File extends BmobObject {

    private BmobFile jsonFile;

    public BmobFile getJsonFile() {
        return jsonFile;
    }

    public void setJsonFile(BmobFile jsonFile) {
        this.jsonFile = jsonFile;
    }
}
