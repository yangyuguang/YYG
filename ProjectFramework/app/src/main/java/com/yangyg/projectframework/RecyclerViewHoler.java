package com.yangyg.projectframework;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yangyuguang on 16/4/12.
 */
public class RecyclerViewHoler extends RecyclerView.ViewHolder{

    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext = null;

    public RecyclerViewHoler(Context mContext,View itemView) {
        super(itemView);
        mViews = new SparseArray<View>();
        this.mConvertView = itemView;
        this.mContext = mContext;
    }

    public <T extends View> T getView(int id){
        View view = null;
        view = mViews.get(id);
        if(view == null){
            view = mConvertView.findViewById(id);
            mViews.put(id,view);
        }
        return (T) view;
    }

    /**
     * 给TextView 设置文本
     *
     * @param resId TextView的ID
     * @param text 文本信息
     * @return
     */
    public RecyclerViewHoler setText(int resId, String text) throws Exception {
        TextView textView = getView(resId);
        textView.setText(text);
        return this;
    }
}
