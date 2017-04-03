package com.intelligencencu.Fragment.News;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intelligencencu.adapter.PeopleStoryAdapter;
import com.intelligencencu.db.File;
import com.intelligencencu.entity.PeopleStory;
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
 * Created by liangzhan on 17-3-30.
 * 人物故事
 */

public class PeopleStoryFragment extends Fragment {

    private RecyclerView rlv_peoplestory;
    private SwipeRefreshLayout swip_peoplestory;
    private String path = null;
    private List<PeopleStory> peopleStoryList = null;
    private PeopleStoryAdapter adapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_peoplestroy, container, false);
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
                initAdapter();
                swip_peoplestory.setRefreshing(false);
            } else {
                ToastUntil.showShortToast(getActivity(), "网络有误！");
            }
        }
    };

    private void initAdapter() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        rlv_peoplestory.setLayoutManager(layoutManager);
        if (peopleStoryList != null) {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            } else {
                adapter = new PeopleStoryAdapter(peopleStoryList);
                rlv_peoplestory.setAdapter(adapter);
            }
        }
    }

    private void gotofindJson() {
        BmobQuery<File> query = new BmobQuery<>();
        query.getObject("7Ba0555y", new QueryListener<File>() {
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

    private void parseJSONWithGSON(String path) {
        Gson gson = new Gson();
        peopleStoryList = gson.fromJson(path, new TypeToken<List<PeopleStory>>() {
        }.getType());
        for (PeopleStory ncuNews : peopleStoryList) {
//            Log.d("schoolOverView", ncuNews.getContent() + ncuNews.getTitle());
        }
    }

    private void initEvent() {
        swip_peoplestory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gotofindJson();
            }
        });
    }

    private void initView(View view) {
        swip_peoplestory = (SwipeRefreshLayout) view.findViewById(R.id.swip_peoplestory);
        swip_peoplestory.setColorSchemeResources(R.color.colorPrimary);
        swip_peoplestory.setRefreshing(true);
        rlv_peoplestory = (RecyclerView) view.findViewById(R.id.rlv_peoplestory);
    }
}
