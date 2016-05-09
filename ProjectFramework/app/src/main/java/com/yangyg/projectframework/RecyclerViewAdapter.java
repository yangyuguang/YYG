package com.yangyg.projectframework;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yangyuguang on 16/4/12.
 */
public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewHoler>{


    private Context mContext = null;
    private int layoutId = -1;
    private List<T> mData = null;


    public RecyclerViewAdapter(Context mContext,int layoutId,List<T> mData){
        this.mContext = mContext;
        this.layoutId = layoutId;
        this.mData = mData;
    }

    @Override
    public RecyclerViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHoler viewHoler = new RecyclerViewHoler(mContext, LayoutInflater.from(mContext).inflate(layoutId,null));
        return viewHoler;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHoler holder, int position) {
        //TODO 绑定数据到View上

        try {
            getView(holder, mData.get(position), position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(mData == null || mData.isEmpty()){
            return 0;
        }
        return mData.size();
    }

    public abstract void getView(RecyclerViewHoler holer, T t, int position);

}
