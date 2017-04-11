package com.intelligencencu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.intelligencencu.db.Lost;
import com.intelligencencu.intelligencencu.R;
import com.intelligencencu.utils.ToastUntil;

import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import dym.unique.com.springinglayoutlibrary.handler.SpringTouchRippleHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingAlphaShowHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchDragHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTranslationShowHandler;
import dym.unique.com.springinglayoutlibrary.view.SpringingButton;
import dym.unique.com.springinglayoutlibrary.view.SpringingEditText;
import dym.unique.com.springinglayoutlibrary.view.SpringingTextView;
import dym.unique.com.springinglayoutlibrary.viewgroup.SpringingLinearLayout;

/**
 * Created by liangzhan on 17-4-9.
 * 登记丢失信息的界面
 */

public class LostFoundActivity extends AppCompatActivity {

    private SpringingLinearLayout sll_lostfound;
    private SpringingTextView stv_title;
    private SpringingEditText set_title;
    private SpringingTextView stv_phone;
    private SpringingEditText set_phone;
    private SpringingTextView stv_desc;
    private SpringingEditText set_desc;
    private SpringingButton sb_lost;
    private SpringingButton sb_found;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initSpringLayout();
        initEvent();
        showViews();
    }

    private void showViews() {
        new SpringingAlphaShowHandler(this, sll_lostfound).showChildrenSequence(500, 100);
        new SpringingTranslationShowHandler(this, sll_lostfound).showChildrenSequence(500, 100);
    }

    private void initSpringLayout() {
        sll_lostfound.getSpringingHandlerController().addSpringingHandler(new SpringingTouchDragHandler(this, sll_lostfound).setBackInterpolator(new OvershootInterpolator()).setBackDuration(SpringingTouchDragHandler.DURATION_LONG).setDirection(SpringingTouchDragHandler.DIRECTOR_BOTTOM | SpringingTouchDragHandler.DIRECTOR_TOP).setMinDistance(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics())));
        sll_lostfound.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, sll_lostfound).setOnlyOnChildren(true, set_title, set_phone, set_desc));

        stv_title.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, stv_title));
        stv_phone.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, stv_title));
        stv_desc.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, stv_desc));
        set_title.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, set_title));
        set_phone.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, set_phone));
        set_desc.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, set_desc));

        sb_lost.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, sb_lost));
        sb_found.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, sb_found));
    }

    private void initEvent() {
        //丢失点击监听
        sb_lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLostFound("1");
            }
        });

        sb_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLostFound("2s");
            }
        });

    }

    private void gotoLostFound(String s) {

        String title = set_title.getText().toString();
        String phone = set_phone.getText().toString();
        String desc = set_desc.getText().toString();
        if (TextUtils.isEmpty(title)) {
            ToastUntil.showShortToast(LostFoundActivity.this, "展标题不能为空！");
        } else if (TextUtils.isEmpty(phone)) {
            ToastUntil.showShortToast(LostFoundActivity.this, "联系方式不能为空！");
        } else if (TextUtils.isEmpty(desc)) {
            ToastUntil.showShortToast(LostFoundActivity.this, "详细描述不能为空！");
        } else {
            //创建一个ACL对象
            BmobACL acl = new BmobACL();
            // 设置所有人可读的权限
            acl.setPublicReadAccess(true);
            // 设置当前用户可写的权限
            BmobUser currentUser = BmobUser.getCurrentUser();
            acl.setWriteAccess(currentUser, true);
            Lost lost = new Lost();
            lost.setDescribe(desc);
            lost.setPhone(phone);
            lost.setTitle(title);
            lost.setFlag(s);
            lost.setUsername(currentUser.getUsername());
            lost.setACL(acl);
            lost.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        //信息添加成功
                        ToastUntil.showShortToast(LostFoundActivity.this, s.equals("1") ? "添加丢失成功,请下拉刷新数据！" : "添加招领成功，请下拉刷新数据！");
                        finish();
                    } else {
                        ToastUntil.showShortToast(LostFoundActivity.this, "添加失败" + e);
                    }
                }
            });
        }
    }

    private void initView() {
        setContentView(R.layout.activity_lostfound);
        Toolbar tb_lostfound = (Toolbar) findViewById(R.id.tb_lostfound);
        setSupportActionBar(tb_lostfound);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        sll_lostfound = (SpringingLinearLayout) findViewById(R.id.sll_lostfound);
        stv_title = (SpringingTextView) findViewById(R.id.stv_title);
        set_title = (SpringingEditText) findViewById(R.id.set_title);
        stv_phone = (SpringingTextView) findViewById(R.id.stv_phone);
        set_phone = (SpringingEditText) findViewById(R.id.set_phone);
        stv_desc = (SpringingTextView) findViewById(R.id.stv_desc);
        set_desc = (SpringingEditText) findViewById(R.id.set_desc);
        sb_lost = (SpringingButton) findViewById(R.id.sb_lost);
        sb_found = (SpringingButton) findViewById(R.id.sb_found);
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
}
