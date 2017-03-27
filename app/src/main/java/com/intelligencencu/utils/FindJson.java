package com.intelligencencu.utils;

import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intelligencencu.db.File;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liangzhan on 17-3-27.
 * 获取json的工具
 */

public class FindJson {

    private String path = null;
    public FindJson(String id) {
        BmobQuery<File> query = new BmobQuery<File>();
        query.getObject(id, new QueryListener<File>() {
            @Override
            public void done(File file, BmobException e) {
                if (e == null) {
                    BmobFile bmobFile = file.getJsonFile();
                    Log.d("Name", "查询成功！" + bmobFile.getFilename());
                    if (bmobFile != null) {
                        final String url = bmobFile.getFileUrl();
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    OkHttpClient client = new OkHttpClient();
                                    Request request = new Request.Builder().url(url).build();
                                    Response response = client.newCall(request).execute();
                                    path = response.body().string();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }.start();
                    } else {
                        Log.d("Name", "bmobFikle=null！");
                    }
                }
            }
        });
    }
    
    public String getPath() {
        return path;
    }
}
