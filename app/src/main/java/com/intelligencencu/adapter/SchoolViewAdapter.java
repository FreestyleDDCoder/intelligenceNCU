package com.intelligencencu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.bumptech.glide.Glide;
import com.intelligencencu.activity.ShowViewActivity;
import com.intelligencencu.entity.SchoolOverView;
import com.intelligencencu.intelligencencu.R;

import java.util.List;

import dym.unique.com.springinglayoutlibrary.handler.SpringTouchRippleHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingAlphaShowHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingNotificationRotateHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchDragHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchScaleHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTranslationShowHandler;
import dym.unique.com.springinglayoutlibrary.view.SpringingImageView;
import dym.unique.com.springinglayoutlibrary.view.SpringingTextView;
import dym.unique.com.springinglayoutlibrary.viewgroup.SpringingLinearLayout;

/**
 * Created by liangzhan on 17-3-27.
 * 学校概况条目mvc
 */

public class SchoolViewAdapter extends RecyclerView.Adapter<SchoolViewAdapter.ViewHolder> {
    private List<SchoolOverView> mschooloverviews;
    private Context mcontext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        SpringingImageView imageView;
        SpringingTextView title;
        SpringingLinearLayout sll_schoolview;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            imageView = (SpringingImageView) view.findViewById(R.id.siv_schoolviewimage);
            title = (SpringingTextView) view.findViewById(R.id.stv_schoolviewtitle);
            sll_schoolview = (SpringingLinearLayout) view.findViewById(R.id.sll_schoolview);
        }
    }

    public SchoolViewAdapter(List<SchoolOverView> schooloverviews) {
        mschooloverviews = schooloverviews;
    }

    //布局填充
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mcontext == null) {
            mcontext = parent.getContext();
        }
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_schoolview, parent, false);
        return new ViewHolder(view);
    }

    //条目逻辑处理
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SchoolOverView view = mschooloverviews.get(position);
        holder.title.setText(view.getTitle());
        //Glide.with.load方法可以直接加载本地路径，可以是URI，也可以是资源id
        Glide.with(mcontext).load(view.getBackgroundUrl()).into(holder.imageView);
        initSpringLayout(holder);
        showViews(holder);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showViewMessage(view.getTitle(), view.getBackgroundUrl(), view.getContentUrl(), view.getImgTitleUrl());
            }
        });
    }

    private void showViewMessage(String title, String backgroundUrl, String contentUrl, String imgTitleUrl) {
        //处理条目点击事件
        Intent intent = new Intent(mcontext, ShowViewActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("backgroundUrl", backgroundUrl);
        intent.putExtra("contentUrl", contentUrl);
        intent.putExtra("imgTitleUrl", imgTitleUrl);
        mcontext.startActivity(intent);
    }

    private void showViews(ViewHolder holder) {
        new SpringingAlphaShowHandler(mcontext, holder.sll_schoolview).showChildrenSequence(500, 100);
        new SpringingTranslationShowHandler(mcontext, holder.sll_schoolview).showChildrenSequence(500, 100);
        new SpringingNotificationRotateHandler(mcontext, holder.imageView).start(1);
    }

    private void initSpringLayout(ViewHolder holder) {
        holder.title.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(mcontext, holder.title));
        holder.imageView.getSpringingHandlerController().addSpringingHandler(new SpringingTouchScaleHandler(mcontext, holder.imageView));
        holder.sll_schoolview.getSpringingHandlerController().addSpringingHandler(new SpringingTouchDragHandler(mcontext, holder.sll_schoolview).setBackInterpolator(new OvershootInterpolator()).setBackDuration(SpringingTouchDragHandler.DURATION_LONG).setDirection(SpringingTouchDragHandler.DIRECTOR_BOTTOM | SpringingTouchDragHandler.DIRECTOR_TOP).setMinDistance(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, mcontext.getResources().getDisplayMetrics())));
    }

    @Override
    public int getItemCount() {
        return mschooloverviews.size();
    }
}
