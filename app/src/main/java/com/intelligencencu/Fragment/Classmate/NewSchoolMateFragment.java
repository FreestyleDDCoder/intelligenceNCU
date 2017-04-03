package com.intelligencencu.Fragment.Classmate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.intelligencencu.Fragment.Classmate.CalenderFragment;
import com.intelligencencu.Fragment.Classmate.MoreFragment;
import com.intelligencencu.Fragment.Classmate.SchoolCarFragment;
import com.intelligencencu.Fragment.Classmate.WeatherFragment;
import com.intelligencencu.intelligencencu.R;

import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchPointHandler;
import dym.unique.com.springinglayoutlibrary.view.SpringingImageView;

/**
 * Created by liangzhan on 17-3-22.
 * 新生导航界面
 */

public class NewSchoolMateFragment extends Fragment {

    //    private LinearLayout mll_calendar;
    private SpringingImageView msiv_calendar;
    //    private LinearLayout mll_school_car;
    private SpringingImageView msiv_school_car;
    //    private LinearLayout mll_weather;
    private SpringingImageView msiv_weather;
    //    private LinearLayout mll_icn_4;
    private SpringingImageView msiv_icn_4;


    @Nullable
    @Override
    //加载控件
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newschoolmate, container, false);
//        mll_calendar = (LinearLayout) view.findViewById(R.id.ll_calendar);
        msiv_calendar = (SpringingImageView) view.findViewById(R.id.siv_calendar);
//        mll_school_car = (LinearLayout) view.findViewById(R.id.ll_school_car);
        msiv_school_car = (SpringingImageView) view.findViewById(R.id.siv_school_car);
//        mll_weather = (LinearLayout) view.findViewById(R.id.ll_weather);
        msiv_weather = (SpringingImageView) view.findViewById(R.id.siv_weather);
//        mll_icn_4 = (LinearLayout) view.findViewById(R.id.ll_icn_4);
        msiv_icn_4 = (SpringingImageView) view.findViewById(R.id.siv_icn_4);
        msiv_calendar.setIsCircleImage(true);
        msiv_school_car.setIsCircleImage(true);
        msiv_weather.setIsCircleImage(true);
        msiv_icn_4.setIsCircleImage(true);

        initSpringLayout();
        //默认显示日历
        msiv_calendar.setBackgroundResource(R.drawable.circle_background);
        //msiv_calendar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        msiv_school_car.setBackgroundColor(getResources().getColor(R.color.white));
        msiv_weather.setBackgroundColor(getResources().getColor(R.color.white));
        msiv_icn_4.setBackgroundColor(getResources().getColor(R.color.white));

        replaceFragment(new CalenderFragment());

        initEvent();

        return view;
    }

    private void initEvent() {
        msiv_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msiv_calendar.setBackgroundResource(R.drawable.circle_background);
               // msiv_calendar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                msiv_school_car.setBackgroundColor(getResources().getColor(R.color.white));
                msiv_weather.setBackgroundColor(getResources().getColor(R.color.white));
                msiv_icn_4.setBackgroundColor(getResources().getColor(R.color.white));

                replaceFragment(new CalenderFragment());
            }
        });
        msiv_school_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msiv_calendar.setBackgroundColor(getResources().getColor(R.color.white));
                msiv_school_car.setBackgroundResource(R.drawable.circle_background);
                //msiv_school_car.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                msiv_weather.setBackgroundColor(getResources().getColor(R.color.white));
                msiv_icn_4.setBackgroundColor(getResources().getColor(R.color.white));
                replaceFragment(new SchoolCarFragment());
            }
        });
        msiv_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msiv_calendar.setBackgroundColor(getResources().getColor(R.color.white));
                msiv_school_car.setBackgroundColor(getResources().getColor(R.color.white));
                msiv_weather.setBackgroundResource(R.drawable.circle_background);
                //msiv_weather.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                msiv_icn_4.setBackgroundColor(getResources().getColor(R.color.white));
                replaceFragment(new WeatherFragment());


            }
        });
        msiv_icn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msiv_calendar.setBackgroundColor(getResources().getColor(R.color.white));
                msiv_school_car.setBackgroundColor(getResources().getColor(R.color.white));
                msiv_weather.setBackgroundColor(getResources().getColor(R.color.white));
                msiv_icn_4.setBackgroundResource(R.drawable.circle_background);
                //msiv_icn_4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                replaceFragment(new MoreFragment());
            }
        });
    }

    private void initSpringLayout() {
        msiv_calendar.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(getContext(), msiv_calendar).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        msiv_school_car.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(getContext(), msiv_school_car).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        msiv_weather.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(getContext(), msiv_weather).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        msiv_icn_4.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(getContext(), msiv_icn_4).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
    }

    //替换fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_newSchoolmate, fragment);
        transaction.commit();
    }
}
