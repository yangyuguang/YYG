package com.yangyg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yangyg.utils.ImageLoaderUtils;

/**
 * 注意需要引入 com.android.support:design:23.3.0 包
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

    /**
     * 给TextView 设置文本
     *
     * @param resId TextView的ID
     * @param text 文本信息
     * @return
     * @throws Exception
     */
    public RecyclerViewHoler setText(int resId, CharSequence text) throws Exception {
        TextView textView = getView(resId);
        textView.setText(text);
        return this;
    }

    /**
     * 给TextView 设置文本
     * @param resId TextView的ID
     * @param span 文本信息
     * @return
     * @throws Exception
     */
    public RecyclerViewHoler setText(int resId, Spannable span) throws Exception {
        TextView textView = getView(resId);
        textView.setText(span);
        return this;
    }

    /**
     * 给ImageView 设置图片 目前只支持本地图片
     *
     * @param resId ImageView ID
     * @param imageId 图片ID
     * @return
     */
    public RecyclerViewHoler setImage(int resId, int imageId) throws Exception {
        ImageView imageView = getView(resId);
        imageView.setImageResource(imageId);
        return this;
    }

    /**
     * 给ImageView 设置图片
     *
     * @param resId ImageView的ID
     * @param url 图片地址
     * @return
     */
    public RecyclerViewHoler setImage(int resId, String url) throws Exception {
        ImageView imageView = getView(resId);
        imageView.setTag(url);
        ImageLoaderUtils.getinstance(mContext).getImage(imageView, url);
        return this;
    }

    /**
     * 设置View 为GONE
     * @param resId view的ID
     * @return
     * @throws Exception
     */
    public RecyclerViewHoler setGone(int resId) throws Exception {
        this.getView(resId).setVisibility(View.GONE);
        return this;
    }

    /**
     * 设置View 为INVISIBLE
     * @param resId view的ID
     * @return
     * @throws Exception
     */
    public RecyclerViewHoler setInvisible(int resId) throws Exception {
        this.getView(resId).setVisibility(View.INVISIBLE);
        return this;
    }

    /**
     * 设置View 为VISIBLE
     * @param resId view的ID
     * @return
     * @throws Exception
     */
    public RecyclerViewHoler setVisible(int resId) throws Exception {
        this.getView(resId).setVisibility(View.VISIBLE);
        return this;
    }
}