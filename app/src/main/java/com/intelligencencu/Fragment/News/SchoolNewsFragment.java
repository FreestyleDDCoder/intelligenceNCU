package com.intelligencencu.Fragment.News;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intelligencencu.Fragment.News.NcuNewsFragment;
import com.intelligencencu.activity.BeginPageActivity;
import com.intelligencencu.intelligencencu.R;

/**
 * Created by liangzhan on 17-3-22.
 * 校园新闻Fragment
 */

public class SchoolNewsFragment extends Fragment {

    private TextView newstext1;
    private TextView newstext2;
    private TextView newstext3;
    private TextView newstext4;
    private LinearLayout news1;
    private LinearLayout news2;
    private LinearLayout news3;
    private LinearLayout news4;

    private int position = 1;
    private BeginPageActivity.MyOnTouchListener myOnTouchListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schoolnews, container, false);
        initView(view);
        initEvent();
        initTouch();
        gotoNcuNews();
        return view;
    }

    //滑动手势
    private void initTouch() {
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
                    if (e1.getX() - e2.getX() < -120 && Math.abs(e1.getY() - e2.getY()) < 120) {
                        flingLeft();
                        return true;
                    } else if (e1.getX() - e2.getX() > 120 && Math.abs(e1.getY() - e2.getY()) < 120) {
                        flingRight();
                        return true;
                    }
                } catch (Exception e) {
                    Log.d(getActivity().toString(), "" + e);
                }
                return false;
            }
        });

        myOnTouchListener = new BeginPageActivity.MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent ev) {
                boolean result = detector.onTouchEvent(ev);
                return result;
            }
        };

        ((BeginPageActivity) getActivity()).registerMyOnTouchListener(myOnTouchListener);
    }

    //往右滑手势
    private void flingRight() {
        switch (position) {
            case 1:
                gotoFastNews();
                break;
            case 2:
                gotoPeopleStory();
                break;
            case 3:
                gotoButiNcu();
                break;
            case 4:
                break;
        }
    }

    //往左滑手势
    private void flingLeft() {
        switch (position) {
            case 1:
                break;
            case 2:
                gotoNcuNews();
                break;
            case 3:
                gotoFastNews();
                break;
            case 4:
                gotoPeopleStory();
                break;
        }
    }

    private void initEvent() {
        newstext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNcuNews();
            }
        });
        newstext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoFastNews();
            }
        });
        newstext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPeopleStory();
            }
        });
        newstext4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoButiNcu();
            }
        });
    }

    //美丽昌大
    private void gotoButiNcu() {
        newstext1.setTextColor(getResources().getColor(R.color.gray));
        news1.setBackgroundColor(getResources().getColor(R.color.white));
        newstext2.setTextColor(getResources().getColor(R.color.gray));
        news2.setBackgroundColor(getResources().getColor(R.color.white));
        newstext3.setTextColor(getResources().getColor(R.color.gray));
        news3.setBackgroundColor(getResources().getColor(R.color.white));
        newstext4.setTextColor(getResources().getColor(R.color.colorPrimary));
        news4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        replaceFragment(new ButiNcuFragment());
        position = 4;
    }

    //人物故事
    private void gotoPeopleStory() {
        newstext1.setTextColor(getResources().getColor(R.color.gray));
        news1.setBackgroundColor(getResources().getColor(R.color.white));
        newstext2.setTextColor(getResources().getColor(R.color.gray));
        news2.setBackgroundColor(getResources().getColor(R.color.white));
        newstext3.setTextColor(getResources().getColor(R.color.colorPrimary));
        news3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        newstext4.setTextColor(getResources().getColor(R.color.gray));
        news4.setBackgroundColor(getResources().getColor(R.color.white));
        replaceFragment(new PeopleStoryFragment());
        position = 3;
    }

    //校园快讯
    private void gotoFastNews() {
        newstext1.setTextColor(getResources().getColor(R.color.gray));
        news1.setBackgroundColor(getResources().getColor(R.color.white));
        newstext2.setTextColor(getResources().getColor(R.color.colorPrimary));
        news2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        newstext3.setTextColor(getResources().getColor(R.color.gray));
        news3.setBackgroundColor(getResources().getColor(R.color.white));
        newstext4.setTextColor(getResources().getColor(R.color.gray));
        news4.setBackgroundColor(getResources().getColor(R.color.white));
        replaceFragment(new FastNewsFragment());
        position = 2;
    }

    //昌大要闻
    private void gotoNcuNews() {
        newstext1.setTextColor(getResources().getColor(R.color.colorPrimary));
        news1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        newstext2.setTextColor(getResources().getColor(R.color.gray));
        news2.setBackgroundColor(getResources().getColor(R.color.white));
        newstext3.setTextColor(getResources().getColor(R.color.gray));
        news3.setBackgroundColor(getResources().getColor(R.color.white));
        newstext4.setTextColor(getResources().getColor(R.color.gray));
        news4.setBackgroundColor(getResources().getColor(R.color.white));
        replaceFragment(new NcuNewsFragment());
        position = 1;
    }

    private void initView(View view) {
        newstext1 = (TextView) view.findViewById(R.id.Newstext1);
        newstext2 = (TextView) view.findViewById(R.id.Newstext2);
        newstext3 = (TextView) view.findViewById(R.id.Newstext3);
        newstext4 = (TextView) view.findViewById(R.id.Newstext4);

        news1 = (LinearLayout) view.findViewById(R.id.News1);
        news2 = (LinearLayout) view.findViewById(R.id.News2);
        news3 = (LinearLayout) view.findViewById(R.id.News3);
        news4 = (LinearLayout) view.findViewById(R.id.News4);

    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_schoolNews, fragment);
        transaction.commit();
    }

    //注意当有多个Fragment使用GestureDetector的时候，在每个使用界面销毁时一定得反注册掉
    @Override
    public void onDestroy() {
        super.onDestroy();
        //当这个fragmentation销毁时注销掉
        ((BeginPageActivity) getActivity()).unregisterMyOnTouchListener(myOnTouchListener);
    }
}
