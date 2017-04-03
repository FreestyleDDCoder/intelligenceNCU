package com.intelligencencu.Fragment.Schoollife;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.intelligencencu.intelligencencu.R;

/**
 * Created by liangzhan on 17-3-28.
 * 查询服务Fragment
 */

public class QueryServiceFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_queryservice, container, false);
        return view;
    }
}
