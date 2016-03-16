package com.yangyg.adapter;


import com.yangyg.utils.ImageLoaderUtils;

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
	private Context mContext = null;

	private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
		this.mPosition = position;
		this.mContext = context;
		this.mViews = new SparseArray<View>();
		this.mConvertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}

	public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			mPosition = position;
			return holder;
		}
	}

	/**
	 * 获取Item的View 对象
	 * 
	 * @return
	 */
	public View getConvertView() {
		return this.mConvertView;
	}

	/**
	 * 通过resId 获取控件
	 * 
	 * @param resId
	 * @return
	 */
	public <T extends View> T getView(int resId) {
		View view = mViews.get(resId);
		if (view == null) {
			view = mConvertView.findViewById(resId);
			mViews.put(resId, view);
		}

		if (view == null) {
			return null;
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
	public ViewHolder setText(int resId, String text) throws Exception {
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
	public ViewHolder setText(int resId, CharSequence text) throws Exception {
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
	public ViewHolder setText(int resId, Spannable span) throws Exception {
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
	public ViewHolder setImage(int resId, int imageId) throws Exception {
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
	public ViewHolder setImage(int resId, String url) throws Exception {
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
	public ViewHolder setGone(int resId) throws Exception {
		this.getView(resId).setVisibility(View.GONE);
		return this;
	}

	/**
	 * 设置View 为INVISIBLE
	 * @param resId view的ID
	 * @return
	 * @throws Exception
	 */
	public ViewHolder setInvisible(int resId) throws Exception {
		this.getView(resId).setVisibility(View.INVISIBLE);
		return this;
	}

	/**
	 * 设置View 为VISIBLE
	 * @param resId view的ID
	 * @return
	 * @throws Exception
	 */
	public ViewHolder setVisible(int resId) throws Exception {
		this.getView(resId).setVisibility(View.VISIBLE);
		return this;
	}

}
