package com.intelligencencu.Fragment.Classmate;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intelligencencu.activity.BeginPageActivity;
import com.intelligencencu.db.File;
import com.intelligencencu.entity.SchoolBus;
import com.intelligencencu.intelligencencu.R;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import dym.unique.com.springinglayoutlibrary.handler.SpringTouchRippleHandler;
import dym.unique.com.springinglayoutlibrary.view.SpringingListView;
import dym.unique.com.springinglayoutlibrary.view.SpringingTextView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by liangzhan on 17-3-24.
 * 校车Fragment
 * Fragment的生命周期依赖Activity存在
 */

public class SchoolCarFragment extends Fragment {

    private String path = null;
    private List<SchoolBus> schoolBusList;
    private ListView mlv_schoolbus;
    private SwipeRefreshLayout swip_refresh;
    private MyAdapter myAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schoolcar, container, false);
        initView(view);
        //加载网路数据
        gotoFindJson();
        //下拉刷新控件

        return view;
    }

    private void initView(View view) {
        mlv_schoolbus = (ListView) view.findViewById(R.id.lv_schoolbus);
        swip_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swip_refresh);
        swip_refresh.setColorSchemeResources(R.color.colorPrimary);
        swip_refresh.setRefreshing(true);
        swip_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gotoFindJson();
            }
        });
    }

    private void initData() {
        if (myAdapter != null) {
            myAdapter.notifyDataSetChanged();
        } else {
            myAdapter = new MyAdapter();
            mlv_schoolbus.setAdapter(myAdapter);
        }
        swip_refresh.setRefreshing(false);
    }

    //加载Json方法
    private void gotoFindJson() {
        BmobQuery<File> query = new BmobQuery<File>();
        query.getObject("pjHc444D", new QueryListener<File>() {
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
        //http://bmob-cdn-5986.b0.upaiyun.com/2017/03/26/4ef56a164067bccf801e94386b58c505.json
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    String url = "http://bmob-cdn-5986.b0.upaiyun.com/2017/03/26/4ef56a164067bccf801e94386b58c505.json";
//                    OkHttpClient client = new OkHttpClient();
//                    Request request = new Request.Builder().url(url).build();
//                    Response response = client.newCall(request).execute();
//                    path = response.body().string();
//                    Message message = new Message();
//                    message.what = 0;
//                    handler.sendMessage(message);
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//            }
//        }.start();
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Log.d("Name", "查询成功！" + path);
                    parseJSONWithGSON(path);
                    //显示数据
                    initData();
                    break;
            }
        }
    };

    private void parseJSONWithGSON(String s) {
        Gson gson = new Gson();
        schoolBusList = gson.fromJson(s, new TypeToken<List<SchoolBus>>() {
        }.getType());
        for (SchoolBus schoolBus : schoolBusList) {
            Log.d("SchoolBus", schoolBus.getName() + schoolBus.getPathway() + schoolBus.getTime());
        }
    }

    //适配器
    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return schoolBusList.size();
        }

        @Override
        public Object getItem(int position) {
            return schoolBusList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder;
            if (view != null && view instanceof LinearLayout) {
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getActivity(), R.layout.itme_schoolbus, null);
                holder = new ViewHolder();
                holder.Name = (SpringingTextView) view.findViewById(R.id.stv_schoolbus);
                view.setTag(holder);
            }
            final SchoolBus bus = schoolBusList.get(position);
            holder.Name.setText(bus.getName());
            holder.Name.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(getActivity(), holder.Name));
            //用于点击展示详细线路信息
            holder.Name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(bus.getName());
                    builder.setMessage("候车地点：" + bus.getNote() + "\n" + "\n" + "行车路线：" + bus.getPathway() + "\n" + "\n" + "发车时间：" + bus.getTime());
                    builder.setNegativeButton("返回", null);
                    builder.show();
                    Log.d("bus", bus.getName() + bus.getTime() + bus.getPathway() + bus.getName());
                }
            });
            return view;
        }
    }

    static class ViewHolder {
        SpringingTextView Name;
    }
}
