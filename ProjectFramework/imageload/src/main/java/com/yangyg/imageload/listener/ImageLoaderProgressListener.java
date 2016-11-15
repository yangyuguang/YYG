package com.yangyg.imageload.listener;

/**
 * 图片加载过程的监听
 * Created by 杨裕光 on 16/11/9.
 */
public interface ImageLoaderProgressListener {
    /**
     * 开始加载
     */
    void onStartLoader();

    /**
     * 加载进度更新
     * @param current
     * @param total
     */
    void onProgressUpdate(int current, int total);

    /**
     * 完成加载
     */
    void onCompleteLoading();
}
