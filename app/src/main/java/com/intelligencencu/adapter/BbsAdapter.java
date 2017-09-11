package com.intelligencencu.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.intelligencencu.activity.CommentsActivity;
import com.intelligencencu.db.BBS;
import com.intelligencencu.db.User;
import com.intelligencencu.intelligencencu.R;
import com.intelligencencu.utils.ToastUntil;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liangzhan on 4/21/17.
 * 帖子的adapter
 */

public class BbsAdapter extends RecyclerView.Adapter<BbsAdapter.ViewHolder> {
    private List<BBS> mlist;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circ_userimage;
        TextView bbs_name;
        TextView bbs_time;
        TextView tv_bbsdesc;
        TextView tv_likenumber;
        TextView tv_commentnumber;
        ImageButton ib_delectbbs;
        ImageView bbs_like;
        ImageView bbs_comment;
        ImageView iv_sex;

        public ViewHolder(View view) {
            super(view);
            circ_userimage = (CircleImageView) view.findViewById(R.id.circ_userimage);
            bbs_name = (TextView) view.findViewById(R.id.bbs_name);
            bbs_time = (TextView) view.findViewById(R.id.bbs_time);
            tv_bbsdesc = (TextView) view.findViewById(R.id.tv_bbsdesc);
            tv_likenumber = (TextView) view.findViewById(R.id.tv_likenumber);
            tv_commentnumber = (TextView) view.findViewById(R.id.tv_commentnumber);
            ib_delectbbs = (ImageButton) view.findViewById(R.id.ib_delectbbs);
            bbs_like = (ImageView) view.findViewById(R.id.bbs_like);
            bbs_comment = (ImageView) view.findViewById(R.id.bbs_comment);
            iv_sex = (ImageView) view.findViewById(R.id.iv_sex);
        }
    }

    public BbsAdapter(List<BBS> list) {
        mlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bbs, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final BBS bbs = mlist.get(position);
        //判断是否匿名消息
        if (bbs.isNoname()) {
            if (bbs.isSex()) {
                Glide.with(mContext).load(R.mipmap.boy3).into(holder.circ_userimage);
                Glide.with(mContext).load(R.mipmap.boy2).into(holder.iv_sex);
                holder.bbs_name.setText("美男子");
            } else {
                Glide.with(mContext).load(R.mipmap.nv3).into(holder.circ_userimage);
                Glide.with(mContext).load(R.mipmap.nv2).into(holder.iv_sex);
                holder.bbs_name.setText("萌妹子");
            }
        } else {
            if (bbs.isSex()) {
                Glide.with(mContext).load(R.mipmap.boy2).into(holder.iv_sex);
            } else {
                Glide.with(mContext).load(R.mipmap.nv2).into(holder.iv_sex);
            }
            User user = bbs.getUsername();
            BmobFile image = user.getImage();
            //Log.d("image信息",image.toString());
            if (image == null) {
                Glide.with(mContext).load(R.mipmap.default_user_head_img).into(holder.circ_userimage);
            } else {
                Glide.with(mContext).load(image.getFileUrl()).into(holder.circ_userimage);
            }
            String username = user.getUsername();
            Log.d("username", username);
            holder.bbs_name.setText(username);
        }
        holder.bbs_time.setText(bbs.getUpdatedAt());
        holder.tv_bbsdesc.setText(bbs.getDesc());
        holder.tv_likenumber.setText("" + bbs.getLikes());
        holder.tv_commentnumber.setText("" + bbs.getReview());
        BmobUser user = BmobUser.getCurrentUser();
        if (user != null) {
            if (user.getUsername().equals(bbs.getUsername().getUsername())) {
                Log.d("acl", "到了这里！");
                holder.ib_delectbbs.setVisibility(View.VISIBLE);
                holder.ib_delectbbs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
                holder.ib_delectbbs.setVisibility(View.INVISIBLE);
            }
        }

        final String objectId = bbs.getObjectId();
        Log.d("objectId", objectId);
        holder.bbs_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToastUntil.showShortToast(mContext, "喜欢功能待续...");
                //更新喜欢帖子信息
                bbs.setLikes(bbs.getLikes() + 1);
                bbs.update(objectId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.d("喜欢更新成功：", null);
                            notifyItemChanged(position);
                        } else {
                            Log.d("喜欢更新失败：", e.toString());
                        }
                    }
                });
            }
        });

        //当点击文章内容或者点击评论图标时进入发帖详细页面
        holder.tv_bbsdesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoComment(bbs);
            }
        });
        holder.bbs_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoComment(bbs);
            }
        });
        //点击头像可以查看发帖人的详细信息
        holder.circ_userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //进入评论界面
    private void gotoComment(BBS bbs) {
        Intent intent = new Intent(mContext, CommentsActivity.class);
        //传递对象需要实现序列化接口
        Bundle bundle = new Bundle();
        bundle.putSerializable("bbs", bbs);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    private void deleBbs(BBS bbs) {
        BBS bbs1 = new BBS();
        bbs1.setObjectId(bbs.getObjectId());
        bbs1.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                ToastUntil.showShortToast(mContext, e == null ? "删除成功，请下拉刷新数据！" : "删除失败！" + e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

}
