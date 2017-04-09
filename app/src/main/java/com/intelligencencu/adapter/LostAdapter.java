package com.intelligencencu.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intelligencencu.db.Lost;
import com.intelligencencu.intelligencencu.R;

import java.util.List;

/**
 * Created by liangzhan on 17-4-9.
 * 失物招领适配器
 */

public class LostAdapter extends RecyclerView.Adapter<LostAdapter.ViewHolder> {
    private List<Lost> mLost;
    private Context mcontext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_lostfound;
        LinearLayout ll_lf;
        TextView tv_lostfoundtitle;
        TextView tv_time;
        TextView tv_desc;

        public ViewHolder(View view) {
            super(view);
            ll_lf = (LinearLayout) view;
            ll_lostfound = (LinearLayout) view.findViewById(R.id.ll_lostfound);
            tv_lostfoundtitle = (TextView) view.findViewById(R.id.tv_lostfoundtitle);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_desc = (TextView) view.findViewById(R.id.tv_desc);
        }
    }

    public LostAdapter(List<Lost> list) {
        mLost = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mcontext == null) {
            mcontext = parent.getContext();
        }
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_lostfound, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Lost lost = mLost.get(position);
        if (lost.getFlag().equals("1")) {
            holder.tv_lostfoundtitle.setText("(丢失)" + lost.getTitle());
        } else if (lost.getFlag().equals("2")) {
            holder.tv_lostfoundtitle.setText("(招领)" + lost.getTitle());
        }
        holder.tv_time.setText(lost.getUpdatedAt());
        holder.tv_desc.setText(lost.getDescribe());
        holder.ll_lostfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDescDialog(lost);
            }
        });

        Log.d("lostAdapter", lost.getTitle() + lost.getDescribe());
    }

    private void showDescDialog(Lost lost) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        if (lost.getFlag().equals("1")) {
            builder.setTitle("(丢失)" + lost.getTitle());
        } else if (lost.getFlag().equals("2")) {
            builder.setTitle("(招领)" + lost.getTitle());
        }
        builder.setNegativeButton("返回", null);
        builder.setMessage("时间:" + lost.getUpdatedAt() + "\n" + "联系方式:" + lost.getPhone() + "\n" + "详细内容:" + "\n" + lost.getDescribe());
        builder.show();
    }

    @Override
    public int getItemCount() {
        return mLost.size();
    }
}
