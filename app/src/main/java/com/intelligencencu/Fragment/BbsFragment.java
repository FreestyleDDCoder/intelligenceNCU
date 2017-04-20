package com.intelligencencu.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.intelligencencu.intelligencencu.R;

/**
 * Created by liangzhan on 17-4-14.
 * 论坛Fragment
 */

public class BbsFragment extends Fragment {

    private SwipeRefreshLayout swip_bbs;
    private FloatingActionButton float_bbs;
    private RecyclerView rlv_bbs;

    //设置两个常亮，用于标记是下拉刷新还是上拉刷新
    private static final int STATE_REFRESH = 0;
    private static final int STATE_MORE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bbs, container, false);
        initView(view);
        initEvent();
        getDate(0,STATE_REFRESH);
        return view;
    }
    //获取服务器数据的方法

    /**
     * @param page 分页查找的页码
     * @param refreshType 刷新的类型
     */
    private void getDate(int page, int refreshType) {

    }

    private void initEvent() {
        float_bbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        swip_bbs.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gotoPublish();
            }
        });
    }

    //前往发表帖子的活动界面
    private void gotoPublish() {

    }

    private void initView(View view) {
        rlv_bbs = (RecyclerView) view.findViewById(R.id.rlv_bbs);
        float_bbs = (FloatingActionButton) view.findViewById(R.id.float_bbs);
        swip_bbs = (SwipeRefreshLayout) view.findViewById(R.id.swip_bbs);
        swip_bbs.setColorSchemeResources(R.color.colorPrimary);
        swip_bbs.setRefreshing(true);
    }
}
