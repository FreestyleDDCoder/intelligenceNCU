package com.intelligencencu.Fragment.Schoollife;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intelligencencu.activity.BeginPageActivity;
import com.intelligencencu.intelligencencu.R;

/**
 * Created by liangzhan on 17-3-22.
 * 学校生活界面Fragment
 */

public class SchoolYellowFragment extends Fragment {

    private LinearLayout mlay1;
    private LinearLayout mlay2;
    private LinearLayout mlay3;
    private LinearLayout mlay4;
    private TextView mfratext1;
    private TextView mfratext2;
    private TextView mfratext3;
    private TextView mfratext4;
    //用于判断当前是第几个Fragment界面
    private int position = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schoolyellow, container, false);

        initView(view);
        initEvent();
        initGesture();

        gotoSchoolPhone();

        return view;
    }

    //手势滑动监听接口实现
    private void initGesture() {
        final GestureDetector detector = new GestureDetector(getActivity(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                try {
                    if (e1.getX() - e2.getX() < -89 && Math.abs(e1.getY() - e2.getY()) < 120) {
                        flingLeft();
                        return true;
                    } else if (e1.getX() - e2.getX() > 89 && Math.abs(e1.getY() - e2.getY()) < 120) {
                        flingRight();
                        return true;
                    }
                } catch (Exception e) {
                    Log.d("onFling", "" + e);
                }
                return false;
            }
        });
        BeginPageActivity.MyOnTouchListener myOnTouchListener = new BeginPageActivity.MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent ev) {
                boolean result = detector.onTouchEvent(ev);
                return result;
            }
        };

        ((BeginPageActivity) getActivity()).registerMyOnTouchListener(myOnTouchListener);
    }

    private void initEvent() {
        mfratext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSchoolPhone();
            }
        });
        mfratext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoFreeRoom();
            }
        });
        mfratext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoQueryService();
            }
        });
        mfratext4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLostFound();
            }
        });
    }

    //失物招领处理方法
    private void gotoLostFound() {
        mlay1.setBackgroundColor(getResources().getColor(R.color.white));
        mfratext1.setTextColor(getResources().getColor(R.color.gray));
        mlay2.setBackgroundColor(getResources().getColor(R.color.white));
        mfratext2.setTextColor(getResources().getColor(R.color.gray));
        mlay3.setBackgroundColor(getResources().getColor(R.color.white));
        mfratext3.setTextColor(getResources().getColor(R.color.gray));
        mlay4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mfratext4.setTextColor(getResources().getColor(R.color.colorPrimary));
        replaceFragment(new LostFoundFragment());
        position = 4;
    }

    //查询服务处理方法
    private void gotoQueryService() {
        mlay1.setBackgroundColor(getResources().getColor(R.color.white));
        mfratext1.setTextColor(getResources().getColor(R.color.gray));
        mlay2.setBackgroundColor(getResources().getColor(R.color.white));
        mfratext2.setTextColor(getResources().getColor(R.color.gray));
        mlay3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mfratext3.setTextColor(getResources().getColor(R.color.colorPrimary));
        mlay4.setBackgroundColor(getResources().getColor(R.color.white));
        mfratext4.setTextColor(getResources().getColor(R.color.gray));
        replaceFragment(new QueryServiceFragment());
        position = 3;
    }

    //空闲教室处理方法
    private void gotoFreeRoom() {
        mlay1.setBackgroundColor(getResources().getColor(R.color.white));
        mfratext1.setTextColor(getResources().getColor(R.color.gray));
        mlay2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mfratext2.setTextColor(getResources().getColor(R.color.colorPrimary));
        mlay3.setBackgroundColor(getResources().getColor(R.color.white));
        mfratext3.setTextColor(getResources().getColor(R.color.gray));
        mlay4.setBackgroundColor(getResources().getColor(R.color.white));
        mfratext4.setTextColor(getResources().getColor(R.color.gray));
        replaceFragment(new FreeClassRoomFragment());
        position = 2;
    }

    //校园电话处理方法
    private void gotoSchoolPhone() {
        mlay1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mfratext1.setTextColor(getResources().getColor(R.color.colorPrimary));
        mlay2.setBackgroundColor(getResources().getColor(R.color.white));
        mfratext2.setTextColor(getResources().getColor(R.color.gray));
        mlay3.setBackgroundColor(getResources().getColor(R.color.white));
        mfratext3.setTextColor(getResources().getColor(R.color.gray));
        mlay4.setBackgroundColor(getResources().getColor(R.color.white));
        mfratext4.setTextColor(getResources().getColor(R.color.gray));
        replaceFragment(new SchoolPhoneFragment());
        position = 1;
    }

    private void initView(View view) {
        mlay1 = (LinearLayout) view.findViewById(R.id.lay1);
        mlay2 = (LinearLayout) view.findViewById(R.id.lay2);
        mlay3 = (LinearLayout) view.findViewById(R.id.lay3);
        mlay4 = (LinearLayout) view.findViewById(R.id.lay4);

        mfratext1 = (TextView) view.findViewById(R.id.fratext1);
        mfratext2 = (TextView) view.findViewById(R.id.fratext2);
        mfratext3 = (TextView) view.findViewById(R.id.fratext3);
        mfratext4 = (TextView) view.findViewById(R.id.fratext4);
        mlay1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mfratext1.setTextColor(getResources().getColor(R.color.colorPrimary));
        replaceFragment(new SchoolPhoneFragment());
    }


    //替换布局的方法
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.schoollife, fragment);
        transaction.commit();
    }

    //自定义方法：处理向左滑动事件
    public void flingLeft() {
        switch (position) {
            case 1:
                break;
            case 2:
                gotoSchoolPhone();
                break;
            case 3:
                gotoFreeRoom();
                break;
            case 4:
                gotoQueryService();
                break;
        }
    }

    //自定义方法：处理向右滑动事件
    public void flingRight() {
        switch (position) {
            case 1:
                gotoFreeRoom();
                break;
            case 2:
                gotoQueryService();
                break;
            case 3:
                gotoLostFound();
                break;
            case 4:
                break;
        }
    }
}
