package com.intelligencencu.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.intelligencencu.intelligencencu.R;

import java.util.ArrayList;

import dym.unique.com.springinglayoutlibrary.handler.SpringTouchRippleHandler;
import dym.unique.com.springinglayoutlibrary.view.SpringingTextView;
import dym.unique.com.springinglayoutlibrary.viewgroup.SpringingLinearLayout;

/**
 * Created by liangzhan on 17-3-23.
 * 地图活动
 */

public class MapActivity extends AppCompatActivity {

    private LocationClient mLocationClient;
    private SpringingTextView mTv_position;
    private MapView mMv_baiduview;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    private double latitude;
    private double longitude;
    private BDLocation location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        //点击事件
        initEvent();
    }

    private void initEvent() {

    }

    private void initUI() {
        //展示地图(初始化一定在findViewById前完成，否则会出错)
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        mMv_baiduview = (MapView) findViewById(R.id.mv_baiduview);
        //获取地图控制器（对地图进行各种操作）
        baiduMap = mMv_baiduview.getMap();
        //设置地图上显示的位置随着位置的变动而变动
        baiduMap.setMyLocationEnabled(true);

        mTv_position = (SpringingTextView) findViewById(R.id.tv_position);

        Toolbar mBaidumapToolbar = (Toolbar) findViewById(R.id.baidumapToolbar);

        setSupportActionBar(mBaidumapToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //设置导航图标（默认样式）
            actionBar.setDisplayHomeAsUpEnabled(true);
            //修改导航图标样式
//            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());

//        运行时权限
        ArrayList<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MapActivity.this, permissions, 1);
        } else {
            requestLocation();
        }
    }

    private void requestLocation() {
        //start()方法只执行一次
        initLocation();
        mLocationClient.start();
    }

    //每5秒获取一次位置信息
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocationClient.setLocOption(option);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.normalmap:
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.satellitemap:
                baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.allviewmap:
                gotoPanoramaActivity();
                break;
            case R.id.mylocation:
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
                    baiduMap.animateMapStatus(update);
                    //设置地图的缩放级别（3-19）数字越大信息越精细
                    update = MapStatusUpdateFactory.zoomTo(16f);
                    baiduMap.animateMapStatus(update);
                }
                break;
        }
        return true;
    }

    private void gotoPanoramaActivity() {
        Intent intent = new Intent(MapActivity.this, PanoramaActivity.class);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        startActivity(intent);
    }

    //运行时权限结果处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        //当所有权限都开启后才进行地图活动
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MapActivity.this, "必须同意所有权限才能使用本功能", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(MapActivity.this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    //处理位置信息
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            location = bdLocation;
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                //找到自己的位置
                navigateUpTo(bdLocation);
            }
            StringBuffer currentposition = new StringBuffer();
            latitude = bdLocation.getLatitude();
            longitude = bdLocation.getLongitude();
            currentposition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
            currentposition.append("经度：").append(bdLocation.getLongitude()).append("\n");
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }


    //将定位的位置显示到地图上
    private void navigateUpTo(BDLocation bdLocation) {
        if (isFirstLocate) {
            //用于存放经纬度
            LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
            baiduMap.animateMapStatus(update);
            //设置地图的缩放级别（3-19）数字越大信息越精细
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        } else {

        }
        //设置地图上显示的位置随着位置的变动而变动（有小光标）
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(bdLocation.getLatitude());
        locationBuilder.longitude(bdLocation.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMv_baiduview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMv_baiduview.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mMv_baiduview.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maptoolbar, menu);
        return true;
    }
}
