package com.intelligencencu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.intelligencencu.entity.SchoolOverViewContent;
import com.intelligencencu.intelligencencu.R;
import com.intelligencencu.utils.ToastUntil;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liangzhan on 17-3-28.
 * 用于展示详细信息的活动
 */

public class ShowViewActivity extends AppCompatActivity {

    private String path;
    private TextView tv_showviewmessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showview);
        initView();
    }

    public Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Gson gson = new Gson();
            SchoolOverViewContent content = gson.fromJson(path, SchoolOverViewContent.class);
            Log.d("content", content.getData());
            tv_showviewmessage.setText(content.getData());
        }
    };

    private void initView() {
        CollapsingToolbarLayout coll_toolbar = (CollapsingToolbarLayout) findViewById(R.id.coll_toolbar);
        ImageView iv_showviewbg = (ImageView) findViewById(R.id.iv_showviewbg);
        Toolbar tb_showviewtitle = (Toolbar) findViewById(R.id.tb_showviewtitle);
        tv_showviewmessage = (TextView) findViewById(R.id.tv_showviewmessage);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String backgroundUrl = intent.getStringExtra("backgroundUrl");
        final String contentUrl = intent.getStringExtra("contentUrl");
        String imgTitleUrl = intent.getStringExtra("imgTitleUrl");

        setSupportActionBar(tb_showviewtitle);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //加载背景图片
        Glide.with(ShowViewActivity.this).load(backgroundUrl).into(iv_showviewbg);
        coll_toolbar.setTitle(title);

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(contentUrl).build();
                    Response response = client.newCall(request).execute();
                    path = response.body().string();
                    hander.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUntil.showShortToast(ShowViewActivity.this, "数据异常！" + e);
                }

            }
        }.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
