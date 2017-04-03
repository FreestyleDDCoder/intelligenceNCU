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
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.intelligencencu.activity.ShowNewsActivity;
import com.intelligencencu.activity.ShowViewActivity;
import com.intelligencencu.entity.NcuNews;
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
 * Created by liangzhan on 17-3-30.
 * 昌大要闻适配器
 */

public class NcuNewsAdapter extends RecyclerView.Adapter<NcuNewsAdapter.ViewHolder> {
    private List<NcuNews> mncunews;
    private Context mcontext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        SpringingImageView imageView;
        SpringingTextView title;
        SpringingLinearLayout sll_ncunews;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            imageView = (SpringingImageView) view.findViewById(R.id.siv_ncunews);
            title = (SpringingTextView) view.findViewById(R.id.stv_ncunews);
            sll_ncunews = (SpringingLinearLayout) view.findViewById(R.id.sll_ncunews);
        }
    }

    public NcuNewsAdapter(List<NcuNews> ncunewsList) {
        mncunews = ncunewsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mcontext == null) {
            mcontext = parent.getContext();
        }

        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_ncunews, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NcuNews ncuNews = mncunews.get(position);
        holder.title.setText(ncuNews.getTitle() + "\n" + ncuNews.getTime());
        Glide.with(mcontext).load(ncuNews.getImg()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoShowView(ncuNews);
            }
        });

        initSpringLayout(holder);
        showViews(holder);
    }

    private void showViews(ViewHolder holder) {
        new SpringingAlphaShowHandler(mcontext, holder.sll_ncunews).showChildrenSequence(500, 100);
        new SpringingTranslationShowHandler(mcontext, holder.sll_ncunews).showChildrenSequence(500, 100);
        new SpringingNotificationRotateHandler(mcontext, holder.imageView).start(1);
    }

    private void initSpringLayout(ViewHolder holder) {
        holder.title.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(mcontext, holder.title));
        holder.imageView.getSpringingHandlerController().addSpringingHandler(new SpringingTouchScaleHandler(mcontext, holder.imageView));
        holder.sll_ncunews.getSpringingHandlerController().addSpringingHandler(new SpringingTouchDragHandler(mcontext, holder.sll_ncunews).setBackInterpolator(new OvershootInterpolator()).setBackDuration(SpringingTouchDragHandler.DURATION_LONG).setDirection(SpringingTouchDragHandler.DIRECTOR_BOTTOM | SpringingTouchDragHandler.DIRECTOR_TOP).setMinDistance(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, mcontext.getResources().getDisplayMetrics())));
    }

    private void gotoShowView(NcuNews ncuNews) {
        Intent intent = new Intent(mcontext, ShowNewsActivity.class);
        intent.putExtra("title", ncuNews.getTitle());
        intent.putExtra("content", ncuNews.getContent());
        intent.putExtra("img", ncuNews.getImg());
        intent.putExtra("time", ncuNews.getTime());
        Log.d("NcuNews", ncuNews.getTitle());
        mcontext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mncunews.size();
    }

}
