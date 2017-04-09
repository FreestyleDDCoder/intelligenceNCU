package com.intelligencencu.Fragment.Schoollife;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.intelligencencu.activity.LostFoundActivity;
import com.intelligencencu.adapter.LostAdapter;
import com.intelligencencu.db.Found;
import com.intelligencencu.db.Lost;
import com.intelligencencu.intelligencencu.R;
import com.intelligencencu.utils.ToastUntil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by liangzhan on 17-3-28.
 * 失物招领Fragment
 */

public class LostFoundFragment extends Fragment {

    private FloatingActionButton float_lostfound;
    private SwipeRefreshLayout swip_lostfound;
    private RecyclerView rlv_lostfound;
    private List<Lost> mLostlist;
    //    private List<Found> mFoundlist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lostfound, container, false);
        initView(view);
        initEvent();
        return view;
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initDate();
            Log.d("initDate()", "到这里了");
        }
    };

    private void initDate() {
        if (mLostlist != null) {
            rlv_lostfound.setAdapter(new LostAdapter(mLostlist));
        } else {
            ToastUntil.showShortToast(getActivity(), "数据为空！");
        }
        //使用notifyDataSetChanged方法更新列表数据时，一定要保证数据为同个对象
        //lostAdapter.notifyDataSetChanged();
        Log.d("notifyDataSetChanged", mLostlist.size() + "");
        swip_lostfound.setRefreshing(false);
    }

    private void getList() {
        BmobQuery<Lost> query = new BmobQuery<Lost>();
        //按照时间降序
        query.order("-createdAt");
        query.findObjects(new FindListener<Lost>() {
            @Override
            public void done(List<Lost> list, BmobException e) {
                if (e == null) {
                    mLostlist = list;
                    //保证mLostlist为同一个对象
//                    for (Lost lost : list) {
//                        Lost currentLost = new Lost();
//                        currentLost.setTitle(lost.getTitle());
//                        currentLost.setPhone(lost.getPhone());
//                        currentLost.setFlag(lost.getFlag());
//                        currentLost.setDescribe(lost.getDescribe());
//                        mLostlist.add(currentLost);
//                        Log.d("lostfoundlist", lost.getTitle() + lost.getDescribe());
//                    }
                    handler.sendEmptyMessage(0);
                } else {
                    ToastUntil.showShortToast(getActivity(), "" + e);
                }
            }
        });
    }

    private void initEvent() {
        //悬浮按钮
        float_lostfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLostFound();
            }
        });
        //刷新监听
        swip_lostfound.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("onRefresh", "到了这里！");
                //先清空缓存
                getList();
            }
        });
    }

    private void gotoLostFound() {
        Intent intent = new Intent(getActivity(), LostFoundActivity.class);
        startActivity(intent);
    }

    private void initView(View view) {
        float_lostfound = (FloatingActionButton) view.findViewById(R.id.float_lostfound);
        swip_lostfound = (SwipeRefreshLayout) view.findViewById(R.id.swip_lostfound);
        swip_lostfound.setColorSchemeResources(R.color.colorPrimary);
        swip_lostfound.setRefreshing(true);
        rlv_lostfound = (RecyclerView) view.findViewById(R.id.rlv_lostfound);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rlv_lostfound.setLayoutManager(layoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getList();
    }
}
