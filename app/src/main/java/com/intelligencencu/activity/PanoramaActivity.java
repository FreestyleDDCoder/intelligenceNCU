package com.intelligencencu.activity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.intelligencencu.intelligencencu.R;

/**
 * Created by liangzhan on 17-4-6.
 * 全景图活动
 */

public class PanoramaActivity extends AppCompatActivity {

    private BMapManager bMapManager;
    private PanoramaView pv_panorama;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama);
        Toolbar tb_panorama = (Toolbar) findViewById(R.id.tb_panorama);
        setSupportActionBar(tb_panorama);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        pv_panorama = (PanoramaView) findViewById(R.id.pv_panorama);
        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude", 28.65456325298023);
        double longitude = intent.getDoubleExtra("longitude", 115.80237329006195);
        Application application = getApplication();
        if (bMapManager == null) {
            bMapManager = new BMapManager(application);
            bMapManager.init(new MKGeneralListener() {
                @Override
                public void onGetPermissionState(int i) {

                }
            });
        }
        pv_panorama.setPanorama(longitude, latitude);
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

    @Override
    protected void onPause() {
        super.onPause();
        pv_panorama.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pv_panorama.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        pv_panorama.destroy();
    }
}
