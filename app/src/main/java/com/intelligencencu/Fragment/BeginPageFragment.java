package com.intelligencencu.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.intelligencencu.intelligencencu.R;

/**
 * Created by liangzhan on 17-3-24.
 * 主页Fragment
 */

public class BeginPageFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beginpage, container, false);
        WebView mwv_beginPage = (WebView) view.findViewById(R.id.wv_beginPage);
        mwv_beginPage.getSettings().setJavaScriptEnabled(true);
        mwv_beginPage.setWebViewClient(new WebViewClient());
        mwv_beginPage.loadUrl("http://www.helloncu.cn");
        return view;
    }
}
