package com.pami.filemultipledownLoad;

public interface FileMultipleThreadDownLoadListener {

	/**
	 * 开始下载
	 */
	void onStartDownLoad();
	/**
	 * 下载中
	 * @param totalLen 文件的总长度
	 * @param curLen 当前已下载的长度
	 */
	void onDownLoading(long totalLen,long curLen);
	/**
	 * 完成下载
	 * @param filePath
	 */
	void onCompleteDownLoading(String filePath);
	
	/**
	 * 下载失败
	 * @param errorCode 错误码
	 * @param errorMsg  错误信息
	 */
	void onDownLoadError(int errorCode,String errorMsg);
}
