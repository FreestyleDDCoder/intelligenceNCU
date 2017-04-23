package com.intelligencencu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.intelligencencu.db.BBS;
import com.intelligencencu.db.User;
import com.intelligencencu.intelligencencu.R;
import com.intelligencencu.utils.ToastUntil;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liangzhan on 17-4-23.
 * 评论帖子活动
 */

public class CommentsActivity extends AppCompatActivity {

    private CircleImageView circ_userimage;
    private TextView bbs_name;
    private TextView bbs_time;
    private TextView tv_bbsdesc;
    private TextView tv_likenumber;
    private TextView tv_commentnumber;
    private ImageButton ib_delectbbs;
    private ImageView bbs_like;
    private ImageView bbs_comment;
    private ImageView iv_sex;
    private RecyclerView rlv_comment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        initView();
        initEvent();
        //帖子详情代码块
        postDetails();
    }

    private void initEvent() {

    }

    private void initView() {
        circ_userimage = (CircleImageView) findViewById(R.id.circ_userimage1);
        bbs_name = (TextView) findViewById(R.id.bbs_name1);
        bbs_time = (TextView) findViewById(R.id.bbs_time1);
        tv_bbsdesc = (TextView) findViewById(R.id.tv_bbsdesc1);
        tv_likenumber = (TextView) findViewById(R.id.tv_likenumber1);
        tv_commentnumber = (TextView) findViewById(R.id.tv_commentnumber1);
        ib_delectbbs = (ImageButton) findViewById(R.id.ib_delectbbs1);
        bbs_like = (ImageView) findViewById(R.id.bbs_like1);
        bbs_comment = (ImageView) findViewById(R.id.bbs_comment1);
        iv_sex = (ImageView) findViewById(R.id.iv_sex1);
        rlv_comment = (RecyclerView) findViewById(R.id.rlv_comment);
        Toolbar tl_comments = (Toolbar) findViewById(R.id.tl_comments);
        setSupportActionBar(tl_comments);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void postDetails() {
        Intent intent = this.getIntent();
        final BBS bbs = (BBS) intent.getSerializableExtra("bbs");
        //判断是否匿名消息
        if (bbs.isNoname()) {
            if (bbs.isSex()) {
                Glide.with(this).load(R.mipmap.boy3).into(circ_userimage);
                Glide.with(this).load(R.mipmap.boy2).into(iv_sex);
                bbs_name.setText("美男子");
            } else {
                Glide.with(this).load(R.mipmap.nv3).into(circ_userimage);
                Glide.with(this).load(R.mipmap.nv2).into(iv_sex);
                bbs_name.setText("萌妹子");
            }
        } else {
            if (bbs.isSex()) {
                Glide.with(this).load(R.mipmap.boy2).into(iv_sex);
            } else {
                Glide.with(this).load(R.mipmap.nv2).into(iv_sex);
            }
            User user = bbs.getUsername();
            BmobFile image = user.getImage();
            if (image == null) {
                Glide.with(this).load(R.mipmap.default_user_head_img).into(circ_userimage);
            } else {
                Glide.with(this).load(image.getFileUrl()).into(circ_userimage);
            }
            String username = user.getUsername();
            Log.d("username", username);
            bbs_name.setText(username);
        }
        bbs_time.setText(bbs.getUpdatedAt());
        tv_bbsdesc.setText(bbs.getDesc());
        tv_likenumber.setText("" + bbs.getLikes());
        tv_commentnumber.setText("" + bbs.getReview());
        BmobUser user = BmobUser.getCurrentUser();
        if (user != null) {
            if (user.getUsername().equals(bbs.getUsername().getUsername())) {
                Log.d("acl", "到了这里！");
                ib_delectbbs.setVisibility(View.VISIBLE);
                ib_delectbbs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CommentsActivity.this);
                        builder.setTitle("注意！");
                        builder.setMessage("删除后不可恢复，确认这么做吗？");
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //点击删除按钮
                                deleBbs(bbs);
                            }
                        });
                        builder.show();
                    }
                });
            } else {
                ib_delectbbs.setVisibility(View.INVISIBLE);
            }
        }
    }
    //删除提示框
    private void deleBbs(BBS bbs) {
        BBS bbs1 = new BBS();
        bbs1.setObjectId(bbs.getObjectId());
        bbs1.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                ToastUntil.showShortToast(CommentsActivity.this, e == null ? "删除成功，请下拉刷新数据！" : "删除失败！" + e);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
