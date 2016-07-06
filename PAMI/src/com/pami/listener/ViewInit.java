package com.pami.listener;

import android.os.Bundle;

public interface ViewInit {
	
	/**
	 * 从布局文件初始化界面
	 */
	void initViewFromXML(Bundle arg0)throws Exception;
	
	/**
	 * 初始化数据
	 */
	void initData()throws Exception;
	
	/**
	 * 填充数据
	 */
	void fillView()throws Exception;
	
	/**
	 * 初始化监听
	 */
	void initListener()throws Exception;
}
