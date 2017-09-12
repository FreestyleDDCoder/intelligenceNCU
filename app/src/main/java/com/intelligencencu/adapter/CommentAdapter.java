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
import com.intelligencencu.db.Comment;
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
 * Created by liangzhan on 17-9-12.
 * 这是评论界面的适配器
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> mlist;
    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Comment comment = mlist.get(position);
        User user = comment.getUsername();
        Boolean sex = user.getSex();
        if (sex) {
            Glide.with(mContext).load(R.mipmap.boy2).into(holder.iv_commentsex);
        } else {
            Glide.with(mContext).load(R.mipmap.nv2).into(holder.iv_commentsex);
        }
        BmobFile image = user.getImage();
        //Log.d("image信息",image.toString());
        if (image == null) {
            Glide.with(mContext).load(R.mipmap.default_user_head_img).into(holder.circ_comUserimage);
        } else {
            Glide.with(mContext).load(image.getFileUrl()).into(holder.circ_comUserimage);
        }
        String username = user.getUsername();
        Log.d("username", username);
        holder.comment_name.setText(username);

        holder.comment_time.setText(mlist.size() - position + "楼  " + comment.getUpdatedAt());
        holder.tv_comment.setText(comment.getComment());

        BmobUser commentUser = BmobUser.getCurrentUser();
        if (commentUser != null) {
            if (commentUser.getUsername().equals(username)) {
                Log.d("acl", "到了这里！");
                holder.ib_delectcomment.setVisibility(View.VISIBLE);
                holder.ib_delectcomment.setOnClickListener(new View.OnClickListener() {
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
                                deleComs(comment, position);
                            }
                        });
                        builder.show();
                    }
                });
            } else {
                holder.ib_delectcomment.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void deleComs(Comment comment, final int position) {
        Comment comment1 = new Comment();
        comment1.setObjectId(comment.getObjectId());
        comment1.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                mlist.remove(position);
                notifyDataSetChanged();
                ToastUntil.showShortToast(mContext, e == null ? "删除成功！" : "删除失败！" + e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        //实现显示评论
        CircleImageView circ_comUserimage;
        TextView comment_name;
        TextView comment_time;
        TextView tv_comment;
        ImageView iv_commentsex;
        ImageButton ib_delectcomment;

        public ViewHolder(View itemView) {
            super(itemView);
            circ_comUserimage = (CircleImageView) itemView.findViewById(R.id.circ_comUserimage);
            comment_name = (TextView) itemView.findViewById(R.id.comment_name);
            comment_time = (TextView) itemView.findViewById(R.id.comment_time);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            iv_commentsex = (ImageView) itemView.findViewById(R.id.iv_commentsex);
            ib_delectcomment = (ImageButton) itemView.findViewById(R.id.ib_delectcomment);
        }
    }

    //构造方法,用于传值
    public CommentAdapter(List<Comment> mlist) {
        this.mlist = mlist;
    }
}
