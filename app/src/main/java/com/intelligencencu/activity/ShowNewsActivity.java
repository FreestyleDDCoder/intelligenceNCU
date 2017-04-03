package com.intelligencencu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.intelligencencu.intelligencencu.R;
/**
 * Created by liangzhan on 17-3-30.
 * 展示新闻的活动
 */

public class ShowNewsActivity extends AppCompatActivity {
    private TextView tv_showviewmessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shownews);
        initView();
    }

    private void initView() {
        CollapsingToolbarLayout coll_toolbar = (CollapsingToolbarLayout) findViewById(R.id.coll_newstoolbar);
        ImageView iv_showviewbg = (ImageView) findViewById(R.id.iv_shownewsbg);
        Toolbar tb_showviewtitle = (Toolbar) findViewById(R.id.tb_shownewstitle);
        tv_showviewmessage = (TextView) findViewById(R.id.tv_shownewsmessage);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String backgroundUrl = intent.getStringExtra("img");
        final String contentUrl = intent.getStringExtra("content");
        String time = intent.getStringExtra("time");
        tv_showviewmessage.setText(time + "\n" + contentUrl);
        setSupportActionBar(tb_showviewtitle);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //加载背景图片
        Glide.with(ShowNewsActivity.this).load(backgroundUrl).into(iv_showviewbg);
        coll_toolbar.setTitle(title);
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
