package com.pami.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

@SuppressLint("NewApi")
public class MyScrollView extends ScrollView {

	public MyScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}


	private OnScrollListener onScrollListener;

	/**
	 * 设置滚动接口
	 * 
	 * @param onScrollListener
	 */
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}
	
	/**
	 * 滚动时会给接口回调信息
	 */
	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		onScrollListener.onScroll();
	}


	/**
	 * 滚动的回调接口
	 */
	public interface OnScrollListener {
		public void onScroll();
	}
}
