package com.intelligencencu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.intelligencencu.intelligencencu.R;
import com.intelligencencu.utils.ToastUntil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import dym.unique.com.springinglayoutlibrary.handler.SpringTouchRippleHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingAlphaShowHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchDragHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchPointHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTranslationShowHandler;
import dym.unique.com.springinglayoutlibrary.view.SpringingEditText;
import dym.unique.com.springinglayoutlibrary.view.SpringingImageView;
import dym.unique.com.springinglayoutlibrary.view.SpringingTextView;
import dym.unique.com.springinglayoutlibrary.viewgroup.SpringingLinearLayout;

/**
 * Created by liangzhan on 17-3-23.
 * 重置密码界面
 */

public class ResetPasswordActivity extends AppCompatActivity {

    private SpringingImageView mSimg_back;
    private SpringingEditText sedt_resetmail;
    private SpringingTextView stv_reset;
    private SpringingLinearLayout mSll_mainContainer;
    private SpringingImageView mSimg_eMail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initSpringLayout();
        initEvent();
        showViews();
    }

    private void initSpringLayout() {
        mSll_mainContainer.getSpringingHandlerController().addSpringingHandler(new SpringingTouchDragHandler(this, mSll_mainContainer).setBackInterpolator(new OvershootInterpolator()).setBackDuration(SpringingTouchDragHandler.DURATION_LONG).setDirection(SpringingTouchDragHandler.DIRECTOR_BOTTOM | SpringingTouchDragHandler.DIRECTOR_TOP).setMinDistance(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics())));
        mSll_mainContainer.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, mSll_mainContainer).setOnlyOnChildren(true, sedt_resetmail));
        mSimg_back.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, mSimg_back).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        mSimg_eMail.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, mSimg_eMail).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        stv_reset.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, stv_reset));
    }

    private void showViews() {
        new SpringingAlphaShowHandler(this, mSll_mainContainer).showChildrenSequence(500, 100);
        new SpringingTranslationShowHandler(this, mSll_mainContainer).showChildrenSequence(500, 100);
    }

    private void initUI() {
        setContentView(R.layout.activity_resetpassword);
        mSll_mainContainer = (SpringingLinearLayout) findViewById(R.id.sll_mainContainer3);
        mSimg_back = (SpringingImageView) findViewById(R.id.simg_back3);
        sedt_resetmail = (SpringingEditText) findViewById(R.id.sedt_resetmail);
        stv_reset = (SpringingTextView) findViewById(R.id.stv_reset);
        mSimg_eMail = (SpringingImageView) findViewById(R.id.simg_eMail);
        mSimg_eMail.setIsCircleImage(true);
    }

    private void initEvent() {
        mSimg_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        stv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToEMail();
            }
        });
    }

    private void sendToEMail() {
        final String eMail = sedt_resetmail.getText().toString();
        if (!TextUtils.isEmpty(eMail)) {
            BmobUser.resetPasswordByEmail(eMail, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        ToastUntil.showShortToast(ResetPasswordActivity.this, "重置密码请求成功，请到" + eMail + "邮箱进行密码重置操作");
                    } else {
                        ToastUntil.showShortToast(ResetPasswordActivity.this, e.getMessage());
                    }
                }
            });
        } else {
            ToastUntil.showShortToast(ResetPasswordActivity.this, "邮箱不能为空！");
        }
    }
}
