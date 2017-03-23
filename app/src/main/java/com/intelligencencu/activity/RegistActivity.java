package com.intelligencencu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.RadioButton;
import android.widget.Toast;

import com.intelligencencu.db.User;
import com.intelligencencu.intelligencencu.R;
import com.intelligencencu.utils.MD5Utils;
import com.intelligencencu.utils.ToastUntil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import dym.unique.com.springinglayoutlibrary.handler.SpringTouchRippleHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingAlphaShowHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchDragHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchPointHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchScaleHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTranslationShowHandler;
import dym.unique.com.springinglayoutlibrary.view.SpringingEditText;
import dym.unique.com.springinglayoutlibrary.view.SpringingImageView;
import dym.unique.com.springinglayoutlibrary.view.SpringingTextView;
import dym.unique.com.springinglayoutlibrary.viewgroup.SpringingLinearLayout;
import dym.unique.com.springinglayoutlibrary.viewgroup.SpringingRelativeLayout;

/**
 * Created by liangzhan on 17-3-22.
 * 登陆界面
 */

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {

    private SpringingImageView mSimg_back;
    private SpringingImageView mSimg_avatarMan;
    private SpringingEditText mSedt_account;
    private SpringingEditText mSedt_emailHint;
    private SpringingEditText mSedt_password;
    private RadioButton mBoys;
    private RadioButton mGirls;
    private SpringingTextView mStv_regist;
    private SpringingRelativeLayout mSrl_actionBar;
    private SpringingLinearLayout mSll_mainContainer;
    private boolean boys = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initUI();
        initSpringLayout();
        initEvent();
        showViews();
    }

    private void showViews() {
        new SpringingAlphaShowHandler(this, mSll_mainContainer).showChildrenSequence(500, 100);
        new SpringingTranslationShowHandler(this, mSll_mainContainer).showChildrenSequence(500, 100);
    }

    private void initEvent() {
        mSimg_back.setOnClickListener(this);
        mSimg_avatarMan.setOnClickListener(this);
        mStv_regist.setOnClickListener(this);
        mBoys.setOnClickListener(this);
        mGirls.setOnClickListener(this);


    }

    private void initSpringLayout() {
        mSrl_actionBar.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, mSrl_actionBar).setOnlyOnChildren(true, mSimg_back));
        mSimg_back.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, mSimg_back).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        mSll_mainContainer.getSpringingHandlerController().addSpringingHandler(new SpringingTouchDragHandler(this, mSll_mainContainer).setBackInterpolator(new OvershootInterpolator()).setBackDuration(SpringingTouchDragHandler.DURATION_LONG).setDirection(SpringingTouchDragHandler.DIRECTOR_BOTTOM | SpringingTouchDragHandler.DIRECTOR_TOP).setMinDistance(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics())));
        mSll_mainContainer.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, mSll_mainContainer).setOnlyOnChildren(true, mSedt_account, mSedt_password));
        mSimg_avatarMan.getSpringingHandlerController().addSpringingHandler(new SpringingTouchScaleHandler(this, mSimg_avatarMan));
        mStv_regist.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, mStv_regist));

    }

    private void initUI() {
        mSrl_actionBar = (SpringingRelativeLayout) findViewById(R.id.srl_actionBar2);
        mSimg_back = (SpringingImageView) findViewById(R.id.simg_back2);
        mSll_mainContainer = (SpringingLinearLayout) findViewById(R.id.sll_mainContainer2);
        mSimg_avatarMan = (SpringingImageView) findViewById(R.id.simg_avatarMan2);
        mSimg_avatarMan.setIsCircleImage(true);
        mSedt_account = (SpringingEditText) findViewById(R.id.sedt_account2);
        mSedt_emailHint = (SpringingEditText) findViewById(R.id.sedt_EmailHint);
        mSedt_password = (SpringingEditText) findViewById(R.id.sedt_password2);
        mBoys = (RadioButton) findViewById(R.id.boys);
        mGirls = (RadioButton) findViewById(R.id.girls);
        mStv_regist = (SpringingTextView) findViewById(R.id.stv_regist);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回按钮
            case R.id.simg_back2:
                finish();
                break;
            //设置头像
            case R.id.simg_avatarMan2:

                break;
            //性别设置
            case R.id.boys:
                if (!boys) {
                    mBoys.setChecked(true);
                    mGirls.setChecked(false);
                }
                boys = true;
                break;
            case R.id.girls:
                if (boys) {
                    mBoys.setChecked(false);
                    mGirls.setChecked(true);
                }
                boys = false;
                break;
            case R.id.stv_regist:
                String emailHint = mSedt_emailHint.getText().toString();
                String account = mSedt_account.getText().toString();
                String password = mSedt_password.getText().toString();
                if (TextUtils.isEmpty(emailHint)) {
                    ToastUntil.showShortToast(RegistActivity.this, "邮箱不能为空！");
                } else if (TextUtils.isEmpty(account)) {
                    ToastUntil.showShortToast(RegistActivity.this, "用户名不能为空！");
                } else if (TextUtils.isEmpty(password)) {
                    ToastUntil.showShortToast(RegistActivity.this, "密码不能为空！");
                } else {
                    regist(emailHint, account, password);
                    checkEmail(emailHint);
                }
                break;
        }
    }

    private void checkEmail(String emailHint) {
        final String email = emailHint;
        BmobUser.requestEmailVerify(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUntil.showShortToast(RegistActivity.this, "请求验证邮件成功，请到" + email + "邮箱中进行激活,方便以后重置密码。");
                    finish();
                } else {
                    ToastUntil.showShortToast(RegistActivity.this, "请求验证邮件失败:" + e.getMessage());
                    finish();
                }
            }
        });
    }

    private void regist(String emailHint, String account, String password) {
        User user = new User();
        user.setEmail(emailHint);
        user.setUsername(account);
        user.setPassword(MD5Utils.MD5Encryption(password));
        if (mBoys.isChecked()) {
            user.setSex(true);
        } else {
            user.setSex(false);
        }
        user.setNick("");
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    //注册成功
                    ToastUntil.showShortToast(RegistActivity.this, "注册成功，你现在可以使用该账号了！");
                } else {
                    ToastUntil.showShortToast(RegistActivity.this, "用户名或电子邮件已经被另一个用户注册。");
                }
            }
        });
    }

//    private void gotoBeginActivity() {
//        Intent intent = new Intent(this, BeginPageActivity.class);
//        startActivity(intent);
//    }
}
