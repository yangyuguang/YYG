package com.pami.adapter;

import java.util.List;

import com.pami.utils.MLog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class PMBaseAdapter<T> extends BaseAdapter {

	protected List<T> mData = null;
	protected Context context = null;
	private int layoutId;
	
	/**
	 * 
	 * @param context  上下文对象
	 * @param mData  数据源
	 * @param layoutId  View的Id
	 */
	public PMBaseAdapter(Context context,List<T> mData,int layoutId){
		this.context = context;
		this.mData = mData;
		this.layoutId = layoutId;
	}
	
	@Override
	public int getCount() {
		int count = 0;
		if(mData != null){
			count = mData.size();
		}
		return count;
	}
	
	@Override
	public T getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.get(context, convertView, parent, layoutId, position);
		getViews(holder,getItem(position),position);
		return holder.getConvertView();
	}
	
	public abstract void getViews(ViewHolder holder,T t,int position);

	public void bindData(List<T> mData){
		this.mData = mData; 
		notifyDataSetChanged();
	}

}
