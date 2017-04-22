package com.intelligencencu.Fragment.begin;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intelligencencu.adapter.NcuNewsAdapter;
import com.intelligencencu.adapter.SchoolSceneryAdapter;
import com.intelligencencu.db.File;
import com.intelligencencu.entity.NcuNews;
import com.intelligencencu.entity.Scenery;
import com.intelligencencu.intelligencencu.R;
import com.intelligencencu.utils.ToastUntil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liangzhan on 17-4-6.
 * 校园风光Fragment
 */

public class SchoolSceneryFragment extends Fragment {
    private SwipeRefreshLayout swip_ncunews;
    private String path = null;
    private RecyclerView rlv_ncunews;
    private SchoolSceneryAdapter adapter = null;
    private List<Scenery> ncuNewsList = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenery, container, false);
        initView(view);
        initEvent();
        gotofindJson();
        return view;
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                parseJSONWithGSON(path);
                //加载条目
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                } else {
                    initAdapter();
                }
                swip_ncunews.setRefreshing(false);
            }
        }
    };

    private void parseJSONWithGSON(String path) {
        Gson gson = new Gson();
        ncuNewsList = gson.fromJson(path, new TypeToken<List<Scenery>>() {
        }.getType());
        for (Scenery ncuNews : ncuNewsList) {
            Log.d("schoolOverView", ncuNews.getContent() + ncuNews.getTitle());
        }
    }

    private void initAdapter() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        rlv_ncunews.setLayoutManager(layoutManager);
        if (ncuNewsList != null) {
            adapter = new SchoolSceneryAdapter(ncuNewsList);
            rlv_ncunews.setAdapter(adapter);
        } else {
            ToastUntil.showShortToast(getActivity(), "数据异常");
        }
    }

    private void initEvent() {
        swip_ncunews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gotofindJson();
            }
        });
    }

    private void initView(View view) {
        swip_ncunews = (SwipeRefreshLayout) view.findViewById(R.id.swip_scenery);
        swip_ncunews.setColorSchemeResources(R.color.colorPrimary);
        swip_ncunews.setRefreshing(true);
        rlv_ncunews = (RecyclerView) view.findViewById(R.id.rlv_scenery);
    }

    private void gotofindJson() {
        BmobQuery<File> query = new BmobQuery<>();
        query.getObject("B1I6000A", new QueryListener<File>() {
            @Override
            public void done(File file, BmobException e) {
                if (e == null) {
                    BmobFile bmobFile = file.getJsonFile();
                    Log.d("Name", "查询成功！" + bmobFile.getFilename());
                    if (bmobFile != null) {
                        final String url = bmobFile.getFileUrl();
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    OkHttpClient client = new OkHttpClient();
                                    Request request = new Request.Builder().url(url).build();
                                    Response response = client.newCall(request).execute();
                                    path = response.body().string();
                                    Message message = new Message();
                                    message.what = 0;
                                    handler.sendMessage(message);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }.start();
                    } else {
                        Log.d("Name", "bmobFikle=null！");
                    }
                }
            }
        });
    }
}
