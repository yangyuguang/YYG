package com.pami.listener;

public interface ThreadDownLoadListener {
	/**
	 * 单个线程下载
	 * @param downLen 下载的数据量
	 */
	void onThreadDownLoad(long downLen);
	/**
	 * 单个线程下载完成
	 * @param threadId 线程ID
	 */
	void onThreadDownLoadComplete(int threadId);
}
