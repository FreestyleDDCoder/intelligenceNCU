package com.intelligencencu.Fragment.begin;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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
import com.intelligencencu.adapter.SchoolViewAdapter;
import com.intelligencencu.db.File;
import com.intelligencencu.entity.SchoolOverView;
import com.intelligencencu.intelligencencu.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liangzhan on 17-3-26.
 * 学校概况
 */

public class SchoolMessageFragment extends Fragment {

    // private ProgressBar pb_message;
    private RecyclerView rv_schoolview;
    private List<SchoolOverView> schoolOverViewList = null;
    private SchoolViewAdapter adapter;
    private String path = null;
    private GridLayoutManager layoutManager;
    private SwipeRefreshLayout swip_schoolviewrefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schoolmessage, container, false);

        initView(view);
        gotofindJson();
        //showWebView();
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
                swip_schoolviewrefresh.setRefreshing(false);
            }
        }
    };

    private void initAdapter() {
        rv_schoolview.setLayoutManager(layoutManager);
        if (schoolOverViewList != null) {
            adapter = new SchoolViewAdapter(schoolOverViewList);
            rv_schoolview.setAdapter(adapter);
        }
    }

    private void initView(View view) {
        //final long startTime = System.currentTimeMillis();
        rv_schoolview = (RecyclerView) view.findViewById(R.id.rv_schoolview);
        swip_schoolviewrefresh = (SwipeRefreshLayout) view.findViewById(R.id.swip_schoolviewrefresh);
        swip_schoolviewrefresh.setColorSchemeResources(R.color.colorPrimary);
        swip_schoolviewrefresh.setRefreshing(true);
        swip_schoolviewrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gotofindJson();
            }
        });
        layoutManager = new GridLayoutManager(getActivity(), 2);
        // pb_message = (ProgressBar) view.findViewById(R.id.pb_message);
    }

    private void gotofindJson() {
        BmobQuery<File> query = new BmobQuery<File>();
        query.getObject("DFhk999M", new QueryListener<File>() {
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

    //json解析
    private void parseJSONWithGSON(String s) {
        Gson gson = new Gson();
        schoolOverViewList = gson.fromJson(s, new TypeToken<List<SchoolOverView>>() {
        }.getType());
        for (SchoolOverView schoolOverView : schoolOverViewList) {
            Log.d("schoolOverView", schoolOverView.getId() + schoolOverView.getTitle());
        }
    }
}
