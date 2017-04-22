package com.intelligencencu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.intelligencencu.db.BBS;
import com.intelligencencu.intelligencencu.R;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
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
        BBS bbs = mlist.get(position);
        //判断是否匿名消息
        if (bbs.isNoname()) {
            holder.circ_userimage.setBackgroundResource(R.mipmap.default_user_head_img);
            if (bbs.isSex())
                holder.bbs_name.setText("不一样的美男子");
            holder.bbs_name.setText("不一样的美女子");
            holder.bbs_time.setText(bbs.getUpdatedAt());
            holder.tv_bbsdesc.setText(bbs.getDesc());
            holder.tv_likenumber.setText(bbs.getLikes());
            holder.tv_commentnumber.setText(bbs.getReview());
        } else {
            Glide.with(mContext).load(bbs.getImage().getFileUrl()).into(holder.circ_userimage);
            holder.bbs_name.setText(bbs.getUsername().getUsername());
            holder.bbs_time.setText(bbs.getUpdatedAt());
            holder.tv_bbsdesc.setText(bbs.getDesc());
            holder.tv_likenumber.setText(bbs.getLikes());
            holder.tv_commentnumber.setText(bbs.getReview());
        }
        holder.bbs_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
      holder.bbs_comment.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

          }
      });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

}
