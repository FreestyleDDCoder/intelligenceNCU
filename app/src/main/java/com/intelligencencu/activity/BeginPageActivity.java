package com.intelligencencu.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.intelligencencu.Fragment.begin.BeginPageFragment;
import com.intelligencencu.Fragment.Classmate.NewSchoolMateFragment;
import com.intelligencencu.Fragment.begin.SchoolMessageFragment;
import com.intelligencencu.Fragment.News.SchoolNewsFragment;
import com.intelligencencu.Fragment.begin.SchoolSceneryFragment;
import com.intelligencencu.Fragment.Schoollife.SchoolYellowFragment;
import com.intelligencencu.db.User;
import com.intelligencencu.intelligencencu.R;
import com.intelligencencu.utils.IsLogin;
import com.intelligencencu.utils.ToastUntil;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import dym.unique.com.springinglayoutlibrary.handler.SpringTouchRippleHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchPointHandler;
import dym.unique.com.springinglayoutlibrary.view.SpringingImageView;
import dym.unique.com.springinglayoutlibrary.view.SpringingTextView;

/**
 * Created by liangzhan on 17-3-21.
 * 这是应用的开始界面
 */

public class BeginPageActivity extends AppCompatActivity implements View.OnClickListener {


    private DrawerLayout mdrawer_layout;
    private NavigationView mNav_view;
    private SpringingImageView mIcon_image;
    //    private CircleImageView mIcon_image;
    private SpringingImageView mLogout;
    private SpringingTextView mTv_state;
    private SpringingImageView mNewclassmate;
    private SpringingImageView mSchoolyellow;
    private SpringingImageView mSchoolnews;
    private Toolbar mToolbar;
    private SpringingImageView siv_begin;
    private SpringingImageView siv_sexs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginpage);
        initUI();
        initSpringLayout();
        initEvent();
        //启动时配置默认信息
        initPersonalInfo();
        Log.d("onCreate", "onCreate");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //回到页面时再进行配置启动时配置默认信息
        //要理解生命周期的
        initPersonalInfo();
        Log.d("onRestart", "onRestart");
    }

    //用于展现效果
    private void initSpringLayout() {
        mTv_state.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, mTv_state));
        mLogout.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, mLogout).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        // mSl_beginPage.getSpringingHandlerController().addSpringingHandler(new SpringingTouchDragHandler(this, mSl_beginPage).setBackInterpolator(new OvershootInterpolator()).setBackDuration(SpringingTouchDragHandler.DURATION_LONG).setDirection(SpringingTouchDragHandler.DIRECTOR_BOTTOM | SpringingTouchDragHandler.DIRECTOR_TOP).setMinDistance(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics())));
        siv_sexs.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, siv_sexs).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        mIcon_image.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, mIcon_image).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        mNewclassmate.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, mNewclassmate).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        mSchoolyellow.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, mSchoolyellow).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        siv_begin.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, siv_begin).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        mSchoolnews.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, mSchoolnews).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
    }

    private void initUI() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.icon_m3);
        }

        mdrawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNav_view = (NavigationView) findViewById(R.id.nav_view);

        //注意不是当前ContentView是不可以直接使用findViewById的
        View headerView = mNav_view.getHeaderView(0);
