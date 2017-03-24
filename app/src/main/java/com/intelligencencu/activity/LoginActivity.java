package com.intelligencencu.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.intelligencencu.db.User;
import com.intelligencencu.intelligencencu.R;
import com.intelligencencu.utils.MD5Utils;
import com.intelligencencu.utils.ToastUntil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import dym.unique.com.springinglayoutlibrary.handler.SpringTouchRippleHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingAlphaHideHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingAlphaShowHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingNotificationJumpHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingNotificationRotateHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchDragHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchPointHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchScaleHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTranslationHideHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTranslationShowHandler;
import dym.unique.com.springinglayoutlibrary.view.SpringingEditText;
import dym.unique.com.springinglayoutlibrary.view.SpringingImageView;
import dym.unique.com.springinglayoutlibrary.view.SpringingTextView;
import dym.unique.com.springinglayoutlibrary.viewgroup.SpringingLinearLayout;
import dym.unique.com.springinglayoutlibrary.viewgroup.SpringingRelativeLayout;

/**
 * Created by liangzhan on 17-3-21.
 * 登录注册页面
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private SpringingRelativeLayout srl_actionBar;
    private SpringingImageView simg_back;
    private SpringingLinearLayout sll_mainContainer;
    private SpringingImageView simg_avatarMan;
    private SpringingEditText sedt_account;
    private SpringingEditText sedt_password;
    private SpringingTextView stv_login;
    private TextView tv_forgetpassword;
    private TextView tv_noregist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        initSpringLayout();
        initEvent();
        showViews();
    }

    private void showViews() {
        new SpringingAlphaShowHandler(this, sll_mainContainer).showChildrenSequence(500, 100);
        new SpringingTranslationShowHandler(this, sll_mainContainer).showChildrenSequence(500, 100);
        new SpringingNotificationRotateHandler(this, simg_avatarMan).start(1);
    }

    private void initEvent() {
        stv_login.setOnClickListener(this);
        simg_back.setOnClickListener(this);
        tv_noregist.setOnClickListener(this);
        tv_forgetpassword.setOnClickListener(this);
    }

    private void initSpringLayout() {
        srl_actionBar.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, srl_actionBar).setOnlyOnChildren(true, simg_back));
        simg_back.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, simg_back).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        sll_mainContainer.getSpringingHandlerController().addSpringingHandler(new SpringingTouchDragHandler(this, sll_mainContainer).setBackInterpolator(new OvershootInterpolator()).setBackDuration(SpringingTouchDragHandler.DURATION_LONG).setDirection(SpringingTouchDragHandler.DIRECTOR_BOTTOM | SpringingTouchDragHandler.DIRECTOR_TOP).setMinDistance(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics())));
        sll_mainContainer.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, sll_mainContainer).setOnlyOnChildren(true, sedt_account, sedt_password));
        simg_avatarMan.getSpringingHandlerController().addSpringingHandler(new SpringingTouchScaleHandler(this, simg_avatarMan));
        stv_login.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, stv_login));

    }

    private void initUI() {
        srl_actionBar = (SpringingRelativeLayout) findViewById(R.id.srl_actionBar);
        simg_back = (SpringingImageView) findViewById(R.id.simg_back);
        sll_mainContainer = (SpringingLinearLayout) findViewById(R.id.sll_mainContainer);
        simg_avatarMan = ((SpringingImageView) findViewById(R.id.simg_avatarMan)).setIsCircleImage(true);
        sedt_account = (SpringingEditText) findViewById(R.id.sedt_account);
        sedt_password = (SpringingEditText) findViewById(R.id.sedt_password);
        stv_login = (SpringingTextView) findViewById(R.id.stv_login);
        tv_noregist = (TextView) findViewById(R.id.tv_noregist);
        tv_forgetpassword = (TextView) findViewById(R.id.tv_forgetpassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stv_login:
                String account = sedt_account.getText().toString();
                String password = sedt_password.getText().toString();
                if (TextUtils.isEmpty(account)) {
                    ToastUntil.showShortToast(LoginActivity.this, "账号不能为空！");
                } else if (TextUtils.isEmpty(password)) {
                    ToastUntil.showShortToast(LoginActivity.this, "密码不能为空！");
                } else {
                    gotoLogin(account, password);
                }
                break;
            case R.id.tv_noregist:
                gotoRegister();
                break;
            case R.id.tv_forgetpassword:
                gotoResetPassword();
                break;
            case R.id.simg_back:
                finish();
                break;
        }
    }

    //密码重置功能
    private void gotoResetPassword() {
        startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
    }

    private void gotoLogin(String account, String password) {
        BmobUser.loginByAccount(account, MD5Utils.MD5Encryption(password), new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (user != null) {
                    //登录成功
                    ToastUntil.showShortToast(LoginActivity.this, "登录成功！");
                    finish();
                } else {
                    ToastUntil.showShortToast(LoginActivity.this,"账号或密码有误！请重试！");
                }
            }
        });
    }

    private void gotoRegister() {
        Intent intent = new Intent(this, RegistActivity.class);
        startActivity(intent);
        finish();
    }
}
