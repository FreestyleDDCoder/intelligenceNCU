package com.intelligencencu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.intelligencencu.intelligencencu.R;

/**
 * Created by liangzhan on 4/20/17.
 * 用于发帖子的活动
 */

public class BbsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbs);
        initview();
    }

    private void initview() {

    }
}
