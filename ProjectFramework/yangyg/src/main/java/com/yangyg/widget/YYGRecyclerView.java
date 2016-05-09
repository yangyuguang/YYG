package com.yangyg.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 监听加载更多的  以及  自动设置  LinearLayoutManager 的RecyclerView
 * Created by yangyuguang on 16/4/12.
 */
public class YYGRecyclerView extends RecyclerView{

    private OnRefreshAndLoadListener onRefreshAndLoadListener = null;
    private LinearLayoutManager lManager = null;
    private int lastVisibleItem = 0;

    public YYGRecyclerView(Context context) {
        this(context, null);
    }

    public YYGRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YYGRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        lManager = new LinearLayoutManager(context);
        setLayoutManager(lManager);

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == getAdapter().getItemCount() && onRefreshAndLoadListener != null) {
                    onRefreshAndLoadListener.onLoad();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = lManager.findLastVisibleItemPosition();
            }
        });
    }

    public OnRefreshAndLoadListener getOnRefreshAndLoadListener() {
        return onRefreshAndLoadListener;
    }

    public void setOnRefreshAndLoadListener(OnRefreshAndLoadListener onRefreshAndLoadListener) {
        this.onRefreshAndLoadListener = onRefreshAndLoadListener;
    }

    /**
     * 加载更多监听
     */
    public interface OnRefreshAndLoadListener{
        /**
         * 加载更多
         */
        void onLoad();
    }
}
