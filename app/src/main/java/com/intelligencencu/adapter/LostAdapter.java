package com.intelligencencu.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intelligencencu.db.BBS;
import com.intelligencencu.db.Lost;
import com.intelligencencu.intelligencencu.R;
import com.intelligencencu.utils.ToastUntil;

import java.util.List;

import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

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
        TextView tv_lfusername;

        public ViewHolder(View view) {
            super(view);
            ll_lf = (LinearLayout) view;
            ll_lostfound = (LinearLayout) view.findViewById(R.id.ll_lostfound);
            tv_lostfoundtitle = (TextView) view.findViewById(R.id.tv_lostfoundtitle);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            tv_lfusername = (TextView) view.findViewById(R.id.tv_lfusername);
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Lost lost = mLost.get(position);
        if (lost.getFlag().equals("1")) {
            holder.tv_lostfoundtitle.setText("(丢失)" + lost.getTitle());
        } else if (lost.getFlag().equals("2")) {
            holder.tv_lostfoundtitle.setText("(招领)" + lost.getTitle());
        }
        holder.tv_lfusername.setText(lost.getUsername());
        holder.tv_time.setText(lost.getUpdatedAt());
        holder.tv_desc.setText(lost.getDescribe());
        holder.ll_lostfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDescDialog(lost, position);
            }
        });
        Log.d("lostAdapter", lost.getTitle() + lost.getDescribe());
    }

    private void showDescDialog(final Lost lost, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        if (lost.getFlag().equals("1")) {
            builder.setTitle("(丢失)" + lost.getTitle());
        } else if (lost.getFlag().equals("2")) {
            builder.setTitle("(招领)" + lost.getTitle());
        }
        builder.setNegativeButton("返回", null);
        //如果拥有权限，则显示删除
        BmobUser currentUser = BmobUser.getCurrentUser();
        if (currentUser != null) {
            if (currentUser.getUsername().equals(lost.getUsername())) {
                Log.d("acl", "到了这里！");
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delLostFound(lost, position);
                    }
                });
            }
        }
        builder.setMessage("发布人：" + lost.getUsername() + "\n" + "时间:" + lost.getUpdatedAt() + "\n" + "联系方式:" + lost.getPhone() + "\n" + "详细内容:" + "\n" + lost.getDescribe());
        builder.show();
    }

    private void delLostFound(final Lost lost, final int position) {
        Lost lost1 = new Lost();
        lost1.setObjectId(lost.getObjectId());
        lost1.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUntil.showShortToast(mcontext, "删除成功，请下拉刷新数据！");
                    //这里把数据从list中除去，然后刷新适配器，避免手动刷新（提升用户体验）
                    //mLost.remove(position);
                } else {
                    ToastUntil.showShortToast(mcontext, "删除失败！" + e);
                    Log.d("删除失败",e+"");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLost.size();
    }
}
