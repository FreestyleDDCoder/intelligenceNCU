package com.intelligencencu.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.intelligencencu.intelligencencu.R;

/**
 * Created by liangzhan on 17-3-23.
 * 日历Fragment
 */

public class CalenderFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calender, container, false);
        CalendarView mcv_day = (CalendarView) view.findViewById(R.id.cv_day);
        mcv_day.setDate(System.currentTimeMillis());
        return view;
    }
}
