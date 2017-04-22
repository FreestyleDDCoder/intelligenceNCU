package com.intelligencencu.Fragment;

import android.content.Intent;
import android.os.Bundle;
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

import com.intelligencencu.activity.BbsActivity;
import com.intelligencencu.adapter.BbsAdapter;
import com.intelligencencu.db.BBS;
import com.intelligencencu.intelligencencu.R;
import com.intelligencencu.utils.ToastUntil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

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

    private int count = 20;        // 每页的数据是20条
    private int curPage = 0;        // 当前页的编号，从0开始
    private ArrayList<BBS> mbbsList;
    private BbsAdapter bbsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bbs, container, false);
        initView(view);
        initEvent();
        getDate(0, STATE_REFRESH);
        return view;
    }
    //获取服务器数据的方法

    /**
     * @param page        分页查找的页码
     * @param refreshType 刷新的类型
     */
    private void getDate(int page, final int refreshType) {
        BmobQuery<BBS> query = new BmobQuery<>();
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
        query.findObjects(new FindListener<BBS>() {
            @Override
            public void done(List<BBS> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        Log.d("list.size()", "" + list.size());
                        if (refreshType == STATE_REFRESH) {
                            // 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
                            curPage = 0;
                            if (mbbsList != null)
                                mbbsList.clear();
                        }
                        for (BBS bbs : list) {
                            mbbsList.add(bbs);
                        }
                        //mLostlist = list;
                        // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                        curPage++;
                    } else if (refreshType == STATE_MORE) {
                        ToastUntil.showShortToast(getActivity(), "没有更多数据了");
                    } else if (refreshType == STATE_REFRESH) {
                        ToastUntil.showShortToast(getActivity(), "没有数据");
                    }
                    initDate();
                } else {
                    ToastUntil.showShortToast(getActivity(), "查询失败！" + e);
                }
            }
        });
    }

    private void initDate() {
        if (mbbsList != null) {
            if (bbsAdapter == null) {
                bbsAdapter = new BbsAdapter(mbbsList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rlv_bbs.setLayoutManager(layoutManager);
                rlv_bbs.setAdapter(bbsAdapter);
            } else {
                bbsAdapter.notifyDataSetChanged();
            }
        } else {
            ToastUntil.showShortToast(getActivity(), "当前没有数据！");
        }
        swip_bbs.setRefreshing(false);
    }

    private void initEvent() {
        float_bbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPublish();
            }
        });

        swip_bbs.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDate(0, STATE_REFRESH);
            }
        });

        rlv_bbs.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition == curPage * count - 1) {
                        getDate(curPage,STATE_MORE);
                        ToastUntil.showShortToast(getActivity(), "加载更多ing...");
                    }
                }
            }
        });
    }

    //前往发表帖子的活动界面
    private void gotoPublish() {
        Intent intent = new Intent(getActivity(), BbsActivity.class);
        startActivity(intent);
    }

    private void initView(View view) {
        rlv_bbs = (RecyclerView) view.findViewById(R.id.rlv_bbs);
        float_bbs = (FloatingActionButton) view.findViewById(R.id.float_bbs);
        swip_bbs = (SwipeRefreshLayout) view.findViewById(R.id.swip_bbs);
        swip_bbs.setColorSchemeResources(R.color.colorPrimary);
        swip_bbs.setRefreshing(true);
        mbbsList = new ArrayList<>();
    }
}
