package com.intelligencencu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.intelligencencu.db.BBS;
import com.intelligencencu.intelligencencu.R;

import java.util.List;

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
        if(bbs.isNoname()){

        }else {

        }
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

}
