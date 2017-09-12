package com.intelligencencu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.intelligencencu.adapter.CommentAdapter;
import com.intelligencencu.db.BBS;
import com.intelligencencu.db.Comment;
import com.intelligencencu.db.User;
import com.intelligencencu.intelligencencu.R;
import com.intelligencencu.utils.ToastUntil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
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
    private ImageButton ib_comments;
    private EditText et_comment;
    private BBS bbs;
    private SwipeRefreshLayout swip_comment;

    //设置两个常亮，用于标记是下拉刷新还是上拉刷新
    private static final int STATE_REFRESH = 0;
    private static final int STATE_MORE = 1;

    private int count = 20;        // 每页的数据是20条
    private int curPage = 0;        // 当前页的编号，从0开始
    private ArrayList<Comment> mcommentList;
    private CommentAdapter commentAdapter;
    private boolean isFirstLastPosition = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        initView();
        initEvent();
        //帖子详情代码块
        postDetails();
        //评论部分
        getDate(0, STATE_REFRESH);
    }

    private void initEvent() {
        //评论按钮点击监听
        ib_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoComment(bbs);
            }
        });
        //喜欢监听
        bbs_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新喜欢帖子信息
                String objectId = bbs.getObjectId();
                final int likes = bbs.getLikes();
                Log.d("objectId", objectId);
                BBS bbs1 = new BBS();
                bbs1.setLikes(likes + 1);
                bbs.setLikes(likes + 1);
                bbs1.update(objectId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.d("喜欢更新成功：", null);
                            tv_likenumber.setText(likes);
                        } else {
                            Log.d("喜欢更新失败：", e.toString());
                        }
                    }
                });
            }
        });
        //下拉刷新
        swip_comment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDate(0, STATE_REFRESH);
            }
        });
        //滑动监听
        rlv_comment.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition == count * curPage - 1) {
                        if (isFirstLastPosition) {
                            isFirstLastPosition = false;
                            ToastUntil.showShortToast(CommentsActivity.this, "加载更多ing...");
                            getDate(curPage, STATE_MORE);
                        }
                    }
                }
            }
        });
    }

    private void initView() {
        //获取传递过来的bbs对象
        Intent intent = this.getIntent();
        bbs = (BBS) intent.getSerializableExtra("bbs");

        circ_userimage = (CircleImageView) findViewById(R.id.circ_userimage1);
        bbs_name = (TextView) findViewById(R.id.bbs_name1);
        bbs_time = (TextView) findViewById(R.id.bbs_time1);
        tv_bbsdesc = (TextView) findViewById(R.id.tv_bbsdesc1);
        tv_likenumber = (TextView) findViewById(R.id.tv_likenumber1);
        tv_commentnumber = (TextView) findViewById(R.id.tv_commentnumber1);
        ib_delectbbs = (ImageButton) findViewById(R.id.ib_delectbbs1);
        bbs_like = (ImageView) findViewById(R.id.bbs_like1);
        iv_sex = (ImageView) findViewById(R.id.iv_sex1);
        rlv_comment = (RecyclerView) findViewById(R.id.rlv_comment);
        //评论文本和评论按钮
        ib_comments = (ImageButton) findViewById(R.id.ib_comments);
        et_comment = (EditText) findViewById(R.id.et_comment);

        swip_comment = (SwipeRefreshLayout) findViewById(R.id.swip_comment);
        swip_comment.setColorSchemeResources(R.color.colorPrimary);
        swip_comment.setRefreshing(true);
        mcommentList = new ArrayList<>();

        Toolbar tl_comments = (Toolbar) findViewById(R.id.tl_comments);
        setSupportActionBar(tl_comments);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void postDetails() {
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
        tv_commentnumber.setText("0");
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    //发表评论的方法
    private void gotoComment(BBS post) {
        String comments = et_comment.getText().toString();
        Log.d("comments:", comments);
        if (!TextUtils.isEmpty(comments)) {
            //角色管理
            BmobACL acl = new BmobACL();
            User user = BmobUser.getCurrentUser(User.class);
            acl.setPublicReadAccess(true);
            acl.setWriteAccess(user, true);
            //创建评论信息
            Comment comment = new Comment();

            comment.setACL(acl);
            comment.setUsername(user);
            comment.setComment(comments);
            comment.setPost(post);

            comment.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        ToastUntil.showShortToast(CommentsActivity.this, "评论成功!");
                        getDate(0, STATE_REFRESH);
                        et_comment.clearFocus();
                        et_comment.setText(null);
                    } else {
                        ToastUntil.showShortToast(CommentsActivity.this, "评论失败，请重试！");
                    }
                }
            });
        } else {
            ToastUntil.showShortToast(CommentsActivity.this, "内容不能为空！");
        }
    }

    //获取服务器数据的方法

    /**
     * @param page        分页查找的页码
     * @param refreshType 刷新的类型
     */
    private void getDate(int page, final int refreshType) {
        BmobQuery<Comment> query = new BmobQuery<>();
        //按照时间降序
        query.order("-createdAt");
        // 如果是加载更多
        if (refreshType == STATE_MORE) {
            // 跳过之前页数并去掉重复数据
            query.setSkip(page * count);
        } else {
            page = 0;
            query.setSkip(page);
        }
        query.setLimit(count);
        query.include("username");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.addWhereEqualTo("post", bbs);
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        Log.d("list.size()", "" + list.size());
                        //设置帖子详情页的评论数
                        tv_commentnumber.setText("" + list.size());
                        if (refreshType == STATE_REFRESH) {
                            // 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
                            curPage = 0;
                            if (mcommentList != null)
                                mcommentList.clear();
                        }
                        for (Comment comment : list) {
                            mcommentList.add(comment);
                            Log.d("Comment", "" + comment.getObjectId());
                        }
                        //mLostlist = list;
                        // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                        curPage++;
                        isFirstLastPosition = true;
                    } else if (refreshType == STATE_MORE) {
                        ToastUntil.showShortToast(CommentsActivity.this, "没有更多数据了");
                    } else if (refreshType == STATE_REFRESH) {
                        ToastUntil.showShortToast(CommentsActivity.this, "没有数据");
                    }
                    initDate();
                } else {
                    ToastUntil.showShortToast(CommentsActivity.this, "查询失败！" + e);
                }
            }
        });
    }

    //链接适配器
    private void initDate() {
        if (mcommentList != null) {
            if (commentAdapter == null) {
                commentAdapter = new CommentAdapter(mcommentList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(CommentsActivity.this);
                rlv_comment.setLayoutManager(layoutManager);
                rlv_comment.setAdapter(commentAdapter);
            } else {
                commentAdapter.notifyDataSetChanged();
            }
        } else {
            ToastUntil.showShortToast(CommentsActivity.this, "当前没有数据！");
        }
        swip_comment.setRefreshing(false);
    }
}
