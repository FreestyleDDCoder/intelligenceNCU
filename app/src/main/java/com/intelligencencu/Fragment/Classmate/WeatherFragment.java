package com.intelligencencu.Fragment.Classmate;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.intelligencencu.intelligencencu.R;

/**
 * Created by liangzhan on 17-3-24.
 * 天气Fragment
 */

public class WeatherFragment extends Fragment {

    private ProgressBar pb_weather;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        final long startTime = System.currentTimeMillis();
        pb_weather = (ProgressBar) view.findViewById(R.id.pb_weather);
        WebView wv_weather = (WebView) view.findViewById(R.id.wv_weather);
        wv_weather.getSettings().setJavaScriptEnabled(true);
        wv_weather.setWebViewClient(new WebViewClient());
        wv_weather.loadUrl("https://www.yahoo.com/news/weather/");
        new Thread() {
            @Override
            public void run() {
                super.run();
                Message message = new Message();
                long endTime = System.currentTimeMillis();
                if ((endTime - startTime) > 3000) {
                    handler.sendEmptyMessage(0);
                } else {
                    SystemClock.sleep(3000 - (endTime - startTime));
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();

        return view;
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pb_weather.setVisibility(View.GONE);
        }
    };
}
