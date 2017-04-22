package com.intelligencencu.adapter;

import android.content.Context;
import android.content.DialogInterface;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BBS bbs = mlist.get(position);
        //判断是否匿名消息
        if (bbs.isNoname()) {
            Glide.with(mContext).load(R.mipmap.default_user_head_img).into(holder.circ_userimage);
            if (bbs.isSex()) {
                holder.bbs_name.setText("一枚美男子");
            } else {
                holder.bbs_name.setText("一枚萌妹子");
            }
        } else {
            User user = bbs.getUsername();
            Glide.with(mContext).load(user.getImage().getFileUrl()).into(holder.circ_userimage);
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
            }
        }
        holder.bbs_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUntil.showShortToast(mContext, "喜欢功能待续...");
            }
        });
        holder.bbs_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUntil.showShortToast(mContext, "评论功能待续...");
            }
        });
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
