package com.intelligencencu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.intelligencencu.Fragment.NewSchoolMateFragment;
import com.intelligencencu.Fragment.SchoolNewsFragment;
import com.intelligencencu.Fragment.SchoolYellowFragment;
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
    private FrameLayout mFl_beginPage;
    private SpringingImageView mNewclassmate;
    private SpringingImageView mSchoolyellow;
    private SpringingImageView mSchoolnews;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginpage);

        initUI();
        initSpringLayout();
        initEvent();
    }

    //用于展现效果
    private void initSpringLayout() {
        mTv_state.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, mTv_state));
        mLogout.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, mLogout).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        mIcon_image.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, mIcon_image).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        mNewclassmate.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, mNewclassmate).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        mSchoolyellow.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, mSchoolyellow).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        mSchoolnews.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, mSchoolnews).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
    }

    private void initUI() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }
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
        mFl_beginPage = (FrameLayout) findViewById(R.id.fl_beginPage);
        mNewclassmate = (SpringingImageView) findViewById(R.id.newclassmate);
        mSchoolyellow = (SpringingImageView) findViewById(R.id.schoolyellow);
        mSchoolnews = (SpringingImageView) findViewById(R.id.schoolnews);

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
                        mToolbar.setTitle("智慧南大");
                        break;
                    //点击设置
                    case R.id.nav_setting:
                        break;
                }
                return true;
            }
        });
    }

    private void initEvent() {
        mTv_state.setOnClickListener(this);
        mIcon_image.setOnClickListener(this);
        mLogout.setOnClickListener(this);
        mNewclassmate.setOnClickListener(this);
        mSchoolyellow.setOnClickListener(this);
        mSchoolnews.setOnClickListener(this);
    }

    //加载菜单的方法
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    //菜单点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mdrawer_layout.openDrawer(GravityCompat.START);
                break;
            case R.id.baidumap:
                break;
            case R.id.schoolpresentation:
                break;
            case R.id.schoolview:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击登录界面,弹出登录界面
            case R.id.icon_image:
                gotoLoginActivity();
                break;
            case R.id.tv_state:
                gotoLoginActivity();
                break;
            //点击登出界面
            case R.id.logout:
                gotoLoginOut();
                break;
            case R.id.newclassmate:
                replaceFragment(new NewSchoolMateFragment());
                mToolbar.setTitle("新生导航");
                mNewclassmate.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mSchoolyellow.setBackgroundColor(getResources().getColor(R.color.white));
                mSchoolnews.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.schoolyellow:
                replaceFragment(new SchoolYellowFragment());
                mToolbar.setTitle("校园生活");
                mSchoolyellow.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mNewclassmate.setBackgroundColor(getResources().getColor(R.color.white));
                mSchoolnews.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.schoolnews:
                replaceFragment(new SchoolNewsFragment());
                mToolbar.setTitle("学校新闻");
                mSchoolnews.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mNewclassmate.setBackgroundColor(getResources().getColor(R.color.white));
                mSchoolyellow.setBackgroundColor(getResources().getColor(R.color.white));
                break;
        }
    }

    //登出逻辑
    private void gotoLoginOut() {
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
    }

    //替换fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_beginPage, fragment);
        transaction.commit();
    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(BeginPageActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void initData() {

    }

}