//        mIcon_image = (CircleImageView) headerView.findViewById(R.id.icon_image);
        mIcon_image = (SpringingImageView) headerView.findViewById(R.id.icon_image);
        mIcon_image.setIsCircleImage(true);
        siv_sexs = (SpringingImageView) headerView.findViewById(R.id.siv_sexs);

        mLogout = (SpringingImageView) headerView.findViewById(R.id.logout);
        mTv_state = (SpringingTextView) headerView.findViewById(R.id.tv_state);

        siv_begin = (SpringingImageView) findViewById(R.id.siv_begin);
        mNewclassmate = (SpringingImageView) findViewById(R.id.newclassmate);
        mSchoolyellow = (SpringingImageView) findViewById(R.id.schoolyellow);
        mSchoolnews = (SpringingImageView) findViewById(R.id.schoolnews);
        siv_begin.setSelected(true);
        replaceFragment(new BeginPageFragment());

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
                        siv_begin.setSelected(true);
                        mNewclassmate.setSelected(false);
                        mSchoolyellow.setSelected(false);
                        mSchoolnews.setSelected(false);
                        mToolbar.setTitle("智慧南大");
                        replaceFragment(new BeginPageFragment());
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
        siv_begin.setOnClickListener(this);
        mIcon_image.setOnClickListener(this);
        mLogout.setOnClickListener(this);
        mNewclassmate.setOnClickListener(this);
        mSchoolyellow.setOnClickListener(this);
        mSchoolnews.setOnClickListener(this);
        mTv_state.setOnClickListener(this);
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
            //校园地图
            case R.id.baidumap:
                if (new IsLogin().isLogin()) {
                    checkpermission();
                } else {
                    ToastUntil.showShortToast(BeginPageActivity.this, "请先登录再使用本功能！");
                    gotoLoginActivity();
                }
                break;
            //学校概况
            case R.id.schoolpresentation:
                siv_begin.setSelected(false);
                mNewclassmate.setSelected(false);
                mSchoolyellow.setSelected(false);
                mSchoolnews.setSelected(false);
                mToolbar.setTitle("学校概况");
                replaceFragment(new SchoolMessageFragment());
                break;
            //校园风光
            case R.id.schoolview:
                siv_begin.setSelected(false);
                mNewclassmate.setSelected(false);
                mSchoolyellow.setSelected(false);
                mSchoolnews.setSelected(false);
                mToolbar.setTitle("校园风光");
                replaceFragment(new SchoolSceneryFragment());
                break;
        }
        return true;
    }

    private void gotoBaiduMap() {
        Intent intent = new Intent(BeginPageActivity.this, MapActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击登录界面,弹出登录界面
            case R.id.icon_image:
                if (new IsLogin().isLogin()) {
                    //如果已经登录则点击查看详细资料并进行修改
                } else {
                    gotoLoginActivity();
                }
                break;
            //点击登录界面,弹出登录界面
            case R.id.tv_state:
                if (new IsLogin().isLogin()) {
                    //如果已经登录则点击查看详细资料并进行修改
                } else {
                    gotoLoginActivity();
                }
                break;
            //点击登出界面
            case R.id.logout:
                gotoLoginOut();
                break;
            case R.id.siv_begin:
                mdrawer_layout.closeDrawers();
                siv_begin.setSelected(true);
                mNewclassmate.setSelected(false);
                mSchoolyellow.setSelected(false);
                mSchoolnews.setSelected(false);
                mToolbar.setTitle("智慧南大");
                replaceFragment(new BeginPageFragment());
                break;
            case R.id.newclassmate:
                replaceFragment(new NewSchoolMateFragment());
                mToolbar.setTitle("学生导航");
                siv_begin.setSelected(false);
                mNewclassmate.setSelected(true);
                mSchoolyellow.setSelected(false);
                mSchoolnews.setSelected(false);
                break;
            case R.id.schoolyellow:
                replaceFragment(new SchoolYellowFragment());
                mToolbar.setTitle("校园生活");
                siv_begin.setSelected(false);
                mNewclassmate.setSelected(false);
                mSchoolyellow.setSelected(true);
                mSchoolnews.setSelected(false);
                break;
            case R.id.schoolnews:
                replaceFragment(new SchoolNewsFragment());
                mToolbar.setTitle("学校新闻");
                siv_begin.setSelected(false);
                mNewclassmate.setSelected(false);
                mSchoolyellow.setSelected(false);
                mSchoolnews.setSelected(true);
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
                //清除本地缓存图片
                if (new IsLogin().isLogin()) {
                    User user = BmobUser.getCurrentUser(User.class);
                    String cacheDirPath = "/data/data/" + getApplicationContext().getPackageName() + "/cache/" + user.getUsername() + "icon.jpg";
                    new File(cacheDirPath).delete();
                    //清除缓存用户对象
                    BmobUser.logOut();
                    initPersonalInfo();
                } else {
                    ToastUntil.showShortToast(BeginPageActivity.this, "请先登录！");
                }
            }
        });
        builder.setNegativeButton("取消", null);
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

    //加载用户配置的方法
    private void initPersonalInfo() {
        User user = BmobUser.getCurrentUser(User.class);
        if (user != null) {
            siv_sexs.setVisibility(View.VISIBLE);
            Boolean sex = user.getSex();
            String username = user.getUsername();
            if (sex) {
                Glide.with(BeginPageActivity.this).load(R.mipmap.boy2).into(siv_sexs);
                mTv_state.setText(username);
            } else {
                Glide.with(BeginPageActivity.this).load(R.mipmap.nv2).into(siv_sexs);
                mTv_state.setText(username);
            }
            BmobFile bmobFile = user.getImage();
            if (bmobFile != null) {
                String cacheDirPath = "/data/data/" + getApplicationContext().getPackageName() + "/cache/" + bmobFile.getFilename();
                File file = new File(cacheDirPath);
                if (!file.exists()) {
                    bmobFile.download(file, new DownloadFileListener() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Log.d("", "加载成功！" + s);
                                Bitmap bitmap = getDiskBitmap(s);
                                mIcon_image.setImageBitmap(bitmap);
                            }
                        }

                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });
                } else {
                    Bitmap bitmap = getDiskBitmap(cacheDirPath);
                    mIcon_image.setImageBitmap(bitmap);
                }
            } else {
                Glide.with(BeginPageActivity.this).load(R.mipmap.default_user_head_img).into(mIcon_image);
               // mIcon_image.setImageDrawable(getResources().getDrawable(R.mipmap.default_user_head_img));
            }
        } else {
            mTv_state.setText("点击登录");
            siv_sexs.setVisibility(View.GONE);
            Glide.with(BeginPageActivity.this).load(R.mipmap.default_avatar_man).into(mIcon_image);
            //mIcon_image.setImageDrawable(getResources().getDrawable(R.mipmap.default_avatar_man));
        }
    }

    //获取BitMap图片
    private Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bitmap;
    }

    private void checkpermission() {
        //        运行时权限
        ArrayList<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(BeginPageActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(BeginPageActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(BeginPageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(BeginPageActivity.this, permissions, 1);
        } else {
            gotoBaiduMap();
        }
    }

    //运行时权限结果处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    Boolean isgotomap = true;
                    for (int result : grantResults) {
                        //当所有权限都开启后才进行地图活动
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(BeginPageActivity.this, "必须同意所有权限才能使用本功能", Toast.LENGTH_SHORT).show();
                            isgotomap = false;
                            return;
                        }
                    }
                    if (isgotomap)
                        gotoBaiduMap();
                } else {
                    Toast.makeText(BeginPageActivity.this, "发生未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    //以下为触摸监听接口，方便在Fragment中实现触摸事件
    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {
        boolean onTouch(MotionEvent ev);
    }
}
