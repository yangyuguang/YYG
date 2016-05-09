package com.yangyg.projectframework;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView;

/**
 * Created by yangyuguang on 16/4/12.
 */
public class MyRecyclerView extends RecyclerView {

    public OnRefreshAndLoadListener getOnRefreshAndLoadListener() {
        return onRefreshAndLoadListener;
    }

    public void setOnRefreshAndLoadListener(OnRefreshAndLoadListener onRefreshAndLoadListener) {
        this.onRefreshAndLoadListener = onRefreshAndLoadListener;
    }

    private OnRefreshAndLoadListener onRefreshAndLoadListener = null;
    private LinearLayoutManager lManager = null;
    private int lastVisibleItem = 0;

    public MyRecyclerView(Context context) {
        this(context, null);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
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
