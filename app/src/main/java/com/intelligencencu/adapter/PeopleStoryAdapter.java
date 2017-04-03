package com.intelligencencu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.bumptech.glide.Glide;
import com.intelligencencu.activity.ShowNewsActivity;
import com.intelligencencu.entity.PeopleStory;
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
 * Created by liangzhan on 17-3-31.
 * 人物故事适配器
 */

public class PeopleStoryAdapter extends RecyclerView.Adapter<PeopleStoryAdapter.ViewHolder> {
    private Context mContext;
    private List<PeopleStory> mPeopleStoryList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        SpringingImageView imageView;
        SpringingTextView stv_peoplestory;
        SpringingLinearLayout sll_peoplestory;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            imageView = (SpringingImageView) view.findViewById(R.id.siv_peoplestory);
            stv_peoplestory = (SpringingTextView) view.findViewById(R.id.stv_peoplestory);
            sll_peoplestory = (SpringingLinearLayout) view.findViewById(R.id.sll_peoplestory);
        }
    }

    public PeopleStoryAdapter(List<PeopleStory> peopleStoryList) {
        mPeopleStoryList = peopleStoryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_peoplestory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PeopleStory peopleStory = mPeopleStoryList.get(position);
        holder.stv_peoplestory.setText(peopleStory.getTitle() + "\n" + peopleStory.getTime());
        Glide.with(mContext).load(peopleStory.getImg()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoShowView(peopleStory);
            }
        });
        initSpringLayout(holder);
        showViews(holder);
    }

    private void showViews(ViewHolder holder) {
        new SpringingAlphaShowHandler(mContext, holder.sll_peoplestory).showChildrenSequence(500, 100);
        new SpringingTranslationShowHandler(mContext, holder.sll_peoplestory).showChildrenSequence(500, 100);
        new SpringingNotificationRotateHandler(mContext, holder.imageView).start(1);
    }

    private void initSpringLayout(ViewHolder holder) {
        holder.stv_peoplestory.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(mContext, holder.stv_peoplestory));
        holder.imageView.getSpringingHandlerController().addSpringingHandler(new SpringingTouchScaleHandler(mContext, holder.imageView));
        holder.sll_peoplestory.getSpringingHandlerController().addSpringingHandler(new SpringingTouchDragHandler(mContext, holder.sll_peoplestory).setBackInterpolator(new OvershootInterpolator()).setBackDuration(SpringingTouchDragHandler.DURATION_LONG).setDirection(SpringingTouchDragHandler.DIRECTOR_BOTTOM | SpringingTouchDragHandler.DIRECTOR_TOP).setMinDistance(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, mContext.getResources().getDisplayMetrics())));
    }

    private void gotoShowView(PeopleStory peopleStory) {
        Intent intent = new Intent(mContext, ShowNewsActivity.class);
        intent.putExtra("title", peopleStory.getTitle());
        intent.putExtra("content", peopleStory.getContent());
        intent.putExtra("img", peopleStory.getImg());
        intent.putExtra("time", peopleStory.getTime());
        Log.d("NcuNews", peopleStory.getTitle());
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mPeopleStoryList.size();
    }
}
