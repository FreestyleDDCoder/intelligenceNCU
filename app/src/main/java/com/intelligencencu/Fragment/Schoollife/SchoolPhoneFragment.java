package com.intelligencencu.Fragment.Schoollife;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intelligencencu.db.File;
import com.intelligencencu.entity.Phone;
import com.intelligencencu.intelligencencu.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import dym.unique.com.springinglayoutlibrary.handler.SpringTouchRippleHandler;
import dym.unique.com.springinglayoutlibrary.view.SpringingTextView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liangzhan on 17-3-28.
 * 学校黄页（电话信息）界面
 */

public class SchoolPhoneFragment extends Fragment {

    private SwipeRefreshLayout swip_phone;
    private String path = null;
    private ListView lv_phone;
    private List<Phone> phones = null;
    private PhoneAdapter phoneAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schoolphone, container, false);
        initView(view);
        gotoFindJson();
        initEvent();
        return view;
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Log.d("Name", "查询成功！" + path);
                parseJSONWithGSON(path);
                initDate();
            }
        }
    };

    private void initDate() {
        if (phoneAdapter != null) {
            phoneAdapter.notifyDataSetChanged();
        } else {
            phoneAdapter = new PhoneAdapter();
            lv_phone.setAdapter(phoneAdapter);
        }
        swip_phone.setRefreshing(false);
    }

    //解析Json
    private void parseJSONWithGSON(String path) {
        Gson gson = new Gson();
        phones = gson.fromJson(path, new TypeToken<List<Phone>>() {
        }.getType());
        for (Phone phone : phones) {
            Log.d("phone", phone.getTitle() + "\n" + phone.getContent());
        }
    }

    private void initEvent() {
        swip_phone.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gotoFindJson();
            }
        });
    }

    //加载json文件
    private void gotoFindJson() {
        BmobQuery<File> query = new BmobQuery<>();
        query.getObject("qV2j111I", new QueryListener<File>() {
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

    private void initView(View view) {
        lv_phone = (ListView) view.findViewById(R.id.lv_phone);
        swip_phone = (SwipeRefreshLayout) view.findViewById(R.id.swip_phone);
        swip_phone.setColorSchemeResources(R.color.colorPrimary);
        swip_phone.setRefreshing(true);
    }

    private class PhoneAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return phones.size();
        }

        @Override
        public Object getItem(int position) {
            return phones.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder;
            if (view != null && view instanceof LinearLayout) {
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getActivity(), R.layout.item_phone, null);
                holder = new ViewHolder();
                holder.cv_phone = (CardView) view.findViewById(R.id.cv_phone);
                holder.stv_phone = (SpringingTextView) view.findViewById(R.id.stv_phone);
                view.setTag(holder);
            }
            final Phone phone = phones.get(position);
            holder.stv_phone.setText(phone.getTitle());
            holder.stv_phone.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(getActivity(), holder.stv_phone));
            holder.stv_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(phone);
                }
            });
            return view;
        }

        private void showDialog(Phone phone) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(phone.getTitle());
            builder.setMessage(phone.getContent());
            builder.setNegativeButton("返回", null);
            builder.show();
        }
    }

    static class ViewHolder {
        CardView cv_phone;
        SpringingTextView stv_phone;
    }
}
