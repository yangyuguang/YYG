package com.pami.adapter;

import android.content.Context;
import android.text.Spannable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {

	private SparseArray<View> mViews;
	private static int mPosition;
	private View mConvertView;
	
	private ViewHolder (Context context,ViewGroup parent,int layoutId,int position){
		mPosition = position;
		this.mViews = new SparseArray<View>();
		this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,false);
		mConvertView.setTag(this);
	}
	
	public static ViewHolder get(Context context,View convertView,ViewGroup parent,int layoutId,int position){
		if(convertView == null){
			return new ViewHolder(context, parent, layoutId, position);
		}else{
			ViewHolder holder = (ViewHolder) convertView.getTag();
			mPosition = position;
			return holder;
		}
	}
	
	
	public View getConvertView(){
		return this.mConvertView;
	}
	
	/**
	 * 通过resId 获取控件
	 * @param resId
	 * @return
	 */
	public <T extends View> T getView(int resId){
		View view = mViews.get(resId);
		if(view == null){
			view = mConvertView.findViewById(resId);
			mViews.put(resId, view);
		}
		
		return (T)view;
	}
	
	/**
	 * 给TextView 设置文本
	 * @param resId TextView的ID 
	 * @param text 文本信息
	 * @return
	 */
	public ViewHolder setText(int resId,String text){
		TextView textView = getView(resId);
		textView.setText(text);
		return this;
	}
	
	public ViewHolder setText(int resId,CharSequence text){
		TextView textView = getView(resId);
		textView.setText(text);
		return this;
	}
	
	public ViewHolder setText(int resId,Spannable span){
		TextView textView = getView(resId);
		textView.setText(span);
		return this;
	}
	
	/**
	 * 给ImageView  设置图片  目前只支持本地图片
	 * @param resId ImageView ID
	 * @param imageId  图片ID
	 * @return
	 */
	public ViewHolder setImage(int resId,int imageId){
		ImageView imageView = getView(resId);
		imageView.setImageResource(imageId);
		return this;
	}
	
	public ViewHolder setGone(int resId){
		this.getView(resId).setVisibility(View.GONE);
		return this;
	}
	
	public ViewHolder setInvisible(int resId){
		this.getView(resId).setVisibility(View.INVISIBLE);
		return this;
	}
	
	public ViewHolder setVisible(int resId){
		this.getView(resId).setVisibility(View.VISIBLE);
		return this;
	}
	
}
