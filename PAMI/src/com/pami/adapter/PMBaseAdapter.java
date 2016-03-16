package com.pami.adapter;

import java.util.List;

import com.pami.PMApplication;
import com.pami.http.ExceptionUtils;
import com.pami.utils.MLog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class PMBaseAdapter<T> extends BaseAdapter {

	protected List<T> mData = null;
	protected Context mContext = null;
	private int layoutId;
	
	/**
	 * 
	 * @param context  上下文对象
	 * @param mData  数据源
	 * @param layoutId  View的Id
	 */
	public PMBaseAdapter(Context context,List<T> mData,int layoutId){
		this.mContext = context;
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
		try {
			ViewHolder holder = ViewHolder.get(mContext, convertView, parent, layoutId, position);
			getViews(holder,getItem(position),position);
			return holder.getConvertView();
		} catch (Exception e) {
			uploadException(e);
		}
		return convertView;
	}
	
	public abstract void getViews(ViewHolder holder,T t,int position)throws Exception;

	/**
	 * 重新绑定数据集  并刷新ListView
	 * @param mData
	 */
	public void bindData(List<T> mData){
		this.mData = mData; 
		notifyDataSetChanged();
	}
	
	/**
	 * 上传log 日志
	 * 注意调用此方法 app 必须重写PMApplication 并在 onCreate方法中 调用 setExceptionUrl(url) 将上传log信息的URL注入系统。否则将调用无效 。
	 * 最后别忘记在清单文件中注册 重写的 PMApplication
	 * @param e
	 */
	protected void uploadException(Exception e){};

}
