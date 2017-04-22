package com.intelligencencu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.intelligencencu.db.BBS;
import com.intelligencencu.db.User;
import com.intelligencencu.intelligencencu.R;
import com.intelligencencu.utils.ToastUntil;

import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import dym.unique.com.springinglayoutlibrary.view.SpringingEditText;

/**
 * Created by liangzhan on 4/20/17.
 * 用于发帖子的活动
 */

public class BbsActivity extends AppCompatActivity {

    private SpringingEditText set_bbsdesc;
    private Toolbar tb_bbs;
    private CheckBox cb_noname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbs);
        initview();
    }

    private void initview() {
        set_bbsdesc = (SpringingEditText) findViewById(R.id.set_bbsdesc);
        cb_noname = (CheckBox) findViewById(R.id.cb_noname);
        tb_bbs = (Toolbar) findViewById(R.id.tb_bbs);
        setSupportActionBar(tb_bbs);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bbstoolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.bbs_public:
                //发表帖子方法
                gotoPublish();
                break;
        }
        return true;
    }

    private void gotoPublish() {
        String desc = set_bbsdesc.getText().toString();
        if (!TextUtils.isEmpty(desc)) {
            boolean checked = cb_noname.isChecked();
            //角色管理
            BmobACL acl = new BmobACL();
            User user = BmobUser.getCurrentUser(User.class);
            acl.setPublicReadAccess(true);
            acl.setWriteAccess(user, true);
            //创建帖子信息
            BBS bbs = new BBS();
            bbs.setACL(acl);
            bbs.setUsername(user);
            bbs.setDesc(desc);
            bbs.setNoname(checked);
            bbs.setImage(user.getImage());
            bbs.setSex(user.getSex());
            bbs.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        ToastUntil.showShortToast(BbsActivity.this, "发表成功，请下拉刷新！");
                        finish();
                    } else {
                        ToastUntil.showShortToast(BbsActivity.this, "发表失败，请重试！");
                    }
                }
            });
        } else {
            ToastUntil.showShortToast(BbsActivity.this, "内容不能为空！");
        }
    }
}
