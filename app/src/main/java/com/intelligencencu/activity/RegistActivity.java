package com.intelligencencu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.RadioButton;

import com.intelligencencu.intelligencencu.R;

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
        switch (v.getId()){
            case R.id.simg_back2:
                finish();
                break;
            case R.id.simg_avatarMan2:
                break;
            case R.id.stv_regist:
                break;
        }
    }
}
