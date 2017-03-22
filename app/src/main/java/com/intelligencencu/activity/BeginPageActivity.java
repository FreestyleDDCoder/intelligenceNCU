package com.intelligencencu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.intelligencencu.intelligencencu.R;
import com.intelligencencu.utils.ToastUntil;

import de.hdodenhof.circleimageview.CircleImageView;
import dym.unique.com.springinglayoutlibrary.handler.SpringTouchRippleHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingAlphaShowHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchDragHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchPointHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTranslationShowHandler;
import dym.unique.com.springinglayoutlibrary.view.SpringingImageView;
import dym.unique.com.springinglayoutlibrary.view.SpringingTextView;
import dym.unique.com.springinglayoutlibrary.viewgroup.SpringingLinearLayout;

/**
 * Created by liangzhan on 17-3-21.
 * 这是应用的开始界面
 */

public class BeginPageActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mdrawer_layout;
    private NavigationView mNav_view;
    private SpringingImageView mIcon_image;
    private SpringingImageView mLogout;
    private SharedPreferences mSpfs;
    private SpringingTextView mTv_state;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginpage);

        initUI();
        initSpringLayout();
        initEvent();
        showViews();
    }

    private void initEvent() {

    }

    private void showViews() {
//
    }

    //用于展现效果
    private void initSpringLayout() {
        mTv_state.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, mTv_state));
        mLogout.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, mLogout).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        mIcon_image.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, mIcon_image).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));

    }

    private void initUI() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //用于记录当前的登录状态
        mSpfs = getSharedPreferences("config", MODE_PRIVATE);
        mdrawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNav_view = (NavigationView) findViewById(R.id.nav_view);

        //注意不是当前ContentView是不可以直接使用findViewById的
        View headerView = mNav_view.getHeaderView(0);
        mIcon_image = (SpringingImageView) headerView.findViewById(R.id.icon_image);
        mIcon_image.setIsCircleImage(true);
        mLogout = (SpringingImageView) headerView.findViewById(R.id.logout);
        mTv_state = (SpringingTextView) headerView.findViewById(R.id.tv_state);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }
        mNav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                mIcon_image.setOnClickListener(BeginPageActivity.this);
                Log.d("icon_image", "到了这里");

                mLogout.setOnClickListener(BeginPageActivity.this);

                switch (item.getItemId()) {
                    //点击主页时的变化
                    case R.id.nav_main:
                        mdrawer_layout.closeDrawers();
                        break;
                    //点击设置
                    case R.id.nav_setting:
                        break;
                }
                return true;
            }
        });
        mTv_state.setOnClickListener(this);
        mIcon_image.setOnClickListener(this);
        mLogout.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mdrawer_layout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击登录界面,弹出登录界面
            case R.id.icon_image:
                goToLoginActivity();
                break;
            case R.id.tv_state:
                goToLoginActivity();
                break;
            //点击登出界面
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("退出登录");
                builder.setMessage("是否确认退出登录？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //判断是否登录
                        if (mSpfs.getBoolean("isLogin", false)) {
                            mSpfs.edit().putBoolean("isLogin", false);
                        } else {
                            ToastUntil.showShortToast(BeginPageActivity.this, "请先登录！");
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;

        }
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(BeginPageActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void initData() {

    }

}
