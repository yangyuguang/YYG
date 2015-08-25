package com.pami.listener;

public interface VolleyLoadImageListener {

	/**
	 * 开始加载图片
	 */
	public void onStartLoadImage();
	/**
	 * 正在加载图片
	 */
	public void onStartLoadingImage();
	/**
	 * 加载结束
	 */
	public void onEndLoadImage();
	
}
