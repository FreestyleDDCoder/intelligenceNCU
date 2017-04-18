package com.intelligencencu.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.intelligencencu.Fragment.News.NcuNewsFragment;
import com.intelligencencu.intelligencencu.R;

import java.util.ArrayList;

/**
 * Created by liangzhan on 17-3-24.
 * 主页Fragment
 */

public class BeginPageFragment extends Fragment {
    //每个Tab都有一个按钮
    private LinearLayout mTabBbs;
    private LinearLayout mTabkebiao;
    private LinearLayout mTabTiaoZhaoShiChang;
    private TabLayout tl_bbs;
    private ViewPager vp_beginPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beginpage, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        vp_beginPage = (ViewPager) view.findViewById(R.id.vp_beginPage);
        //卡片视图集合
        final ArrayList<Fragment> mFragments = new ArrayList<>();
        //卡片名称集合
        final ArrayList<String> tabTitle = new ArrayList<>();
        tabTitle.add("心情地");
        tabTitle.add("小课表");
        tabTitle.add("闲置集");
        //用于切换的界面
        BbsFragment bbsFragment = new BbsFragment();
        LessonsFragment mLessonsFragment = new LessonsFragment();
        SellFragment mSellFragment = new SellFragment();
        mFragments.add(bbsFragment);
        mFragments.add(mLessonsFragment);
        mFragments.add(mSellFragment);
        //设置适配器
        //注意，这里使用getChildFragmentManager
        vp_beginPage.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitle.get(position);
            }
        });

        tl_bbs = (TabLayout) view.findViewById(R.id.tl_bbs);
        //给TabLayout添加Tab
        tl_bbs.addTab(tl_bbs.newTab().setText(tabTitle.get(0)));
        tl_bbs.addTab(tl_bbs.newTab().setText(tabTitle.get(1)));
        tl_bbs.addTab(tl_bbs.newTab().setText(tabTitle.get(2)));
        //给TabLayout设置关联ViewPager，如果设置了ViewPager，那么ViewPagerAdapter中的getPageTitle()方法返回的就是Tab上的标题
        tl_bbs.setupWithViewPager(vp_beginPage);
    }
}
